-- Function: action.createactions(integer)

-- DROP FUNCTION action.createactions(integer);

CREATE OR REPLACE FUNCTION action.createactions(p_actionplanjobid integer)
  RETURNS boolean AS
$BODY$
DECLARE
v_text text;
v_survey_name text;
v_plan_name text;
v_plan_description text;
v_errmess text;
v_actionplanid integer;
v_currentdatetime timestamp;
v_surveyenddate timestamp;
v_number_of_rows integer;

BEGIN

   SELECT j.actionplanid FROM action.actionplanjob j WHERE j.actionplanjobid = p_actionplanjobid INTO v_actionplanid;

   v_currentdatetime := current_timestamp;
   v_number_of_rows := 0;
   
   -- Check cases for the actionplanid exist on the case table
   IF EXISTS (SELECT 1 FROM action.case WHERE actionplanid = v_actionplanid) THEN  
       
      -- Get the survey name and survey end date using the actionplanid passed in
      SELECT s.name,s.surveyenddate 
      FROM action.survey s
          ,action.actionplan a 
      WHERE a.surveyid = s.surveyid
      AND a.actionplanid = v_actionplanid INTO v_survey_name,v_surveyenddate;

      -- Get plan description for messagelog using the actionplanid passed in
      SELECT p.name, p.description 
      FROM action.actionplan p 
      WHERE p.actionplanid = v_actionplanid INTO v_plan_name,v_plan_description;          

      v_text := '*** SURVEY NAME: ' || v_survey_name || ' *** RUNNING ACTION PLAN JOBID: ' ||p_actionplanjobid || ' ACTION PLAN: ' || v_actionplanid || ' ***';
      PERFORM action.logmessage(p_messagetext := v_text
                               ,p_jobid := p_actionplanjobid    
                               ,p_messagelevel := 'INFO'                                                  
                               ,p_functionname := 'action.createactions');     
                                   
      v_text := '*** PLAN NAME: ' || v_plan_name || ' ' || v_plan_description;
      PERFORM action.logmessage(p_messagetext := v_text
                               ,p_jobid := p_actionplanjobid    
                               ,p_messagelevel := 'INFO'                                                  
                               ,p_functionname := 'action.createactions');  
                               
      INSERT INTO action.action
        (
         actionid
        ,caseid 
        ,actionplanid 
        ,actionruleid
        ,actiontypeid 
        ,createdby 
        ,manuallycreated
        ,situation
        ,state
        ,createddatetime 
        ,updateddatetime
        )
      SELECT  
        nextval('action.actionidseq')
        ,l.caseid
        ,l.actionplanid
        ,l.actionruleid
        ,l.actiontypeid
        ,'SYSTEM'
        ,FALSE
        ,NULL
        ,'SUBMITTED'
        ,v_currentdatetime
        ,v_currentdatetime
       FROM 
        (SELECT c.caseid
               ,r.actionplanid
               ,r.actionruleid
               ,r.actiontypeid                                    
         FROM action.actionrule r
              ,action.case c 
         WHERE  c.actionplanid = v_actionplanid
         AND    r.actionplanid = c.actionplanid         
         AND r.surveydatedaysoffset <= EXTRACT(DAY FROM (v_currentdatetime - c.actionplanstartdate)) -- looking at start date to see if the rule is due
         AND EXTRACT(DAY FROM (v_surveyenddate - v_currentdatetime)) >= 0 -- looking at survey end date to see if rule should run          
         EXCEPT 
         SELECT a.caseid
               ,a.actionplanid
               ,a.actionruleid
               ,a.actiontypeid     
         FROM action.action a
             ,action.actionrule r
             ,action.case c 
         WHERE c.actionplanid = v_actionplanid
         AND   c.caseid = a.caseid
         AND   c.actionplanid = a.actionplanid
         AND   a.actionplanid = r.actionplanid 
         AND   a.actionruleid = r.actionruleid) l;

                              
       GET DIAGNOSTICS v_number_of_rows = ROW_COUNT; -- number of actions inserted                         
          
       IF v_number_of_rows = 0 THEN 
           v_text := 'NO NEW CASE ACTION(S) CREATED FOR PLAN: ' || v_actionplanid;
       ELSE
           v_text := v_number_of_rows  || ' NEW CASE ACTION(S) CREATED FOR PLAN: ' || v_actionplanid;
       END IF;
       PERFORM action.logmessage(p_messagetext := v_text
                                ,p_jobid := p_actionplanjobid    
                                ,p_messagelevel := 'INFO'                                                  
                                ,p_functionname := 'action.createactions');          

        v_text := 'ACTION PLAN JOBID: ' || p_actionplanjobid || ' ACTION PLAN: ' || v_actionplanid || ' COMPLETE (action.actionplanjob table updated)'; 
        PERFORM action.logmessage(p_messagetext := v_text
                                 ,p_jobid := p_actionplanjobid    
                                 ,p_messagelevel := 'INFO'                                                  
                                 ,p_functionname := 'action.createactions');                           
   ELSE
      v_text := 'INFO: NO RECORDS FOUND ON action.case TABLE FOR ACTION PLAN: ' ||v_actionplanid;
      PERFORM action.logmessage(p_messagetext := v_text
                               ,p_jobid := p_actionplanjobid    
                               ,p_messagelevel := 'INFO'                                                  
                               ,p_functionname := 'action.createactions');
   END IF;      
      
   -- Update the date the actionplan was run on the actionplan table 
   UPDATE action.actionplan 
   SET lastrundatetime = v_currentdatetime
   WHERE actionplanid  = v_actionplanid;  

   -- Update the date the actionplan was run on the actionplanjob table 
   UPDATE action.actionplanjob 
   SET updateddatetime = v_currentdatetime
      ,state = 'COMPLETED'
   WHERE actionplanjobid =  p_actionplanjobid
   AND   actionplanid    =  v_actionplanid;     
    
RETURN TRUE;

EXCEPTION

WHEN OTHERS THEN
    v_errmess := SQLSTATE;    
    PERFORM action.logmessage(p_messagetext := 'CREATE ACTION(S) EXCEPTION TRIGGERED SQLERRM: ' || SQLERRM || ' SQLSTATE : ' || v_errmess
                             ,p_jobid := p_actionplanjobid   
                             ,p_messagelevel := 'FATAL'
                             ,p_functionname := 'action.createactions');
  RETURN FALSE;    
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION action.createactions(integer)
  OWNER TO postgres;

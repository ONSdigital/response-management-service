--script to create all tables, functions and sequences for foundation for 2017
--10 tables, 3 sequences, 2 functions

SET SCHEMA 'action';

CREATE FUNCTION createactions(p_actionplanjobid integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
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
$$;

CREATE FUNCTION logmessage(p_messagetext text DEFAULT NULL::text, p_jobid numeric DEFAULT NULL::numeric, p_messagelevel text DEFAULT NULL::text, p_functionname text DEFAULT NULL::text) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
v_text TEXT ;
v_function TEXT;
BEGIN
INSERT INTO action.messagelog
(messagetext, jobid, messagelevel, functionname, createddatetime )
values (p_messagetext, p_jobid, p_messagelevel, p_functionname, current_timestamp);
  RETURN TRUE;
EXCEPTION
WHEN OTHERS THEN
RETURN FALSE;
END;
$$;

CREATE SEQUENCE actionidseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;

CREATE TABLE action (
    actionid bigint DEFAULT nextval('actionidseq'::regclass) NOT NULL,
    caseid bigint NOT NULL,
    actionplanid integer,
    actionruleid integer,
    actiontypeid integer NOT NULL,
    createdby character varying(50) NOT NULL,
    manuallycreated boolean NOT NULL,
    priority integer DEFAULT 3,
    situation character varying(100),
    state character varying(20) NOT NULL,
    createddatetime timestamp with time zone NOT NULL,
    updateddatetime timestamp with time zone,
    optlockversion integer DEFAULT 0
);

COMMENT ON COLUMN action.priority IS '1 = highest, 5 = lowest';

CREATE TABLE actionplan (
    actionplanid integer NOT NULL,
    surveyid integer NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250) NOT NULL,
    createdby character varying(20) NOT NULL,
    lastrundatetime timestamp with time zone
);

CREATE SEQUENCE actionplanjobseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;

CREATE TABLE actionplanjob (
    actionplanjobid integer DEFAULT nextval('actionplanjobseq'::regclass) NOT NULL,
    actionplanid integer NOT NULL,
    createdby character varying(20) NOT NULL,
    state character varying(100) NOT NULL,
    createddatetime timestamp with time zone NOT NULL,
    updateddatetime timestamp with time zone
);

CREATE TABLE actionplanjobstate (
    state character varying(100) NOT NULL
);

CREATE TABLE actionrule (
    actionruleid integer NOT NULL,
    actionplanid integer NOT NULL,
    actiontypeid integer NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250) NOT NULL,
    surveydatedaysoffset integer NOT NULL,
    priority integer DEFAULT 3
);

COMMENT ON COLUMN actionrule.priority IS '1 = highest, 5 = lowest';

CREATE TABLE actionstate (
    state character varying(100) NOT NULL
);

CREATE TABLE actiontype (
    actiontypeid integer NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250) NOT NULL,
    handler character varying(100),
    cancancel boolean NOT NULL
);

CREATE TABLE "case" (
    actionplanid integer NOT NULL,
    caseid bigint NOT NULL,
    actionplanstartdate timestamp with time zone NOT NULL
);

CREATE SEQUENCE messageseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;

CREATE TABLE messagelog (
    messageid bigint DEFAULT nextval('messageseq'::regclass) NOT NULL,
    messagetext character varying,
    jobid numeric,
    messagelevel character varying,
    functionname character varying,
    createddatetime timestamp with time zone
);

CREATE TABLE survey (
    surveyid integer NOT NULL,
    surveystartdate timestamp with time zone NOT NULL,
    name character varying(100) NOT NULL,
    surveyenddate timestamp with time zone NOT NULL
);

ALTER TABLE ONLY action
    ADD CONSTRAINT actionid_pkey PRIMARY KEY (actionid);

ALTER TABLE ONLY actionplan
    ADD CONSTRAINT actionplanid_pkey PRIMARY KEY (actionplanid);

ALTER TABLE ONLY actionplanjob
    ADD CONSTRAINT actionplanjobid_pkey PRIMARY KEY (actionplanjobid);

ALTER TABLE ONLY actionplanjobstate
    ADD CONSTRAINT actionplanjobstate_pkey PRIMARY KEY (state);

ALTER TABLE ONLY actionrule
    ADD CONSTRAINT actionruleid_pkey PRIMARY KEY (actionruleid);

ALTER TABLE ONLY actionstate
    ADD CONSTRAINT actionstate_pkey PRIMARY KEY (state);

ALTER TABLE ONLY actiontype
    ADD CONSTRAINT actiontypeid_pkey PRIMARY KEY (actiontypeid);

ALTER TABLE ONLY messagelog
    ADD CONSTRAINT messageid_pkey PRIMARY KEY (messageid);

ALTER TABLE ONLY survey
    ADD CONSTRAINT surveyid_pkey PRIMARY KEY (surveyid);

ALTER TABLE ONLY actionplanjob
    ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES actionplan(actionplanid);

ALTER TABLE ONLY actionrule
    ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES actionplan(actionplanid);

ALTER TABLE ONLY action
    ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES actionplan(actionplanid);

ALTER TABLE ONLY actionplanjob
    ADD CONSTRAINT actionplanjobstate_fkey FOREIGN KEY (state) REFERENCES actionplanjobstate(state);

ALTER TABLE ONLY action
    ADD CONSTRAINT actionruleid_fkey FOREIGN KEY (actionruleid) REFERENCES actionrule(actionruleid);

ALTER TABLE ONLY action
    ADD CONSTRAINT actionstate_fkey FOREIGN KEY (state) REFERENCES actionstate(state);

ALTER TABLE ONLY actionrule
    ADD CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid) REFERENCES actiontype(actiontypeid);

ALTER TABLE ONLY action
    ADD CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid) REFERENCES actiontype(actiontypeid);

ALTER TABLE ONLY actionplan
    ADD CONSTRAINT surveyid_fkey FOREIGN KEY (surveyid) REFERENCES survey(surveyid);

ALTER TABLE ONLY "case"
    ADD CONSTRAINT  actionplanid_fkey FOREIGN KEY (actionplanid)
  REFERENCES action.actionplan (actionplanid);

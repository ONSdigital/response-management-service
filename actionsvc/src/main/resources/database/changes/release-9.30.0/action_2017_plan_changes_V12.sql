-- Change required for Version 12 of Action plan set up data

-- Delete plan actionplanid 12 and 13
DELETE FROM action.actionrule
WHERE actionplanid IN(12,13);

-- Delete plan actionplanid 12 and 13
DELETE FROM action.actionplan
WHERE actionplanid IN(12,13);


-- Action types V12
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel,responserequired) VALUES (55,'ICLB1_2003','Print Initial Contact Letter (behavioural insights)', 'Printer', false,false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel,responserequired) VALUES (56,'1RLB1_0504','Print reminder letter 1 (behavioural insights)'     , 'Printer', true,true);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel,responserequired) VALUES (57,'2RLB1_1804','Print reminder letter 2 (behavioural insights)'     , 'Printer', true,true);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel,responserequired) VALUES (58,'3RLB1_2604','Print reminder letter 3 (behavioural insights)'     , 'Printer', true,true);


-- Action plans v12
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (55, 1,  'C2EO331BIE', 'Component 2 - England/online/no field/three reminders (behavioural insight)','SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (56, 1,  'C2EO332BIE', 'Component 2 - England/online/no field/two reminders/paper q  (behavioural insight)'          ,'SYSTEM',NULL);


-- Action rules V12
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55,117,55,'ICLB1_2003-25' ,'',-25,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55,118,56,'1RLB1_0504-4' ,'',-4,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55,119,57,'2RLB1_1804+9' ,'',9,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55,120,58,'3RLB1_2604+17','',17,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56,121,55,'ICLB1_2003-25','',-25,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56,122,56,'1RLB1_0504-4' ,'',-4,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56,123,57,'2RLB1_1804+9' ,'',9,3);
INSERT INTO action.actionrule (actionplanid, actionruleid,  actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56,124,19,'HH_H1+17'     ,'',17,3);


-- Create actionrule description
 WITH ruledesc AS 
 (SELECT r.actionruleid
        ,r.description as old
        ,CASE WHEN r.surveydatedaysoffset >= 0 THEN (trim(t.description) || '(SD+' || r.surveydatedaysoffset::text ||')') 
              ELSE  (trim(t.description) || '(SD' || r.surveydatedaysoffset::text ||')') END as new
 FROM  action.actiontype t,action.actionrule r
 WHERE r.actiontypeid = t.actiontypeid)

 UPDATE action.actionrule ar
 SET description =  (SELECT r.new FROM ruledesc r WHERE r.old <> r.new AND ar.actionruleid = r.actionruleid)
 WHERE ar.actionruleid IN(SELECT actionruleid from ruledesc WHERE old <> new);



SET SCHEMA 'action';

-- Action Types
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1,'BRESEL' ,'Enrolment Letter','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2,'BRESERL','Enrolment Reminder Letter','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3,'BRESSNE','Survey Notification Email','Notify','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4,'BRESSRE','Survey Reminder Email','Notify','n');

-- Action Plans
INSERT INTO action.actionplan (actionplanid, name, description, createdby, lastrundatetime) VALUES (1,'Enrolment','BRES Enrolment','SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, name, description, createdby, lastrundatetime) VALUES (2,'BRES','BRES','SYSTEM',NULL);

-- Action Rules
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, daysoffset) VALUES (1,1,1,'NULL','NULL',0);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, daysoffset) VALUES (2,1,2,'NULL','NULL',82);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, daysoffset) VALUES (3,2,3,'NULL','NULL',8);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, daysoffset) VALUES (4,2,4,'NULL','NULL',84);


-- Create actionrule description and name from the action type
UPDATE action.actionrule ar
SET description =  (SELECT t1.description || '(+' || ar.daysoffset || ' days)' FROM action.actiontype t1 WHERE ar.actiontypeid = t1.actiontypeid) 
   ,name        =  (SELECT t2.name || '+' || ar.daysoffset FROM action.actiontype t2 WHERE ar.actiontypeid = t2.actiontypeid)
WHERE EXISTS (SELECT 1 FROM action.actiontype t3 WHERE ar.actiontypeid = t3.actiontypeid);
























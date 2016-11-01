
set schema 'action';

-- Drop foreign keys constraints on action, case and actionplanjob
ALTER TABLE action.case DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionruleid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actiontypeid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.actionplanjob DROP CONSTRAINT actionplanid_fkey;

-- Delete Individual action plans, rules and types for England - allows re run of code
DELETE FROM action.actionrule WHERE actionruleid IN(61,62,63,64,65);
DELETE FROM action.actiontype WHERE actiontypeid IN(38,39,40,41,42);
DELETE FROM action.actionplan WHERE actionplanid IN(17,18,19,20,21);


-- Insert action plans for Indivduals in England
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (17, 1, 'I1S-P', 'Individual - England/paper/sexual id', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (18, 1, 'I1-P', 'Individual - England/paper/without sexual id', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (19, 1, 'I-SMS', 'Individual - England/SMS', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (20, 1, 'I-Email', 'Individual - England/Email', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (21, 1, 'I-Letter', 'Individual - England/Letter', 'SYSTEM', NULL);

-- Insert action types for Indivduals in England
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (38, 'I1S_OR', 'Print Individual paper questionnaire (english with sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (39, 'I1_OR', 'Print Individual paper questionnaire (english without sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (40, 'ISMS', 'Send Individual Internet Access Code', 'Notify', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (41, 'IEMAIL', 'Send Individual Internet Access Code', 'Notify', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (42, 'ILETTER', 'Send Individual Internet Access Code', 'Notify', false);

-- Insert action rules for Indivduals in England
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (61, 17, 38, 'I1S_OR-0', 'Print Individual Questionnaire (SD-0)', 0, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (62, 18, 39, 'I1_OR-0', 'Print Individual Questionnaire (SD-0)', 0, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (63, 19, 40, 'ISMS-0', 'Send individual IAC via SMS', 0, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (64, 20, 41, 'IEMAIL-0', 'Send individual IAC via EMAIL', 0, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (65, 21, 42, 'ILETTER-0', 'Send individual IAC via LETTER', 0, 3);

-- Reinstate the foreign keys constraints on action, case and actionplanjob

ALTER TABLE action.case ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid) REFERENCES action.actiontype (actiontypeid);
ALTER TABLE action.action ADD CONSTRAINT actionruleid_fkey FOREIGN KEY (actionruleid) REFERENCES action.actionrule (actionruleid);
ALTER TABLE action.actionplanjob ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);


set schema 'action';
ALTER TABLE action.case DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionruleid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actiontypeid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.actionplanjob DROP CONSTRAINT actionplanid_fkey;


-- Insert action plans for Replacements England
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (20, 1, 'H1SD4-P', 'Replacement - England/paper/sexual id/field day four', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (21, 1, 'H1SD10-P', 'Replacement - England/paper/sexual id/field day ten', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (22, 1, 'H1S-P', 'Replacement - England/paper/sexual id/no field', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (23, 1, 'H1D4-P', 'Replacement - England/paper/without sexual id/field day four', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (24, 1, 'H1D10-P', 'Replacement - England/paper/without sexual id/field day ten', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (25, 1, 'H1-P', 'Replacement - England/paper/without sexual id/no field', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (26, 1, 'R-SMS4', 'Replacement - England/SMS/field day four', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (27, 1, 'R-SMS10', 'Replacement - England/SMS/field day ten', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (28, 1, 'R-SMS', 'Replacement - England/SMS/No field', 'SYSTEM', NULL);


-- Insert action types for Replacements England
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (41, 'H1S_OR', 'Print on request household paper questionnaire (english with sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (42, 'H1_OR', 'Print on request household paper questionnaire (english without sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (43, 'RSMS', 'Send Replacement Internet Access Code', 'Notify', false);


-- Insert action rules for Replacements England
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (64,20,41,'H1S_OR-100' ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (65,20,14,'HH_CV+3'    ,'Create Household Visit (SD+3)'                 ,3,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (66,21,41,'H1S_OR-100' ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (67,21,14,'HH_CV+9'    ,'Create Household Visit (SD+9)'                 ,9,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (68,22,41,'H1S_OR-100' ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (69,23,42,'H1_OR-100'  ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (70,23,14,'HH_CV+3'    ,'Create Household Visit (SD+3)'                 ,3,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (71,24,42,'H1_OR-100'  ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (72,24,14,'HH_CV+9'    ,'Create Household Visit (SD+9)'                 ,9,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (73,25,42,'H1_OR-100'  ,'Print Replacement Paper Questionnaire (SD-100)',-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (74,26,43,'RSMS-100'   ,'Send replacement IAC via SMS (SD-100)'          ,-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (75,26,14,'HH_CV+3'    ,'Create Household Visit (SD+3)'                 ,3,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (76,27,43,'RSMS-100'   ,'Send replacement IAC via SMS (SD-100)'         ,-100,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (77,27,14,'HH_CV+9'    ,'Create Household Visit (SD+9)'                 ,9,3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (78,28,43,'RSMS-100'   ,'Send replacement IAC via SMS'                  ,-100,3);



ALTER TABLE action.case ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid) REFERENCES action.actiontype (actiontypeid);
ALTER TABLE action.action ADD CONSTRAINT actionruleid_fkey FOREIGN KEY (actionruleid) REFERENCES action.actionrule (actionruleid);
ALTER TABLE action.actionplanjob ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);

SET schema 'action';


TRUNCATE TABLE action.actionplan cascade;
TRUNCATE TABLE action.actiontype cascade;
TRUNCATE TABLE action.survey cascade;


INSERT INTO action.survey(surveyid,surveystartdate,surveyenddate,name) VALUES (1,'2017-04-09','2017-05-09','2017 Test');


--
-- Data for Name: actionplan; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (1, 1, 'C1O331D4E', 'Component 1 - England/online/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (2, 1, 'C1O331D4W', 'Component 1 - Wales/online/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (3, 1, 'C1O331D10E', 'Component 1 - England/online/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (4, 1, 'C1O331D10W', 'Component 1 - Wales/online/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (5, 1, 'C2SP331E', 'Component 2 - England/paper/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (6, 1, 'C2SP331W', 'Component 2 - Wales/paper/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (7, 1, 'C2EP331E', 'Component 2 - England/paper/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (8, 1, 'C2EP331W', 'Component 2 - Wales/paper/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (9, 1, 'C2O331E', 'Component 2 - England/online/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (10, 1, 'C2O331W', 'Component 2 - Wales/online/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (11, 1, 'C2EO332E', 'Component 2 - England/online/no field/three reminders (final reminder HH questionnaire)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (12, 1, 'C2EO321E', 'Component 2 - England/online/no field/two reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (13, 1, 'C2EO322E', 'Component 2 - England/online/no field/two reminders (final reminder HH questionnaire)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (14, 1, 'C2EO300E', 'Component 2 - England/online/no field/no reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (15, 1, 'C2EO200E', 'Component 2 - England/online/no field/no reminders (later initial contact)', 'SYSTEM', NULL);	
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (16, 1, 'C2EO331ADE', 'Component 2 - England/online/no field/three reminders (Assisted Digital)', 'SYSTEM', NULL);



--
-- Data for Name: actiontype; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1, 'ICL1_2003', 'Print initial contact letter (english) ', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2, 'ICL2W_2003', 'Print initial contact letter (welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3, 'ICL1_2703', 'Print initial contact letter (english) ', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4, 'ICLAD_2003', 'Print initial contact letter (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5, '1RL1_0504', 'Print reminder letter 1 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6, '1RL2W_0504', 'Print reminder letter 1 (welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7, '1RLAD_0504', 'Print reminder letter 1 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8, '2RL1_1804', 'Print reminder letter 2 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9, '2RL2W_1804', 'Print reminder letter 2 (welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10, '2RLAD_1804', 'Print reminder letter 2 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11, '3RL1_2604', 'Print reminder letter 3 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (12, '3RL2W_2604', 'Print reminder letter 3 (welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (13, '3RLAD_2604', 'Print reminder letter 3 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (14, 'HouseholdCreateVisit', 'Create Visit', 'Field', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (15, 'H1S_2003', 'Print household paper questionnaire (english with sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (16, 'H2S_2003', 'Print household paper questionnaire (welsh with sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (17, 'H1_2003', 'Print household paper questionnaire (english without sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (18, 'H2_2003', 'Print household paper questionnaire (welsh without sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (19, 'H1_2604Q4', 'Print household paper questionnaire (english without sexual id)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (20, 'GC_ESCALATION', 'General complaint escalation', 'CensusSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (21, 'GE_ESCALATION', 'General enquiry escalation', 'CensusSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (22, 'FC_ESCALATION', 'Field complaint escalation', 'FieldSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (23, 'FE_ESCALATION', 'Field emergency escalation', 'FieldSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (24, 'QGPOL', 'Print translation booklet - Polish', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (25, 'QGCAN', 'Print translation booklet - Cantonese', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (26, 'QGSPA', 'Print translation booklet - Spanis', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (27, 'QGARA', 'Print translation booklet - Arabic', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (28, 'QGSOM', 'Print translation booklet - Somali', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (29, 'QGMAN', 'Print translation booklet - Mandarin', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (30, 'QGBEN', 'Print translation booklet - Bengali', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (31, 'QGPOR', 'Print translation booklet - Portuguese', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (32, 'QGGUR', 'Print translation booklet - Punjabi - (Gurmukhi)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (33, 'QGSHA', 'Print translation booklet - Punjabi - (Shahmuki)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (34, 'QGTUR', 'Print translation booklet - Turkish', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (35, 'QGLIT', 'Print translation booklet - Lithuanian', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (36, 'QGURD', 'Print translation booklet - Urdu', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (37, 'QGGUJ', 'Print translation booklet - Gujarati', 'Printer', false);	





--
-- Data for Name: actionrule; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (1, 1, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (2, 1, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (3, 1, 14, 'HH_CV+3', 'Create Household Visit (SD+3)', 3, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (4, 1, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (5, 1, 11, 'HH_3RL1+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (6, 2, 2, 'HH_ICL2W-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (7, 2, 6, 'HH_1RL2W-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (8, 2, 14, 'HH_CV+3', 'Create Household Visit (SD+3)', 3, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (9, 2, 9, 'HH_2RL2W+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (10, 2, 12, 'HH_3RL2W+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (11, 3, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (12, 3, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (13, 3, 14, 'HH_CV+9', 'Create Household Visit (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (14, 3, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (15, 3, 11, 'HH_3RL1+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (16, 4, 2, 'HH_ICL2W-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (17, 4, 6, 'HH_1RL2W-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (18, 4, 14, 'HH_CV+9', 'Create Household Visit (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (19, 4, 9, 'HH_2RL2W+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (20, 4, 12, 'HH_3RL2W+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (21, 5, 15, 'HH_H1S-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (22, 5, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (23, 5, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (24, 5, 11, 'HH_3RL1+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (25, 6, 16, 'HH_H2S-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (26, 6, 6, 'HH_1RL2W-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (27, 6, 9, 'HH_2RL2W+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (28, 6, 12, 'HH_3RL2W+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (29, 7, 17, 'HH_H1-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (30, 7, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (31, 7, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (32, 7, 11, 'HH_3RL1+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (33, 8, 18, 'HH_H2-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (34, 8, 6, 'HH_1RL2W-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (35, 8, 9, 'HH_2RL2W+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (36, 8, 12, 'HH_3RL2W+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (37, 9, 1, 'HH_ICL1-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (38, 9, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (39, 9, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (40, 9, 11, 'HH_3RL1+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (41, 10, 2, 'HH_ICL2W-25', 'Print Paper Questionnaire (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (42, 10, 6, 'HH_1RL2W-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (43, 10, 9, 'HH_2RL2W+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (44, 10, 12, 'HH_3RL2W+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (45, 11, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (46, 11, 5, 'HH_1RL1-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (47, 11, 8, 'HH_2RL1+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (48, 11, 19, 'HH_H1+17', 'Print Paper Questionnaire (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (49, 12, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (50, 12, 8, 'HH_2RL1+9', 'Print Reminder Letter 1 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (51, 12, 11, 'HH_3RL1+17', 'Print Reminder Letter 2 (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (52, 13, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (53, 13, 8, 'HH_2RL1+9', 'Print Reminder Letter 1 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (54, 13, 19, 'HH_H1+17', 'Print Paper Questionnaire (SD+17)', 17, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55, 14, 1, 'HH_ICL1-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56, 15, 3, 'HH_ICL1-18', 'Print Initial Contact Letter (SD-18)', -18, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (57, 16, 4, 'HH_ICLAD-25', 'Print Initial Contact Letter (SD-25)', -25, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (58, 16, 7, 'HH_1RLAD-4', 'Print Reminder Letter 1 (SD-4)', -4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (59, 16, 10, 'HH_2RLAD+9', 'Print Reminder Letter 2 (SD+9)', 9, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (60, 16, 13, 'HH_3RLAD+17', 'Print Reminder Letter 3 (SD+17)', 17, 3);	

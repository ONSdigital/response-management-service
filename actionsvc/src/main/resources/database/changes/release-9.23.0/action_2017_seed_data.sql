SET schema 'action';


TRUNCATE TABLE action.actionplan cascade;
TRUNCATE TABLE action.actiontype cascade;
TRUNCATE TABLE action.survey cascade;


INSERT INTO survey (surveyid, surveydate, name) VALUES (1, '2017-04-09 23:00:00+00', '2017 Test');

--
-- Data for Name: actionplan; Type: TABLE DATA; Schema: action; Owner: postgres
--
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (6, 1, 'C1EO331D4W', 'Component 1 - Wales/online/without sexual id/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (7, 1, 'C1EO331D10E', 'Component 1 - England/online/without sexual id/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (8, 1, 'C1EO331D10W', 'Component 1 - Wales/online/without sexual id/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (9, 1, 'C2SP331E', 'Component 2 - England/paper/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (2, 1, 'C1SO331D4W', 'Component 1 - Wales/online/sexual id/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (10, 1, 'C2SP331W', 'Component 2 - Wales/paper/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (11, 1, 'C2EP331E', 'Component 2 - England/paper/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (12, 1, 'C2EP331W', 'Component 2 - Wales/paper/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (1, 1, 'C1SO331D4E', 'Component 1 - England/online/sexual id/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (4, 1, 'C1SO331D10W', 'Component 1 - Wales/online/sexual id/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (14, 1, 'C2SO331W', 'Component 2 - Wales/online/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (5, 1, 'C1EO331D4E', 'Component 1 - England/online/without sexual id/field day four/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (3, 1, 'C1SO331D10E', 'Component 1 - England/online/sexual id/field day ten/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (16, 1, 'C2EO331W', 'Component 2 - Wales/online/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (13, 1, 'C2SO331E', 'Component 2 - England/online/sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (22, 1, 'C2EO331ADE', 'Component 2 - England/online/without sexual id/no field/three reminders (Assisted Digital)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (21, 1, 'C2EO200E', 'Component 2 - England/online/without sexual id/no field/no reminders (earlier initial contact)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (15, 1, 'C2EO331E', 'Component 2 - England/online/without sexual id/no field/three reminders', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (20, 1, 'C2EO300E', 'Component 2 - England/online/without sexual id/no field/no reminders ', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (19, 1, 'C2EO322E', 'Component 2 - England/online/without sexual id/no field/two reminders (final reminder HH questionnaire)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (17, 1, 'C2EO332E', 'Component 2 - England/online/without sexual id/no field/three reminders (final reminder HH questionnaire)', 'SYSTEM', NULL);
INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (18, 1, 'C2EO321E', 'Component 2 - England/online/without sexual id/no field/two reminders', 'SYSTEM', NULL);


--
-- Data for Name: actiontype; Type: TABLE DATA; Schema: action; Owner: postgres
--
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (0, 'TBC', 'To be confirmed', 'TBC', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1, 'HouseholdUploadIAC', 'Household upload IAC', 'HHSurvey', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2, 'ICL1', 'Print initial contact letter (english) ', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3, 'ICL2', 'Print initial contact letter (welsh in english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4, 'ICL2W', 'Print initial contact letter (welsh in welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5, 'ICL1AD', 'Print initial contact letter (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7, '1RL2W', 'Print reminder letter 1 (english/welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8, '1RLAD1', 'Print reminder letter 1 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9, '2RL1', 'Print reminder letter 2 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10, '2RL2W', 'Print reminder letter 2 (english/welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11, '2RLAD1', 'Print reminder letter 2 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (12, '3RL1', 'Print reminder letter 3 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (13, '3RL2W', 'Print reminder letter 3 (english/Welsh)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (14, '3RLAD1', 'Print reminder letter 3 (assisted digital)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (15, 'H1', 'Print household paper questionnaire (english without sexual id)', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (16, 'H2', 'Print household paper questionnaire (welsh in english without sexual id)','Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (17, 'H1S', 'Print household paper questionnaire (english with sexual id)', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (18, 'H2S', 'Print household paper questionnaire (welsh in english with sexual id)', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (19, 'H2W', 'Print household paper questionnaire (welsh in welsh without sexual id)', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (20, 'H2WS', 'Print household paper questionnaire (welsh in welsh with sexual id)', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (21, 'HouseholdCreateVisit', 'Household create visit', 'Field', true);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (22, 'GeneralEscalation', 'General escalation', 'CensusSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (23, 'SurveyEscalation', 'Survey escalation', 'CensusSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (24, 'ComplaintEscalation', 'Complaint escalation', 'CensusSupport', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (25, 'TBC', 'print replacement paper household questionnaire TO MATCH QSET OF HH - need IAC too so needs a plan', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (26, 'TBC', 'print paper individual questionnaire TO MATCH QSET OF HH - need IAC too so needs a plan', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (27, 'TBC', 'print replacement individual questionnaire TO MATCH QSET OF HH - need IAC too so needs a plan', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (28, 'TBC', 'print translation booklets (14 languages) MAYBE ONLINE - waiting for a decision on what to do', 'Fulfilment', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6, '1RL1', 'print reminder letter 1 (english)', 'Printer', false);
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (29, 'AddressCheck', 'Address check', 'Field', false);



--
-- Data for Name: actionrule; Type: TABLE DATA; Schema: action; Owner: postgres
--
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (51, 8, 21, 'HH_CV+10', 'Create household visit job (cd+10 days)', 10, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (18, 3, 21, 'HH_CV+10', 'Create household visit job (cd+10 days)', 10, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (25, 4, 21, 'HH_CV+10', 'Create household visit job (cd+10 days)', 10, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (4, 1, 21, 'HH_CV+4', 'Create household visit job (cd+4 days)', 4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (11, 2, 21, 'HH_CV+4', 'Create household visit job (cd+4 days)', 4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (7, 2, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (1, 1, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (33, 6, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (27, 5, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (20, 4, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (14, 3, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (2, 1, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (3, 1, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (5, 1, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (6, 1, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (8, 2, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (9, 2, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (10, 2, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (12, 2, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (13, 2, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (15, 3, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (16, 3, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (17, 3, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (19, 3, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (21, 4, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (22, 4, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (23, 4, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (24, 4, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (26, 4, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (28, 5, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (29, 5, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (31, 5, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (32, 5, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (34, 6, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (35, 6, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (36, 6, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (38, 6, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (39, 6, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (41, 7, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (42, 7, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (43, 7, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (45, 7, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (47, 8, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (48, 8, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (49, 8, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (50, 8, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (52, 8, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (54, 9, 17, 'HH_H1S-21', 'Print household paper questionnaire (english with sexual id) H1S (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55, 9, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56, 9, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (57, 9, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (59, 10, 18, 'HH_H2S-21', 'Print household paper questionnaire (welsh in english with sexual id) H2S (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (60, 10, 20, 'HH_H2WS-21', 'Print household paper questionnaire (welsh in welsh with sexual id) H2WS (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (61, 10, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (62, 10, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (63, 10, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (65, 11, 15, 'HH_H1-21', 'Print household paper questionnaire (english without sexual id) H1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (66, 11, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (67, 11, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (68, 11, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (70, 12, 16, 'HH_H2-21', 'Print household paper questionnaire (welsh in english without sexual id) H2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (71, 12, 19, 'HH_H2W-21', 'Print household paper questionnaire (welsh in welsh without sexual id) H2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (72, 12, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (73, 12, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (74, 12, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (76, 13, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (77, 13, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (78, 13, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (79, 13, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (81, 14, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (82, 14, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (83, 14, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (84, 14, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (85, 14, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (87, 15, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (88, 15, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (89, 15, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (90, 15, 12, 'HH_3RL1+21', 'Print reminder letter 3 (english) 3RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (92, 16, 3, 'HH_ICL2-21', 'Print initial contact letter (welsh in english) ICL2 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (93, 16, 4, 'HH_ICL2W-21', 'Print initial contact letter (welsh in welsh) ICL2W (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (94, 16, 7, 'HH_1RL2W-7', 'Print reminder letter 1 (english/welsh) 1RL2W (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (30, 5, 21, 'HH_CV+4', 'Create household visit job (cd+4 days)', 4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (44, 7, 21, 'HH_CV+10', 'Create household visit job (cd+10 days)', 10, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (37, 6, 21, 'HH_CV+4', 'Create household visit job (cd+4 days)', 4, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (69, 12, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (95, 16, 10, 'HH_2RL2W+7', 'Print reminder letter 2 (english/welsh) 2RL2W (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (96, 16, 13, 'HH_3RL2W+21', 'Print reminder letter 3 (english/Welsh) 3RL2W (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (98, 17, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (99, 17, 6, 'HH_1RL1-7', 'Print reminder letter 1 (english) 1RL1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (100, 17, 9, 'HH_2RL1+7', 'Print reminder letter 2 (english) 2RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (101, 17, 15, 'HH_H1+21', 'Print household paper questionnaire (english without sexual id) H1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (103, 18, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (104, 18, 6, 'HH_1RL1+7', 'Print reminder letter 1 (english) 1RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (105, 18, 9, 'HH_2RL1+21', 'Print reminder letter 2 (english) 2RL1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (107, 19, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (108, 19, 6, 'HH_1RL1+7', 'Print reminder letter 1 (english) 1RL1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (109, 19, 15, 'HH_H1+21', 'Print household paper questionnaire (english without sexual id) H1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (111, 20, 2, 'HH_ICL1-21', 'Print initial contact letter (english) ICL1 (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (113, 21, 2, 'HH_ICL1-14', 'Print initial contact letter (english) ICL1 (cd-14 days)', -14, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (115, 22, 5, 'HH_ICL1AD-21', 'Print initial contact letter (assisted digital) ICL1AD (cd-21 days)', -21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (116, 22, 8, 'HH_1RLAD1-7', 'Print reminder letter 1 (assisted digital) 1RLAD1 (cd-7 days)', -7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (117, 22, 11, 'HH_2RLAD1+7', 'Print reminder letter 2 (assisted digital) 2RLAD1 (cd+7 days)', 7, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (118, 22, 14, 'HH_3RLAD1+21', 'Print reminder letter 3 (assisted digital) 3RLAD1 (cd+21 days)', 21, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (80, 14, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (75, 13, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (46, 8, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (58, 10, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (64, 11, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (86, 15, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (91, 16, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (53, 9, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (40, 7, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (114, 22, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (112, 21, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (110, 20, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (106, 19, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (102, 18, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);
INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (97, 17, 1, 'HH_IAC-28', 'Upload household IAC code to online questionnaire (cd-28 days)', -28, 3);


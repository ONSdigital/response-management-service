
-- Update actiontype description
UPDATE action.actiontype
set description = 'Create Household Visit' 
WHERE actiontypeid = 14;


-- Action types V8
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (44, 'I2S_OR'  ,'Print individual paper questionnaire (welsh in english with sexual id)'    , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (45, 'I2WS_OR' ,'Print individual paper questionnaire (welsh in welsh with sexual id)'      , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (46, 'I2_OR'   ,'Print individual paper questionnaire (welsh in english without sexual id)' , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (47, 'I2W_OR'  ,'Print individual paper questionnaire (welsh in welsh without sexual id)'   , 'Printer', false);

-- Action plans v8
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (29, 1,  'I2S-P' , 'Individual - Wales/in english/paper/sexual id'        ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (30, 1,  'I2WS-P', 'Individual - Wales/in welsh/paper/sexual id'          ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (31, 1,  'I2-P'  , 'Individual - Wales/in english/paper/without sexual id','SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (32, 1,  'I2W-P' , 'Individual - Wales/in welsh/paper/without sexual id'  ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (33, 1,  'I-SMSE', 'Individual - England/SMS'                             ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (34, 1,  'I-SMSB', 'Individual - Bi-Lingual/SMS'                          ,'SYSTEM',NULL);

-- Action rules V8
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (79, 29, 44, 'I2S_OR-100' ,'', -100 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (80, 30, 45, 'I2WS_OR-100','', -100 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (81, 31, 46, 'I2_OR-100'  ,'', -100 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (82, 32, 47, 'I2W_OR-100' ,'', -100, 3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (83, 33, 40, 'SMSE-100'   ,'', -100 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (84, 34, 43, 'SMSB-100'   ,'', -100 ,3);




-- Action types V9
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (48, 'H2WS_OR' ,'Print on request household paper questionnaire (welsh in welsh with sexual id)'     , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (49, 'H2W_OR'  ,'Print on request household paper questionnaire (welsh in welsh without sexual id)'  , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (50, 'H2S_OR'  ,'Print on request household paper questionnaire (welsh in english with sexual id)'   , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (51, 'H2_OR'   ,'Print on request household paper questionnaire (welsh in english without sexual id)', 'Printer', false);

-- Action plans v9
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (35, 1,  'H2SD4-PWW',  'Replacement - Wales/welsh/paper/sexual id/field day four'       ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (36, 1,  'H2SD10-PWW', 'Replacement - Wales/welsh/paper/sexual id/field day ten'        ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (37, 1,  'H2S-PWW',    'Replacement - Wales/welsh/paper/sexual id/no field'             ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (38, 1,  'H2D4-PWW',   'Replacement - Wales/welsh/paper/without sexual id/field day four'  ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (39, 1,  'H2D10-PWW',  'Replacement - Wales/welsh/paper/without sexual id/field day ten'   ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (40, 1,  'H2-PWW',     'Replacement - Wales/welsh/paper/without sexual id/no field'        ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (41, 1,  'H2SD4-PWE',  'Replacement - Wales/english/paper/sexual id/field day four'        ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (42, 1,  'H2SD10-PWE', 'Replacement - Wales/english/paper/sexual id/field day ten'         ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (43, 1,  'H2S-PWE',    'Replacement - Wales/english/paper/sexual id/no field'              ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (44, 1,  'H2D4-PWE',   'Replacement - Wales/english/paper/without sexual id/field day four','SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (45, 1,  'H2D10-PWE',  'Replacement - Wales/english/paper/without sexual id/field day ten' ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (46, 1,  'H2-PWE',     'Replacement - Wales/english/paper/without sexual id/no field'      ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (47, 1,  'R-SMS4W',    'Replacement - Wales/SMS/field day four'                            ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (48, 1,  'R-SMS10W',   'Replacement - Wales/SMS/field day ten'                             ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (49, 1,  'R-SMSW',     'Replacement - Wales/SMS/No field'                                  ,'SYSTEM',NULL);

-- Action rules V9
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (85, 35, 48,  'H2WS_OR-100'  ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (86, 35, 14,  'HH_CV+3'      ,'', 3   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (87, 36, 48,  'H2WS_OR-100'  ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (88, 36, 14,  'HH_CV+9'      ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (89, 37, 48,  'H2WS_OR-100'  ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (90, 38, 49,  'H2W_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (91, 38, 14,  'HH_CV+3'      ,'', 3   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (92, 39, 49,  'H2W_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (93, 39, 14,  'HH_CV+9'      ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (94, 40, 49,  'H2W_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (95, 41, 50,  'H2S_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (96, 41, 14,  'HH_CV+3'      ,'', 3   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (97, 42, 50,  'H2S_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (98, 42, 14,  'HH_CV+9'      ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (99, 43, 50,  'H2S_OR-100'   ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (100, 44, 51, 'H2_OR-100'    ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (101, 44, 14, 'HH_CV+3'      ,'', 3   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (102, 45, 51, 'H2_OR-100'    ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (103, 45, 14, 'HH_CV+9'      ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (104, 46, 51, 'H2_OR-100'    ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (105, 47, 43, 'SMSB-100'     ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (106, 47, 14, 'HH_CV+3'      ,'', 3   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (107, 48, 43, 'SMSB-100'     ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (108, 48, 14, 'HH_CV+9'      ,'' , 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (109, 49, 43, 'SMSB-100'     ,'' , -100,3);




-- Action types V10
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (52, 'SHousingCreateVisit','Create Sheltered Housing Visit'           , 'Field'   ,true); 
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (53, 'ICLH1_2003'         ,'Print Initial Contact Letter (Hotel)'     , 'Printer', false);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (54, 'ICLHR1_2003'        ,'Print Initial Contact Letter (University)', 'Printer', false);

-- Action plans v10
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (50, 1,  'SHOUSING'      , 'Sheltered Housing - England/online/field day 10'    ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (51, 1,  'HOTEL'         , 'Hotel - England/online/no field'                    ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (52, 1,  'UNIVERSITY'    , 'University - England/online/no field'               ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (53, 1,  'SHOUSING-SMS'  , 'Replacement Sheltered Housing - England/SMS/Field'  ,'SYSTEM',NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (54, 1,  'UNIVERSITY-SMS', 'Replacement University - England/SMS/Field'         ,'SYSTEM',NULL);

-- Action rules V10
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (110, 50, 1,  'HH_ICL1-25'     ,'', -25 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (111, 50, 52, 'SH_CV+9'        ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (112, 51, 53, 'ICLH1_2003-25'  ,'', -25 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (113, 52, 54, 'ICLHR1_2003-25' ,'', -25 ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (114, 53, 40, 'SMSE-100'       ,'', -100,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (115, 53, 52, 'SH_CV+9'        ,'', 9   ,3);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (116, 54, 40, 'SMSE-100'       ,'', -100,3);



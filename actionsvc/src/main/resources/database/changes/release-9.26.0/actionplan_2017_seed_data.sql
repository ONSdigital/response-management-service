SET schema 'action';


TRUNCATE TABLE action.actionplan cascade;
TRUNCATE TABLE action.actiontype cascade;
TRUNCATE TABLE action.survey cascade;


INSERT INTO action.survey(surveyid,surveystartdate,surveyenddate,name) VALUES (1,'2017-04-09','2017-05-09','2017 Test');


--
-- Data for Name: actionplan; Type: TABLE DATA; Schema: action; Owner: postgres
--


INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (1, 1, 'C10331D4E', 'Component 1 - England/field day four/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (2, 1, 'C10331D4W', 'Component 1 - Wales/field day four/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (3, 1, 'C10331D10E', 'Component 1 - England/field day ten/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (4, 1, 'C10331D10W', 'Component 1 - England/field day ten/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (5, 1, 'C2SP331E', 'Component 2 - England/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (6, 1, 'C2SP331W', 'Component 2 - Wales/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (7, 1, 'C2EP331E', 'Component 2 - England/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (8, 1, 'C2EP331W', 'Component 2 - Wales/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (9, 1, 'C2331E', 'Component 2 - England/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (10, 1, 'C2331W', 'Component 2 - Wales/no field/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (11, 1, 'C2EO332E', 'Component 2 - England/no field/three reminders (final reminder HH questionnaire)', 'SYSTEM', |NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (12, 1, 'C2EO321E', 'Component 2 - England/no field/two reminders','SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (13, 1, 'C2EO322E', 'Component 2 - England/no field/two reminders (final reminder HH questionnaire)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (14, 1, 'C2EO300E', 'Component 2 - England/no field/no reminders (3w initial contact)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (15, 1, 'C2EO200E', 'Component 2 - England/no field/no reminders (2w initial contact)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (16, 1, 'C2EO331ADE', 'Component 2 - England/no field/three reminders (Assisted Digital)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (17, 1, 'INDPQ', 'Individual Paper Questionnaire/without sexual id', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (18, 1, 'INDPQWE', 'Individual Paper Questionnaire/without sexual id/Welsh in English', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (19, 1, 'INDPQWW', 'Individual Paper Questionnaire/without sexual id/Welsh in Welsh', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (20, 1, 'INDSPQ', 'Individual Paper Questionnaire/with sexual id', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (21, 1, 'INDPQSWE', 'Individual Paper Questionnaire/with sexual id/Welsh in English', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (22, 1, 'INDPQSWW', 'Individual Paper Questionnaire/with sexual id/Welsh in Welsh', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (23, 1, 'INDIACS', 'Individual Internet Access Code/SMS', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (24, 1, 'INDIACSB', 'Individual Internet Access Code/SMS/Bilingual', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (25, 1, 'INDIACE', 'Individual Internet Access Code/Email', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (26, 1, 'INDIACEB', 'Individual Internet Access Code/Email/Bilingual', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (27, 1, 'INDIACL', 'Individual Internet Access Code/Letter', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (28, 1, 'INDIACLB', 'Individual Internet Access Code/Letter/Bilingual', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (29, 1, 'REPPQ', 'Print Replacement English HH Paper Questionnaire', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (30, 1, 'REPPQWE', 'Print Replacement Welsh HH Paper Questionnaire (in English)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (31, 1, 'REPPQWW', 'Print Replacement Welsh HH Paper Questionnaire (in Welsh)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (32, 1, 'REPSPQ', 'Print Replacement English HH S Paper Questionnaire', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (33, 1, 'REPPQSWE', 'Print Replacement Welsh HH S Paper Questionnaire (in English)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (34, 1, 'REPPQSWW', 'Print Replacement Welsh HH S Paper Questionnaire (in welsh)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (35, 1, 'REPIACS', 'Send Replacement HH IAC by SMS', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (36, 1, 'REPIACSB', 'Send Replacement HH IAC by SMS (Bilingual)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (37, 1, 'REPIACE', 'Send Replacement HH IAC by Email', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (38, 1, 'REPIACEB', 'Send Replacement HH IAC by Email (Bilingual)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (39, 1, 'REPIACL', 'Send Replacement HH IAC by Letter', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (40, 1, 'REPIACLB', 'Send Replacement HH IAC by Letter (Bilingual)', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (41, 1, 'SHOUSING', 'Component 3 - Sheltered housing/field day four/three reminders', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (42, 1, 'HOTEL', 'Component 3 - Hotels/Initial Contact', 'SYSTEM', NULL);

INSERT INTO actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (43, 1, 'UNIVERSITY', 'Component 3 - University/Initial Contact', 'SYSTEM', NULL);

--
-- Data for Name: actiontype; Type: TABLE DATA; Schema: action; Owner: postgres
--

INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1, 'ICL1_2003', 'Print initial contact letter', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2, 'ICL2W_2003', 'Print initial contact letter (Wales)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3, 'ICLAD_2003', 'Print initial contact letter (assisted digital)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4, 'ICL1_2703', 'Print initial contact letter', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5, 'IRL1_0504', 'print reminder letter 1', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6, 'IRL2W_0504', 'print reminder letter 1 (Wales)', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7, '1RLAD_0504', 'Print reminder letter 1 (assisted digital)', 'Printer', false);
    
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8, '2RL1_1804', 'print reminder letter 2', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9, '2RL2W_1804', 'print reminder letter 2 (Wales)', 'Printer', false);
     
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10, '2RLAD_1804', 'Print reminder letter 2 (assisted digital)', 'Printer', false);
         
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11, '3RL1_2604', 'print reminder letter 3', 'Printer', false);

INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (12, '3RL2W_2604', 'print reminder letter 3 (Wales)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (13, '3RLAD_2604', 'Print reminder letter 3 (assisted digital)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (14, 'H1_2604Q4', 'Print household paper questionnaire (reminder 3)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (15, 'H1_2003', 'Print household paper questionnaire (english without sexual id)', 'Printer', false);
     
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (16, 'H2_2003', 'Print household paper questionnaire (welsh in english without sexual id)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (17, 'H1S_2003', 'Print household paper questionnaire (english with sexual id)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (18, 'H2S_2003', 'Print household paper questionnaire (welsh in english with sexual id)', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (19, 'H2W_2003', 'Print household paper questionnaire (welsh in welsh without sexual id)', 'Printer', false);
   
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (20, 'H2WS_2003', 'Print household paper questionnaire (welsh in welsh with sexual id)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (21, 'HouseholdCreateVisit', 'Household create visit', 'Field', true);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (22, 'GeneralComplaintEscalation', 'General complaint escalation', 'CensusSupport', false);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (23, 'GeneralEnquiryEscalation', 'General enquiry escalation', 'CensusSupport', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (24, 'FieldComplaintEscalation', 'Field complaint escalation', 'FieldSupport', false);

INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (25, 'FieldEmergencyEscalation', 'Field emergency escalation', 'FieldSupport', false);
 
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (26, 'I1_OR', 'Print individual paper questionnaire (english without sexual id)', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (27, 'I2_OR', 'Print individual paper questionnaire (welsh in english without sexual id)', 'Printer',false);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (28, 'I2W_OR', 'Print individual paper questionnaire (welsh in welsh without sexual id)', 'Printer', false);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (29, 'I1S_OR', 'Print individual paper questionnaire (english with sexual id)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (30, 'I2S_OR', 'Print individual paper questionnaire (welsh in english with sexual id)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (31, 'I2WS_OR', 'Print individual paper questionnaire (welsh in welsh with sexual id)', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (32, 'TBC', 'Send Individual IAC by SMS', 'Notify', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (33, 'TBC', 'Send Individual IAC by SMS (Bilingual)', 'Notify', false);
     
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (34, 'TBC', 'Send Individual IAC by Email', 'Notify', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (35, 'TBC', 'Send Individual IAC by Email (Bilingual)', 'Notify', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (36, 'TBC', 'Send Individual IAC by Letter', 'Notify', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (37, 'TBC', 'Send Individual IAC by Letter (Bilingual)', 'Notify', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (38, 'H1_OR', 'OR Print household paper questionnaire (english without sexual id)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (39, 'H2_OR', 'OR Print household paper questionnaire (welsh in english without sexual id)', 'Printer', false);
     
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (40, 'H2W_OR', 'OR Print household paper questionnaire (english with sexual id)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (41, 'H1S_OR', 'OR Print household paper questionnaire (welsh in english with sexual id)', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (42, 'H2S_OR', 'OR Print household paper questionnaire (welsh in welsh without sexual id)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (43, 'H2WS_OR', 'OR Print household paper questionnaire (welsh in welsh with sexual id)', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (44, 'TBC', 'Send HH IAC by SMS', 'Notify', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (45, 'TBC', 'Send HH IAC by SMS (Bilingual)', 'Notify', false);
    
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (46, 'TBC', 'Send HH IAC by Email', 'Notify', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (47, 'TBC', 'Send HH IAC by Email (Bilingual)', 'Notify', false);
     
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (48, 'TBC', 'Send HH IAC by Letter', 'Notify', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (49, 'TBC', 'Send HH IAC by Letter (Bilingual)', 'Notify', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (50, 'TBC', 'Print translation booklet - Welsh', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (51, 'QGPOL_OR', 'Print translation booklet - Polish', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (52, 'QGCAN_OR', 'Print translation booklet - Cantonese', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (53, 'QGSPA_OR', 'Print translation booklet - Spanis', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (54, 'QGARA_OR', 'Print translation booklet - Arabic', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (55, 'QGSOM_OR', 'Print translation booklet - Somali', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (56, 'QGMAN_OR', 'Print translation booklet - Mandarin', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (57, 'QGBEN_OR', 'Print translation booklet - Bengali', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (58, 'QGPOR_OR', 'Print translation booklet - Portuguese', 'Printer', false);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (59, 'QGGUR_OR', 'Print translation booklet - Punjabi - (Gurmukhi)', 'Printer', false);
  
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (60, 'QGSHA_OR', 'Print translation booklet - Punjabi - (Shahmuki)', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (61, 'QGTUR_OR', 'Print translation booklet - Turkish', 'Printer', false);
      
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (62, 'QGLIT_OR', 'Print translation booklet - Lithuanian', 'Printer', false);
        
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (63, 'QGURD_OR', 'Print translation booklet - Urdu', 'Printer', false);
   
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (64, 'QGGUJ_OR', 'Print translation booklet - Gujarati', 'Printer', false);
       
INSERT INTO actiontype (actiontypeid, name, description, handler, cancancel) VALUES (65, 'TBC', 'Print Initial Contact - Hotel', 'Printer', false);





--
-- Data for Name: actionrule; Type: TABLE DATA; Schema: action; Owner: postgres
--




INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (1,  1, 1,  'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (2,  1, 21, 'HH_CV_4', 'Create Visit SD+4days', 4, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (3,  1, 5,  'HH_IRL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (4,  1, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (5,  1, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (6,  2, 2,  'HH_ICL2W_2003', 'Print Initial Contact Letter Wales SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (7,  2, 21, 'HH_CV_4', 'Create Visit SD+4days', 4, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (8,  2, 6,  'HH_IRL2W_0504', 'Print Reminder Letter 1 Wales SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (9,  2, 9,  'HH_2RL2W_1804', 'Print Reminder Letter 2 Wales SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (10, 2, 12, 'HH_3RL2W_2604', 'Print Reminder Letter 3 Wales SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (11, 3, 1,  'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (12, 3, 21, 'HH_CV_10', 'Create Visit SD+10days', 10, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (13, 3, 5,  'HH_IRL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (14, 3, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (15, 3, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (16, 4, 2,  'HH_ICL2W_2003', 'Print Initial Contact Letter Wales SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (17, 4, 21, 'HH_CV_10', 'Create Visit SD+10days', 10, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (18, 4, 6,  'HH_IRL2W_0504', 'Print Reminder Letter 1 Wales SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (19, 4, 9,  'HH_2RL2W_1804', 'Print Reminder Letter 2 Wales SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (20, 4, 12, 'HH_3RL2W_2604', 'Print Reminder Letter 3 Wales SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (21, 5, 17, 'HH_HIS_2003', 'Print household paper questionnaire (english with sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (22, 5, 5,  'HH_1RL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (23, 5, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (24, 5, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (25, 6, 18, 'HH_H2S_2003', 'Print household paper questionnaire (welsh in english with sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (26, 6, 20, 'HH_ H2WS_2003', 'Print household paper questionnaire (welsh in english with sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (27, 6, 6,  'HH_IRL2W_0504', 'Print Reminder Letter 1 Wales SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (28, 6, 9,  'HH_2RL2W_1804', 'Print Reminder Letter 2 Wales SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (29, 6, 12, 'HH_3RL2W_2604', 'Print Reminder Letter 3 Wales SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (30, 7, 15, 'HH_HI_2003', 'Print household paper questionnaire (english without sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (31, 7, 5,  'HH_1RL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (32, 7, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (33, 7, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (34, 8, 16, 'HH_H2_2003', 'Print household paper questionnaire (welsh in english without sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (35, 8, 19, 'H2W_2003', 'Print household paper questionnaire (welsh in welsh without sexual id) SD-21', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (36, 8, 5,  'HH_1RL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (37, 8, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (38, 8, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (39, 9, 1,  'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (40, 9, 5,  'HH_IRL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (41, 9, 8,  'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (42, 9, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (43, 10, 2, 'HH_ICL2W_2003', 'Print Initial Contact Letter Wales SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (44, 10, 6, 'HH_IRL2W_0504', 'Print Reminder Letter 1 Wales SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (45, 10, 9,  'HH_2RL2W_1804', 'Print Reminder Letter 2 Wales SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (46, 10, 12, 'HH_3RL2W_2604', 'Print Reminder Letter 3 Wales SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (47, 11, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (48, 11, 5, 'HH_IRL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (49, 11, 8, 'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (50, 11, 14, 'HH_H1_2604Q4', 'Print Reminder Paper Questionnaire SD+21', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (51, 12, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (52, 12, 8, 'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (53, 12, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (54, 13, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (55, 13, 8, 'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (56, 13, 14, 'HH_H1_2604Q4', 'Print Reminder Paper Questionnaire SD+21', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (57, 14, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (58, 15, 4, 'HH_ICL1_2703', 'Print Initial Contact Letter SD-14days', -14, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (59, 16, 3, 'HH_ICLAD_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (60, 16, 7, 'HH_1RLAD_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (61, 16, 10, 'HH_2RLAD_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (62, 16, 13, 'HH_3RLAD_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (63, 17, 26, 'IND_I1_OR', 'Print English Individual Paper Questionnaire', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (64, 18, 27, 'IND_I2_OR', 'Print Welsh Individual Paper Questionnaire (in English)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (65, 19, 28, 'IND_I2W_OR', 'Print Welsh Individual Paper Questionnaire (in Welsh)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (66, 20, 29, 'IND_I1S_OR', 'Print English Individual S Paper Questionnaire', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (67, 21, 30, 'IND_I2S_OR', 'Print Welsh Individual S Paper Questionnaire (in English)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (68, 22, 31, 'IND_I2WS_OR', 'Print Welsh Individual S Paper Questionnaire (in welsh)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (69, 23, 32, 'IND_TBC', 'Send Individual IAC by SMS', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (70, 24, 33, 'IND_TBC', 'Send Individual IAC by SMS (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (71, 25, 34, 'IND_TBC', 'Send Individual IAC by Email', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (72, 26, 35, 'IND_TBC', 'Send Individual IAC by Email (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (73, 27, 36, 'IND_TBC', 'Send Individual IAC by Letter', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (74, 28, 37, 'IND_TBC', 'Send Individual IAC by Letter (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (75, 29, 38, 'HH_H1_OR', 'Print English HH Paper Questionnaire', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (76, 30, 39, 'HH_H2_OR', 'Print Welsh HH Paper Questionnaire (in English)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (77, 31, 40, 'HH_H2W_OR', 'Print Welsh HH Paper Questionnaire (in Welsh)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (78, 32, 41, 'HH_H1S_OR', 'Print English HH S Paper Questionnaire', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (79, 33, 42, 'HH_H2S_OR', 'Print Welsh HH S Paper Questionnaire (in English)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (80, 34, 43, 'HH_H2WS_OR', 'Print Welsh HH S Paper Questionnaire (in welsh)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (81, 35, 44, 'HH_TBC', 'Send HH IAC by SMS', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (82, 36, 45, 'HH_TBC', 'Send HH IAC by SMS (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (83, 37, 46, 'HH_TBC', 'Send HH IAC by Email', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (84, 38, 47, 'HH_TBC', 'Send HH IAC by Email (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (85, 39, 48, 'HH_TBC', 'Send HH IAC by Letter', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (86, 40, 49, 'HH_TBC', 'Send HH IAC by Letter (Bilingual)', 0, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (87, 41, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (88, 41, 21, 'HH_CV_10', 'Create Visit SD+10days', 10, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (89, 41, 5, 'HH_IRL1_0504', 'Print Reminder Letter 1 SD-7days', -7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (90, 41, 8, 'HH_2RL1_1804', 'Print Reminder Letter 2 SD+7days', 7, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (91, 41, 11, 'HH_3RL1_2604', 'Print Reminder Letter 3 SD+21 days', 21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (92, 42, 65, 'HGH_TBC', 'Print Initial Contact Letter SD-21days', -21, 3);

INSERT INTO actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset, priority) VALUES (93, 43, 1, 'HH_ICL1_2003', 'Print Initial Contact Letter SD-21days', -21, 3);





TRUNCATE action.survey CASCADE;
TRUNCATE action.actionplan CASCADE;
TRUNCATE action.actionrule CASCADE;
TRUNCATE action.actiontype CASCADE;

INSERT INTO action.survey (surveyid, surveydate, name) VALUES (1,'2016-07-01 00:00','2016 Test');

INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1,'HouseholdInitialContact','Household initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2,'HouseholdUploadIAC','Household upload IAC','HHSurvey','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3,'HouseholdCreateVisit','Household create visit','Field','y');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4,'CareHomeInitialContact','Care home initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5,'CareHomeCreateVisit','Care home create visit','Field','y');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6,'HotelGuestHouseInitialContact','Hotel and guest house initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7,'HotelUploadIAC','Hotel upload IAC','HotelSurvey','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8,'HotelGuestHouseCreateVisit','Hotel and guest house create visit','Field','y');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9,'GeneralEscalation','General escalation','CensusSupport','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10,'SurveyEscalation','Survey escalation','CensusSupport','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11,'ComplaintEscalation','Complaint escalation','CensusSupport','n');

INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (1,1,'HH'   ,'Household Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (2,1,'CH'   ,'Care Home Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (3,1,'HGH'  ,'Hotel and Guest House Action Plan','SYSTEM', current_timestamp, NULL);



INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (1,1,1,'HH_IC-39','Create Household Print List for: All Cases',-39);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (2,1,2,'HH_IAC-42','Upload Household IAC codes to online questionnaire for: All Cases',-42);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (3,1,3,'HH_CV+7','Create Household Visit Job for: All Cases where State = INIT',+7);

INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (4,2,4,'CH_IC-39','Create Care Home Print List for: All Cases',-39);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (5,2,5,'CH_Cv+7','Create Care Home Visit Job for: All Cases where State = INIT',+7);

INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (6,3,6,'HGH_IC-39','Create Hotel and Guest House Print List for: All Cases',-39);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (7,3,7,'HGH_IAC-42','Upload Hotel and Guest House IAC codes to online questionnaire for: All Cases',-42);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (8,3,8,'HGH_CV+7','Create Hotel and Guest House Visit Job for: All Cases where State = INIT or State = RESPONDED',+7);

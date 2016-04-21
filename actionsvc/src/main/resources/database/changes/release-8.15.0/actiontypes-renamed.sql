
TRUNCATE action.actiontype CASCADE;


INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (1,'HouseholdInitialContact','Household initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (2,'HouseholdUploadIAC','Household upload IAC','HHSurvey','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (3,'HousholdCreateVisit','Household create visit','Field','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (4,'CareHomeInitialContact','Care home initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (5,'CareHomeCreateVisit','Care home create visit','Field','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (6,'HotelGuestHouseInitialContact','Hotel and guest house initial contact','Printer','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (7,'HotelUploadIAC','Hotel upload IAC','HotelSurvey','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (8,'HotelGuestHouseCreateVisit','Hotel and guest house create visit','Field','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (9,'GeneralEscalation','General escalation','CensusSupport','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (10,'SurveyEscalation','Survey escalation','CensusSupport','n');
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancancel) VALUES (11,'ComplaintEscalation','Complaint escalation','CensusSupport','n');


INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (1,1,1,'HH_IC-7','Household initial contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (2,1,2,'HH_IAC-7','Household upload IAC',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (3,1,3,'HH_CV+18','Household create visit',+18);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (4,2,4,'CH_IC-7','Care home initial contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (5,2,5,'CH_Cv+18','Care home create visit',+18);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (6,3,6,'HGH_IC-7','Hotel and guest house initial contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (7,3,7,'HGH_IAC-7','Hotel upload IAC',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (8,3,8,'HGH_CV+1','Hotel and guest house create visit',+1);
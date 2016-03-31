TRUNCATE action.survey CASCADE;
TRUNCATE action.actiontype CASCADE;
TRUNCATE action.actionplan CASCADE;
TRUNCATE action.actionrule CASCADE;
TRUNCATE action.actionstate CASCADE;
TRUNCATE action.actionplanjobstate CASCADE;


INSERT INTO action.survey (surveyid, surveydate, name) VALUES (1,'2016-06-26 00:00','2016 Test');


INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (1,'HH_IC','Household Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (2,'HH_IAC_LOAD','Household upload IAC','HHSurvey',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (3,'HH_CV','Household Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (4,'CH_IC','Care Home Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (5,'CH_CV','Care Home Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (6,'HGH_IC','Hotel and Guest House Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (7,'HGH_IAC_LOAD','Hotel upload IAC','HotelSurvey',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (8,'HGH_CV','Hotel and Guest House Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (9,'ESC_GENERAL','Escalated General','CensusSupport',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (10,'ESC_SURVEY','Escalated Survey','CensusSupport',FALSE);
INSERT INTO action.actiontype (actiontypeid, name, description, handler, cancanel) VALUES (11,'ESC_COMPLAINT','Escalated Complaint','CensusSupport',FALSE);


INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (1,1,'HH'   ,'Household Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (2,1,'CH'   ,'Care Home Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, createddatetime, lastgoodrundatetime) VALUES (3,1,'HGH'  ,'Hotel and Guest House Action Plan','SYSTEM', current_timestamp, NULL);


INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (1,1,1,'HH_IC-7','Household Initial Contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (2,1,2,'HH_IAC-7','Household IAC Load',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (3,1,3,'HH_CV+18','Household Create Visit',+18);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (4,2,4,'CH_IC-7','Care Home Initial Contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (5,2,5,'CH_Cv+18','Care Home Create Visit',+18);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (6,3,6,'HGH_IC-7','Hotel and Guest House Initial Contact',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (7,3,7,'HGH_IAC-7','Hotel and Guest House IAC Load',-7);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypeid, name, description, surveydatedaysoffset) VALUES (8,3,8,'HGH_CV+1','Hotel and Guest House Create Visit',+1);


INSERT INTO action.actionstate (state) VALUES ('SUBMITTED');
INSERT INTO action.actionstate (state) VALUES ('PENDING');
INSERT INTO action.actionstate (state) VALUES ('ACTIVE');
INSERT INTO action.actionstate (state) VALUES ('COMPLETED');
INSERT INTO action.actionstate (state) VALUES ('CANCELSUBMITTED');
INSERT INTO action.actionstate (state) VALUES ('CANCELLED');
INSERT INTO action.actionstate (state) VALUES ('FAILED');


INSERT INTO action.actionplanjobstate (state) VALUES ('SUBMITTED');
INSERT INTO action.actionplanjobstate (state) VALUES ('STARTED');
INSERT INTO action.actionplanjobstate (state) VALUES ('COMPLETED');
INSERT INTO action.actionplanjobstate (state) VALUES ('FAILED');
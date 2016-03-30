INSERT INTO action.survey (surveyid, surveydate, name) VALUES (2,'2016-06-26 00:00','2016 Test');


INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HH_IC','Household Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HH_IAC_LOAD','Household upload IAC','HHSurvey',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HH_CV','Household Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('CH_IC','Care Home Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('CH_CV','Care Home Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HGH_IC','Hotel and Guest House Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HGH_IAC_LOAD','Hotel upload IAC','HotelSurvey',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('HGH_CV','Hotel and Guest House Create Visit','Field',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('ESC_GENERAL','Escalated General','CensusSupport',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('ESC_SURVEY','Escalated Survey','CensusSupport',FALSE);
INSERT INTO action.actiontype (actiontypename, description, handler, cancanel) VALUES ('ESC_COMPLAINT','Escalated Complaint','CensusSupport',FALSE);

UPDATE action.actionrule SET actiontypename = 'HH_IC', name = 'HH_IC-7', description = 'Household Initial Contact' WHERE actionruleid = 1;
UPDATE action.actionrule SET actiontypename = 'HH_IAC_LOAD', name = 'HH_IAC-7', description = 'Household IAC Load', surveydatedaysoffset = -7 WHERE actionruleid = 2;
UPDATE action.actionrule SET actionplanid = 1, actiontypename = 'HH_CV', name = 'HH_CV+18', description = 'Household Create Visit', surveydatedaysoffset = 18 WHERE actionruleid = 3;
UPDATE action.actionrule SET actiontypename = 'CH_IC', name = 'CH_IC-7', description = 'Care Home Initial Contact', surveydatedaysoffset = -7 WHERE actionruleid = 4;
UPDATE action.actionrule SET actionplanid = 2, actiontypename = 'CH_CV', name = 'CH_CV+18', description = 'Care Home Create Visit', surveydatedaysoffset = 18 WHERE actionruleid = 5;
UPDATE action.actionrule SET actiontypename = 'HGH_IC', name = 'HGH_IC-7', description = 'Hotel and Guest House Initial Contact', surveydatedaysoffset = -7 WHERE actionruleid = 6;
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypename, name, description, surveydatedaysoffset, priority) VALUES (7,3, 'HGH_IAC_LOAD','HGH_IAC-7','Hotel and Guest House IAC Load',-7, 0);
INSERT INTO action.actionrule (actionruleid, actionplanid, actiontypename, name, description, surveydatedaysoffset, priority) VALUES (8,3, 'HGH_CV','HGH_CV+1','Hotel and Guest House Create Visit',+1, 0);

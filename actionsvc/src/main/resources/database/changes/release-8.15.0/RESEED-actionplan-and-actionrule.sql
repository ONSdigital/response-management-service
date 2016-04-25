
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

set schema 'action';

INSERT INTO action.survey VALUES (1,'2016-06-26 00:00','2016 Test');


INSERT INTO action.actiontype VALUES (1,'name','Household Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES (2,'name','Household Create Visit','Field visit',FALSE);
INSERT INTO action.actiontype VALUES (3,'name','Care Home Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES (4,'name','Care Home Create Visit','Field visit',FALSE);
INSERT INTO action.actiontype VALUES (5,'name','Hotel and Guest House Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES (6,'name','Hotel and Guest House Create Visit','Field visit',FALSE);


INSERT INTO action.actionplan VALUES (1,1,'HH','Household Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan VALUES (2,1,'CH','Care Home Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan VALUES (3,1,'HGH','Hotel and Guest House Action Plan','SYSTEM', current_timestamp, NULL);


INSERT INTO action.actionrule VALUES (1,1,1,'name','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (2,1,2,'name','Create Visit',+18,0);
INSERT INTO action.actionrule VALUES (3,2,3,'name','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (4,2,4,'name','Create Visit',+18,0);
INSERT INTO action.actionrule VALUES (5,3,5,'name','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (6,3,6,'name','Create Visit',+1,0);
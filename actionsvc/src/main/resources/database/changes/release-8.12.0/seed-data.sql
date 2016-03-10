set schema 'action';

INSERT INTO action.survey VALUES (1,'2016-06-26 00:00','2016 Test');


INSERT INTO action.actiontype VALUES ('actiontypename1','Household Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES ('actiontypename2','Household Create Visit','Field visit',FALSE);
INSERT INTO action.actiontype VALUES ('actiontypename3','Care Home Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES ('actiontypename4','Care Home Create Visit','Field visit',FALSE);
INSERT INTO action.actiontype VALUES ('actiontypename5','Hotel and Guest House Initial Contact','Printer',FALSE);
INSERT INTO action.actiontype VALUES ('actiontypename6','Hotel and Guest House Create Visit','Field visit',FALSE);


INSERT INTO action.actionplan VALUES (1,1,'HH','Household Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan VALUES (2,1,'CH','Care Home Action Plan','SYSTEM', current_timestamp, NULL);
INSERT INTO action.actionplan VALUES (3,1,'HGH','Hotel and Guest House Action Plan','SYSTEM', current_timestamp, NULL);


INSERT INTO action.actionrule VALUES (1,1,'actiontypename1','actionrulename','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (2,1,'actiontypename2','actionrulename','Create Visit',+18,0);
INSERT INTO action.actionrule VALUES (3,2,'actiontypename3','actionrulename','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (4,2,'actiontypename4','actionrulename','Create Visit',+18,0);
INSERT INTO action.actionrule VALUES (5,3,'actiontypename5','actionrulename','Initial Contact',-7,0);
INSERT INTO action.actionrule VALUES (6,3,'actiontypename6','actionrulename','Create Visit',+1,0);


INSERT INTO action.actionstate VALUES ('SUBMITTED');
INSERT INTO action.actionstate VALUES ('PENDING');
INSERT INTO action.actionstate VALUES ('ACTIVE');
INSERT INTO action.actionstate VALUES ('COMPLETED');
INSERT INTO action.actionstate VALUES ('CANCELSUBMITTED');
INSERT INTO action.actionstate VALUES ('CANCELLED');
INSERT INTO action.actionstate VALUES ('FAILED');


INSERT INTO action.actionplanjobstate VALUES ('SUBMITTED');
INSERT INTO action.actionplanjobstate VALUES ('STARTED');
INSERT INTO action.actionplanjobstate VALUES ('COMPLETED');
INSERT INTO action.actionplanjobstate VALUES ('FAILED');
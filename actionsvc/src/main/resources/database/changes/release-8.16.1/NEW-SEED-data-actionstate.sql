
TRUNCATE action.actionstate CASCADE;


INSERT INTO action.actionstate (state) VALUES ('SUBMITTED');
INSERT INTO action.actionstate (state) VALUES ('PENDING');
INSERT INTO action.actionstate (state) VALUES ('ACTIVE');
INSERT INTO action.actionstate (state) VALUES ('COMPLETED');
INSERT INTO action.actionstate (state) VALUES ('CANCEL_SUBMITTED');
INSERT INTO action.actionstate (state) VALUES ('CANCELLED');
INSERT INTO action.actionstate (state) VALUES ('CANCEL_PENDING');

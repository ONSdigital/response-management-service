TRUNCATE TABLE action.actionplan CASCADE;
ALTER TABLE action.actionplan ALTER COLUMN actionplanid DROP DEFAULT; 

DROP SEQUENCE action.actionplanidseq CASCADE;

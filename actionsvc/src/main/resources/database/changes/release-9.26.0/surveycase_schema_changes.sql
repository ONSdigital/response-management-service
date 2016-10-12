TRUNCATE TABLE action.survey CASCADE;
ALTER TABLE action.survey ADD COLUMN surveyenddate timestamp with time zone NOT NULL;
ALTER TABLE action.survey RENAME COLUMN surveydate TO surveystartdate;

TRUNCATE TABLE action.case; 
ALTER TABLE action.case ADD COLUMN actionplanstartdate timestamp with time zone NOT NULL;

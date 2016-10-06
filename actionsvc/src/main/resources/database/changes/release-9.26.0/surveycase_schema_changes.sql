TRUNCATE TABLE action.survey CASCADE;
ALTER TABLE action.survey ADD COLUMN surveyenddate timestamp with time zone NOT NULL;

TRUNCATE TABLE action.case; 
ALTER TABLE action.case ADD COLUMN actionplanstartdate timestamp with time zone NOT NULL;

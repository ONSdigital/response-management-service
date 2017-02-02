-- Create a new survey for DTM test

INSERT INTO action.survey (surveyid, surveystartdate, name, surveyenddate) VALUES (2, '2017-04-09 00:00:00+01', 'DTM TEST', '2017-05-09 00:00:00+01');


-- Insert an actionplan with an id = 0 and no rules

INSERT INTO action.actionplan (actionplanid, surveyid, name, description, createdby, lastrundatetime) VALUES (0, 2, 'DTM TEST', 'DTM Test Action plan - no associated rules', 'SYSTEM', NULL);


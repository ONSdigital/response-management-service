
-- Update action rules name and description
UPDATE action.actionrule
SET name = replace(name,'-43','-100')
,   description = replace(description,'-43','-100')
WHERE surveydatedaysoffset = -43;

-- Update offset
UPDATE action.actionrule
SET surveydatedaysoffset  = -100
WHERE surveydatedaysoffset = -43;


-- Update surveyenddate
UPDATE action.survey
SET surveyenddate = '2017-05-19 00:00:00+01'
WHERE surveyid = 1 AND name = '2017 Test'; 



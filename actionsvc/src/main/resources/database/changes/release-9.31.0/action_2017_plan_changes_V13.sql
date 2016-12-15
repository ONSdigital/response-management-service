-- Changes required for Version 13 of Action plan set up data
-- Updated the Survey Off Set Date for the Action Rules
-- Initial Print Date moved to 24/02
-- All print dates to run day before delivery date to printer
-- All visit action to be created 4 days before delivery date to DRS

-- Update action rules offset for initial contact from -25 to -45
UPDATE action.actionrule
SET surveydatedaysoffset = -45
,   name = replace(name,'-25','-45')
,   description = replace(description,'-25','-45')
WHERE surveydatedaysoffset = -25 
AND UPPER(description) like 'PRINT INITIAL CONTACT%LETTER%';


-- Update action rules offset for initial contact from -18 to -38
UPDATE action.actionrule
SET surveydatedaysoffset = -38
,   name = replace(name,'-18','-38')
,   description = replace(description,'-18','-38')
WHERE surveydatedaysoffset = -18 
AND UPPER(description) like 'PRINT INITIAL CONTACT%LETTER%';


-- Update visit date from +9 to +4
UPDATE action.actionrule
SET surveydatedaysoffset = 4
,   name = replace(name,'+9','+4')
,   description = replace(description,'+9','+4')
WHERE surveydatedaysoffset = 9 
AND UPPER(description) like 'CREATE%VISIT%';


-- Update visit date from +3 to +1
UPDATE action.actionrule
SET surveydatedaysoffset = 1
,   name = replace(name,'+3','+1')
,   description = replace(description,'+3','+1')
WHERE surveydatedaysoffset = 3 
AND UPPER(description) like 'CREATE%VISIT%';



-- Update reminder letter 1 date from -4 to -5
UPDATE action.actionrule
SET surveydatedaysoffset = -5
,   name = replace(name,'-4','-5')
,   description = replace(description,'-4','-5')
WHERE surveydatedaysoffset = -4 
AND UPPER(description) like '%REMINDER LETTER 1%';



-- Update reminder letter 2 date from +9 to +8
UPDATE action.actionrule
SET surveydatedaysoffset = 8
,   name = replace(name,'+9','+8')
,   description = replace(description,'+9','+8')
WHERE surveydatedaysoffset = 9 
AND UPPER(description) like '%REMINDER LETTER 2%';


-- Update reminder letter 3 date from +17 to +16
UPDATE action.actionrule
SET surveydatedaysoffset = 16
,   name = replace(name,'+17','+16')
,   description = replace(description,'+17','+16')
WHERE surveydatedaysoffset = 17 
AND UPPER(description) like '%REMINDER LETTER 3%';


-- Update print household paper questionnaire
UPDATE action.actionrule
SET surveydatedaysoffset = -45
,   name = replace(name,'-25','-45')
,   description = replace(description,'-25','-45')
WHERE surveydatedaysoffset = -25 
AND UPPER(description) like 'PRINT HOUSEHOLD PAPER%';

-- Update print paper questionnaire
UPDATE action.actionrule
SET surveydatedaysoffset = 16
,   name = replace(name,'+17','+16')
,   description = replace(description,'+17','+16')
WHERE surveydatedaysoffset = 17 
AND UPPER(description) like 'PRINT HOUSEHOLD PAPER%';




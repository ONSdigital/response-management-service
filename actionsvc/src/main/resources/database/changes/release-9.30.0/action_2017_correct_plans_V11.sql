-- Change required for Version 11 of Action plan set up data

-- change description to no field
UPDATE action.actionplan
SET description = 'Replacement University - England/SMS/no field'
WHERE actionplanid = 54;

-- change description to specify when field takes place
UPDATE action.actionplan
SET description = 'Replacement Sheltered Housing - England/SMS/field day 10'
WHERE actionplanid = 53;


-- Delete duplicate plan actionplanid 33
DELETE FROM action.actionrule
WHERE actionplanid = 33;

-- Delete duplicate plan actionplanid 33
DELETE FROM action.actionplan
WHERE actionplanid = 33;


-- Update actionplan name 
UPDATE action.actionplan
SET name = 'I-SMSE'
WHERE actionplanid = 19;

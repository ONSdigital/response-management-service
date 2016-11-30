-- Version 6.0 of Excel spreadsheet from business 4th November

set schema 'action';

-- Drop constraints
ALTER TABLE action.case DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionruleid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actiontypeid_fkey;
ALTER TABLE action.action DROP CONSTRAINT actionplanid_fkey;
ALTER TABLE action.actionplanjob DROP CONSTRAINT actionplanid_fkey;

-- Removed action plans for individual IAC by email and letter and associated rules and types
DELETE FROM action.actionrule
WHERE actionplanid IN(20,21);

DELETE FROM action.actiontype
WHERE actiontypeid IN(41,42);

DELETE FROM action.actionplan
WHERE actionplanid IN(20,21);


-- Reinstate the foreign keys constraints
ALTER TABLE action.case ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);
ALTER TABLE action.action ADD CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid) REFERENCES action.actiontype (actiontypeid);
ALTER TABLE action.action ADD CONSTRAINT actionruleid_fkey FOREIGN KEY (actionruleid) REFERENCES action.actionrule (actionruleid);
ALTER TABLE action.actionplanjob ADD CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES action.actionplan (actionplanid);


-- Typo - Spanish
UPDATE action.actiontype
SET description = 'Print translation booklet - Spanish'
WHERE actiontypeid = 26;


-- Change action date for sending the IAC or paper form to -100 days from Survey Date
-- and associated descriptions and names
UPDATE action.actionrule
SET surveydatedaysoffset = -100
WHERE actionruleid IN(61,62,63);

UPDATE action.actionrule
SET description = 'Print Individual Questionnaire (SD-100)'
   ,name = 'I1S_OR-100'
WHERE actionruleid = 61;

UPDATE action.actionrule
SET description = 'Print Individual Questionnaire (SD-100)'
   ,name = 'I1_OR-100'
WHERE actionruleid = 62;

UPDATE action.actionrule
SET description = 'Send individual IAC via SMS (SD-100)'
   ,name = 'ISMS-100'
WHERE actionruleid = 63;

set schema 'action';
ALTER TABLE action.actionplan ALTER COLUMN createdby TYPE varchar(50);
ALTER TABLE action.actionplanjob ALTER COLUMN createdby TYPE varchar(50);
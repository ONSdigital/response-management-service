--Remove null constraint from actionplan column, translation requests do not run as part of an actionplan

set schema 'actionexporter';

ALTER TABLE actionrequest ALTER COLUMN actionplan DROP NOT NULL;

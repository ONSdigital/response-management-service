set schema 'action';

ALTER TABLE action.actiontype RENAME COLUMN provider TO handler;

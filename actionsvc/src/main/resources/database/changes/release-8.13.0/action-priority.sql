set schema 'action';

ALTER TABLE action.action ALTER COLUMN priority TYPE integer USING (priority::integer);
ALTER TABLE action.action ALTER priority SET DEFAULT 3;
COMMENT ON COLUMN action.action.priority IS '1 = highest, 5 = lowest';

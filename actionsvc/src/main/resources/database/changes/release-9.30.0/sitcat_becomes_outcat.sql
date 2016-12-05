set schema 'action';

ALTER TABLE action.action DROP CONSTRAINT situation_fkey;
DROP TABLE action.situationcategory;

CREATE TABLE action.outcomecategory
( handler     character varying(100),
  actionoutcome character varying(40),
  eventcategory character varying(40),
  CONSTRAINT outcomecategory_pkey PRIMARY KEY (handler, actionoutcome))
WITH (
  OIDS=FALSE
);


INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Field','REQUEST_COMPLETED','ACTION_COMPLETED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Field','REQUEST_COMPLETED_DEACTIVATE', 'ACTION_COMPLETED_DEACTIVATED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Field','REQUEST_COMPLETED_DISABLE', 'ACTION_COMPLETED_DISABLED');

INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Printer','REQUEST_COMPLETED', 'ACTION_COMPLETED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Printer','REQUEST_COMPLETED_DEACTIVATE', 'ACTION_COMPLETED_DEACTIVATED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Printer','REQUEST_COMPLETED_DISABLE', 'ACTION_COMPLETED_DISABLED');

INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Notify','REQUEST_COMPLETED', 'ACTION_COMPLETED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Notify','REQUEST_COMPLETED_DEACTIVATE', 'ACTION_COMPLETED_DEACTIVATED');
INSERT INTO action.outcomecategory (handler, actionoutcome, eventcategory) VALUES ('Notify','REQUEST_COMPLETED_DISABLE', 'ACTION_COMPLETED_DISABLED');
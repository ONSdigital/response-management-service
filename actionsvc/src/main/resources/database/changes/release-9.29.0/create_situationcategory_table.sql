CREATE TABLE action.situationcategory
( situation     character varying(100),
  eventcategory character varying(40),
  CONSTRAINT situation_pkey PRIMARY KEY (situation))
WITH (
  OIDS=FALSE
);

ALTER TABLE action.action ADD CONSTRAINT situation_fkey FOREIGN KEY (situation) REFERENCES action.situationcategory (situation)

-- Table: action.case
DROP TABLE action.case;

CREATE TABLE action.case
( actionplanid integer NOT NULL,
  caseid bigint NOT NULL,
  CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid)
      REFERENCES action.actionplan (actionplanid)
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.case
  OWNER TO postgres;



ALTER TABLE action.actionplan RENAME COLUMN lastgoodrundatetime TO lastrundatetime;
ALTER TABLE action.actionplan DROP COLUMN createddatetime;
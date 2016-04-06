-- Table: action.actiontype

DROP TABLE action.actiontype CASCADE;

CREATE TABLE action.actiontype
( actiontypeid integer NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250) NOT NULL,
  handler character varying(100),
  cancancel boolean NOT NULL,
  CONSTRAINT actiontypeid_pkey PRIMARY KEY (actiontypeid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.actiontype
  OWNER TO postgres;




-- Table: action.actionrule
 DROP TABLE action.actionrule CASCADE;

CREATE TABLE action.actionrule
(
  actionruleid integer NOT NULL,
  actionplanid integer NOT NULL,
  actiontypeid integer NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250) NOT NULL,
  surveydatedaysoffset integer NOT NULL,
  priority integer DEFAULT 3, -- 1 = highest, 5 = lowest
  CONSTRAINT actionruleid_pkey PRIMARY KEY (actionruleid),
  CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid)
      REFERENCES action.actionplan (actionplanid) 
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid)
      REFERENCES action.actiontype (actiontypeid)
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.actionrule
  OWNER TO postgres;
COMMENT ON COLUMN action.actionrule.priority IS '1 = highest, 5 = lowest';




-- Table: action.action
DROP TABLE action.action CASCADE;

CREATE TABLE action.action
(
  actionid bigint NOT NULL DEFAULT nextval('action.actionidseq'::regclass),
  caseid integer NOT NULL,
  actionplanid integer,
  actionruleid integer,
  actiontypeid integer NOT NULL,
  createdby character varying(50) NOT NULL,
  manuallycreated boolean NOT NULL,
  priority integer DEFAULT 3, -- 1 = highest, 5 = lowest
  situation character varying(100),
  state character varying(10) NOT NULL,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT actionid_pkey PRIMARY KEY (actionid),
  CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid)
      REFERENCES action.actionplan (actionplanid) 
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT actionruleid_fkey FOREIGN KEY (actionruleid)
      REFERENCES action.actionrule (actionruleid) 
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT actionstate_fkey FOREIGN KEY (state)
      REFERENCES action.actionstate (state) 
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid)
      REFERENCES action.actiontype (actiontypeid) 
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.action
  OWNER TO postgres;
COMMENT ON COLUMN action.action.priority IS '1 = highest, 5 = lowest';


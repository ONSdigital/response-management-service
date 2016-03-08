set schema 'action';

CREATE TABLE action.messagelog
(
  messageid bigint NOT NULL DEFAULT nextval('action.messageseq'::regclass),
  messagetext character varying,
  jobid numeric,
  messagelevel character varying,
  functionname character varying,
  createddatetime timestamp with time zone,
  CONSTRAINT messageid_pkey PRIMARY KEY (messageid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.messagelog
  OWNER TO postgres;

CREATE TABLE action.case
( actionplanjobid integer NOT NULL,
  caseid integer NOT NULL
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.case
  OWNER TO postgres;


CREATE TABLE action.survey
( surveyid integer NOT NULL,
  surveydate timestamp with time zone NOT NULL,
  name character varying(100) NOT NULL,
  CONSTRAINT surveyid_pkey PRIMARY KEY (surveyid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.survey
  OWNER TO postgres;


CREATE TABLE action.actiontype
( actiontypeid integer NOT NULL,
  name          character varying(100) NOT NULL,
  description   character varying(250) NOT NULL,
  provider      character varying(100),
  cancanel      boolean NOT NULL,
  CONSTRAINT actiontypeid_pkey PRIMARY KEY (actiontypeid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.actiontype
  OWNER TO postgres;


CREATE TABLE action.actionplan
( actionplanid integer NOT NULL, 
  surveyid integer NOT NULL,
  name character varying(100) NOT NULL,
  description   character varying(250) NOT NULL,
  createdby    character varying(20) NOT NULL,
  createddatetime timestamp with time zone NOT NULL,
  lastgoodrundatetime timestamp with time zone,
  CONSTRAINT actionplanid_pkey PRIMARY KEY (actionplanid),
  CONSTRAINT surveyid_fkey FOREIGN KEY (surveyid)
  REFERENCES action.survey (surveyid)
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.actionplan
  OWNER TO postgres;



CREATE TABLE action.actionrule
( actionruleid integer NOT NULL,
  actionplanid integer NOT NULL,
  actiontypeid integer NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250) NOT NULL,
  surveydatedaysoffset integer NOT NULL,
  priority integer NOT NULL,
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

CREATE TABLE action.action
( actionid bigint NOT NULL DEFAULT nextval('action.actionidseq'::regclass),
  caseid integer NOT NULL,
  actionplanid integer,
  actionruleid integer,
  actiontypeid integer NOT NULL,
  createdby character varying(20) NOT NULL,
  manuallycreated boolean NOT NULL,
  priority character varying(10) NOT NULL,
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
  CONSTRAINT actiontypeid_fkey FOREIGN KEY (actiontypeid)
  REFERENCES action.actiontype (actiontypeid)
  ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.action
  OWNER TO postgres;



CREATE TABLE action.actionplanjob
( actionplanjobid integer NOT NULL,
  actionplanid integer NOT NULL,
  createdby character varying(20) NOT NULL,
  state     character varying(100) NOT NULL,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT actionplanjobid_pkey PRIMARY KEY (actionplanjobid),
  CONSTRAINT actionplanid_fkey FOREIGN KEY (actionplanid)
  REFERENCES action.actionplan (actionplanid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE action.actionplanjob
  OWNER TO postgres;





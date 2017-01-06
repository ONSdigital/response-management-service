SET SCHEMA 'party';

CREATE TABLE party.party
(
  id integer NOT NULL,
  parentpartyid integer NOT NULL,
  optlockversion int default 0,
  modversion int default 0,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT party_pkey PRIMARY KEY (id),
  CONSTRAINT parent_fkey FOREIGN KEY (parentid) REFERENCES party(id);
);

CREATE TABLE party.relationship
(
  id integer NOT NULL,
  parentpartytypeid integer NOT NULL,
  childpartytypeid integer NOT NULL,
  CONSTRAINT relationship_pkey PRIMARY KEY (id),
  CONSTRAINT parent_fkey FOREIGN KEY (parentpartytypeid) REFERENCES partytype(id),
  CONSTRAINT child_fkey FOREIGN KEY (childpartytypeid) REFERENCES partytype(id);
);

CREATE TABLE party.partyattribute
(
  id integer NOT NULL,
  partyattributetypeid integer NOT NULL,
  value character varying(200) NOT NULL,
  optlockversion int default 0,
  modversion int default 0,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT code_pkey PRIMARY KEY (code),
);

CREATE TABLE party.partytype
(
  id integer NOT NULL,
  name character varying(60) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT partytype_pkey PRIMARY KEY (id)
);

CREATE TABLE party.partyattributetype
(
  id integer NOT NULL,
  partytypeid integer NOT NULL,
  valuetype character varying(20) NOT NULL,
  name character varying(60) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT ptyattrtype_pkey PRIMARY KEY (id),
  CONSTRAINT valuetype_fkey FOREIGN KEY (valuetype) REFERENCES valuetype(name),
  CONSTRAINT partytype_fkey FOREIGN KEY (partytypeid) REFERENCES partytype(id)
);

CREATE TABLE party.valuetype
(
  name character varying(20) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT valuetype_pkey PRIMARY KEY (name),
);

INSERT INTO party.valuetype VALUES('STRING','String');
INSERT INTO party.valuetype VALUES('FLOAT','Floating point number');
INSERT INTO party.valuetype VALUES('INTEGER','Integer number');
INSERT INTO party.valuetype VALUES('BOOLEAN','Boolean');
INSERT INTO party.valuetype VALUES('DATETIME','UTC DateTime');

INSERT INTO party.partytype VALUES(1,'COMMUNAL','Communal Establishment');
INSERT INTO party.partytype VALUES(2,'HOUSEHOLD','Household');
INSERT INTO party.partytype VALUES(3,'HOUSEHOLD_INDIVIDUAL','Household Individual');
INSERT INTO party.partytype VALUES(4,'COMMUNAL_INDIVIDUAL','Communal Individual');


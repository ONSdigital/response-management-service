SET SCHEMA 'party';

-- INSTANCE DATA TABLES
CREATE TABLE party.party
(
  id integer NOT NULL,
  partytypeid integer NOT NULL,
  optlockversion int default 0,
  effectivestartddatetime timestamp with time zone NOT NULL,
  effectiveenddatetime timestamp with time zone,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT party_pkey PRIMARY KEY (id)
);

CREATE TABLE party.relationship
(
  id integer NOT NULL,
  parentpartytypeid integer NOT NULL,
  childpartytypeid integer NOT NULL,
  optlockversion int default 0,
  CONSTRAINT relationship_pkey PRIMARY KEY (id)
);

CREATE TABLE party.attribute
(
  id integer NOT NULL,
  attributetypeid integer NOT NULL,
  attributevalue character varying(200) NOT NULL,
  optlockversion int default 0,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT partyattr_pkey PRIMARY KEY (id)
);

-- METADATA TABLES
CREATE TABLE party.partytype
(
  id integer NOT NULL,
  name character varying(60) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT partytype_pkey PRIMARY KEY (id)
);

CREATE TABLE party.attributetype
(
  id integer NOT NULL,
  partytypeid integer NOT NULL,
  partyvaluetypeid INTEGER NOT NULL,
  name character varying(60) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT ptyattrtype_pkey PRIMARY KEY (id)
);

CREATE TABLE party.valuetype
(
  id integer NOT NULL,
  name character varying(20) NOT NULL,
  description character varying(100) NOT NULL,
  CONSTRAINT valuetype_pkey PRIMARY KEY (id)
);

ALTER TABLE ONLY party
    ADD CONSTRAINT partytype_fkey FOREIGN KEY (partytypeid) REFERENCES partytype(id);
ALTER TABLE ONLY relationship
    ADD CONSTRAINT parent_fkey FOREIGN KEY (parentpartytypeid) REFERENCES partytype(id);
ALTER TABLE ONLY relationship
    ADD CONSTRAINT child_fkey FOREIGN KEY (childpartytypeid) REFERENCES partytype(id);
ALTER TABLE ONLY attributetype
    ADD CONSTRAINT valuetype_fkey FOREIGN KEY (partyvaluetypeid) REFERENCES valuetype(id);
ALTER TABLE ONLY attributetype
    ADD CONSTRAINT partytype_fkey FOREIGN KEY (partytypeid) REFERENCES partytype(id);



INSERT INTO party.valuetype VALUES(1, 'STRING','String');
INSERT INTO party.valuetype VALUES(2, 'FLOAT','Floating point number');
INSERT INTO party.valuetype VALUES(3, 'INTEGER','Integer number');
INSERT INTO party.valuetype VALUES(4, 'LONG','Long Integer number');
INSERT INTO party.valuetype VALUES(5, 'BOOLEAN','Boolean');
INSERT INTO party.valuetype VALUES(6, 'DATETIME','UTC DateTime');

INSERT INTO party.partytype VALUES(1,'COMMUNAL','Communal Establishment');
INSERT INTO party.partytype VALUES(2,'HOUSEHOLD','Household');
INSERT INTO party.partytype VALUES(3,'HOUSEHOLD_INDIVIDUAL','Household Individual');
INSERT INTO party.partytype VALUES(4,'COMMUNAL_INDIVIDUAL','Communal Individual');

INSERT INTO party.attributetype VALUES(1, 2, 1, 'ADDRESS_LINE_1', 'Address Line 1');
INSERT INTO party.attributetype VALUES(2, 2, 1, 'ADDRESS_LINE_2', 'Address Line 2');
INSERT INTO party.attributetype VALUES(3, 2, 1, 'POSTCODE', 'Postcode');
INSERT INTO party.attributetype VALUES(4, 2, 4, 'UPRN', 'UPRN');


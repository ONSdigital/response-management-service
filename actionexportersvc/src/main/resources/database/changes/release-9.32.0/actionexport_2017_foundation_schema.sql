--script to create all tables, functions and sequences for foundation for 2017

SET SCHEMA 'actionexporter';

CREATE TABLE actionrequest (
  actionid bigint NOT NULL,
  responserequired boolean DEFAULT FALSE,
  actionplan character varying(100) NOT NULL,
  actiontype character varying(100) NOT NULL,
  questionset character varying(10),
  contactid bigint,
  uprn numeric(12,0) NOT NULL,
  caseid bigint,
  priority character varying(10),
  caseref character varying(16) NOT NULL,
  iac character varying (24) NOT NULL,
  dateStored timestamp with time zone,
  dateSent timestamp with time zone
);

CREATE TABLE address (
  uprn numeric(12,0) NOT NULL,
  addresstype character varying(6),
  estabtype character varying(6),
  category character varying(20),
  organisation_name character varying(60),
  address_line1 character varying(60),
  address_line2 character varying(60),  
  locality character varying(35),
  town_name character varying(30),
  postcode character varying(8),
  lad character varying(9),
  latitude double precision,
  longitude double precision
);

CREATE TABLE templatemapping (
  actiontype character varying(100) NOT NULL,
  template character varying(100) NOT NULL,
  file character varying(100) NOT NULL,
  datemodified timestamp with time zone
);

CREATE TABLE template (
  name character varying(100) NOT NULL,
  content text NOT NULL,
  datemodified timestamp with time zone
);

CREATE SEQUENCE contactidseq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;

CREATE TABLE contact (
    contactid integer DEFAULT nextval('contactidseq'::regclass) NOT NULL,
    forename character varying(35),
    surname character varying(35),
    phonenumber character varying(20),
    emailaddress character varying(50),
    title character varying(20)
);

ALTER TABLE ONLY address
    ADD CONSTRAINT addressuprn_pkey PRIMARY KEY (uprn);

ALTER TABLE ONLY actionrequest
    ADD CONSTRAINT actionrequestactionid_pkey PRIMARY KEY (actionid);

ALTER TABLE ONLY templatemapping
    ADD CONSTRAINT templatemappingactiontype_pkey PRIMARY KEY (actiontype);

ALTER TABLE ONLY template
    ADD CONSTRAINT templatename_pkey PRIMARY KEY (name);

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (contactid);

ALTER TABLE ONLY actionrequest
    ADD CONSTRAINT uprn_fkey FOREIGN KEY (uprn) REFERENCES address(uprn);

ALTER TABLE ONLY actionrequest
    ADD CONSTRAINT contactid_fkey FOREIGN KEY (contactid) REFERENCES contact(contactid);

ALTER TABLE ONLY templatemapping
    ADD CONSTRAINT template_fkey FOREIGN KEY (template) REFERENCES template(name);


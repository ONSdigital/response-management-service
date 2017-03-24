SET SCHEMA 'respondent';
  
CREATE TABLE respondent.respondenttype (
    respondenttype character varying(10) NOT NULL
);


INSERT INTO respondenttype (respondenttype) VALUES ('H');
INSERT INTO respondenttype (respondenttype) VALUES ('HI');
INSERT INTO respondenttype (respondenttype) VALUES ('C');
INSERT INTO respondenttype (respondenttype) VALUES ('CI');
INSERT INTO respondenttype (respondenttype) VALUES ('R');
INSERT INTO respondenttype (respondenttype) VALUES ('RI'


CREATE TABLE respondent.respondent
(
  respondentid integer NOT NULL,
  respondentref character varying(32) NOT NULL,
  respondenttype character varying(10) NOT NULL,
  optlockversion int default 0,
  createddatetime timestamp with time zone NOT NULL,
  updateddatetime timestamp with time zone,
  CONSTRAINT respondent_pkey PRIMARY KEY (id)
);

CREATE TABLE respondent.address
(
    addressid integer NOT NULL,
    addresstype character varying(6),
    estabtype character varying(6),
    category character varying(20),
    organisation_name character varying(60),
    address_line1 character varying(60),
    address_line2 character varying(60),
    locality character varying(35),
    town_name character varying(30),
    postcode character varying(8),
    oa character varying(9),
    lsoa character varying(9),
    msoa character varying(9),
    lad character varying(9),
    region character varying(9),
    eastings numeric(8,0),
    northings numeric(8,0),
    htc numeric(8,0),
    latitude double precision,
    longitude double precision,
    sample character varying(20),
    optlockversion int default 0,
  CONSTRAINT address_pkey PRIMARY KEY (addressid)
);

CREATE TABLE respondent.person
(
    personid integer NOT NULL,
    title character varying(20)
    forename character varying(35),
    surname character varying(35),
    optlockversion int default 0
  CONSTRAINT person_pkey PRIMARY KEY (personid)
);

CREATE TABLE respondent.contact
(
    contactid integer NOT NULL,
    phonenumber character varying(20),
    emailaddress character varying(50),
    optlockversion int default 0
  CONSTRAINT contact_pkey PRIMARY KEY (contactid)
);



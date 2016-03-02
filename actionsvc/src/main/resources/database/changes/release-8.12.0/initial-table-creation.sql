set schema 'action';


CREATE TABLE action (
    actionid integer NOT NULL,
    caseid integer,
    actionplanid integer,
    action_status character varying(10),
    action_type character varying(10),
    priority character varying(10),
    situation character varying(10),
    created_datetime timestamp with time zone,
    created_by character varying(20)
);


ALTER TABLE action.action OWNER TO postgres;


CREATE TABLE actionplan (
    actionplanid integer NOT NULL,
    actionplan_name character varying(20),
    description character varying(100),
    rules character varying(100)
);


ALTER TABLE action.actionplan OWNER TO postgres;


ALTER TABLE ONLY action
    ADD CONSTRAINT action_pkey PRIMARY KEY (actionid);



ALTER TABLE ONLY actionplan
    ADD CONSTRAINT actionplan_pkey PRIMARY KEY (actionplanid);




ALTER TABLE ONLY action
    ADD CONSTRAINT action_actionplanid_fkey FOREIGN KEY (actionplanid) REFERENCES actionplan(actionplanid);



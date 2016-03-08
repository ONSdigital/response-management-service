--ALTER USER actionsvc SET search_path to 'action,refdata';

DROP USER actionsvc;
DROP OWNED BY actionsvc;

CREATE USER actionsvc LOGIN
  PASSWORD 'actionsvc'
  NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
GRANT role_connect TO actionsvc;


DROP SCHEMA action cascade;
create schema action AUTHORIZATION postgres;

REVOKE CONNECT ON DATABASE postgres FROM PUBLIC;
GRANT CONNECT
ON DATABASE postgres 
TO actionsvc;

REVOKE ALL
ON ALL TABLES IN SCHEMA action 
FROM PUBLIC;

REVOKE ALL
ON ALL SEQUENCES IN SCHEMA action 
FROM PUBLIC;

ALTER DEFAULT PRIVILEGES 
    FOR USER postgres
    IN SCHEMA action
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO actionsvc;

ALTER DEFAULT PRIVILEGES 
    FOR USER postgres
    IN SCHEMA action
    GRANT ALL ON SEQUENCES TO actionsvc;

GRANT ALL PRIVILEGES ON DATABASE postgres to actionsvc;
GRANT ALL ON SCHEMA action TO actionsvc;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA action TO actionsvc;

GRANT ALL ON SCHEMA refdata TO actionsvc;
GRANT SELECT ON ALL TABLES IN SCHEMA refdata TO actionsvc;






drop table action.action cascade;
drop table action.actionplan cascade;
drop table action.databasechangelog cascade;
drop table action.databasechangeloglock cascade;


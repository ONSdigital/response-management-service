-- Need conditional test for Revoke if rolename doesn't already exist 

DO $$
BEGIN
IF EXISTS (SELECT 1 FROM pg_roles WHERE rolname='actionexportsvc') THEN
   REVOKE ALL PRIVILEGES ON DATABASE postgres FROM actionexportsvc;
END IF;
END$$;

DROP SCHEMA IF EXISTS actionexport CASCADE;
DROP ROLE IF EXISTS actionexportsvc;

CREATE USER actionexportsvc PASSWORD 'actionexportsvc'
  NOSUPERUSER NOCREATEDB NOCREATEROLE NOREPLICATION INHERIT LOGIN;

CREATE SCHEMA actionexport AUTHORIZATION actionexportsvc;

REVOKE ALL ON ALL TABLES IN SCHEMA actionexport FROM PUBLIC;
REVOKE ALL ON ALL SEQUENCES IN SCHEMA actionexport FROM PUBLIC;
REVOKE CONNECT ON DATABASE postgres FROM PUBLIC;

GRANT CONNECT ON DATABASE postgres TO actionexportsvc;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA actionexport TO actionexportsvc;
GRANT ALL ON ALL SEQUENCES IN SCHEMA actionexport TO actionexportsvc;

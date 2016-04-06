SET SCHEMA 'action';

CREATE SEQUENCE actionplanidseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999999999999
  START 1
  CACHE 1;
ALTER TABLE actionplanidseq OWNER TO postgres;

TRUNCATE TABLE actionplan CASCADE;
ALTER TABLE actionplan ALTER COLUMN actionplanid SET DEFAULT nextval('actionplanidseq');
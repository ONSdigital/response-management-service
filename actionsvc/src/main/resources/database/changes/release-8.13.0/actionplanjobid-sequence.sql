set schema 'action';

-- Sequence: action.actionplanjobseq
-- DROP SEQUENCE action.actionplanjobseq;

CREATE SEQUENCE action.actionplanjobseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999999999999
  START 1
  CACHE 1;
ALTER TABLE action.actionplanjobseq
  OWNER TO postgres;

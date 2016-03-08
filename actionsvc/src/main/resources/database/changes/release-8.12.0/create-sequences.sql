set schema 'action';

-- Sequence: action.messageseq
-- DROP SEQUENCE action.messageseq;
CREATE SEQUENCE action.messageseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999999999999
  START 1
  CACHE 1;
ALTER TABLE action.messageseq
  OWNER TO postgres;



-- Sequence: action.actionidseq
-- DROP SEQUENCE action.actionidseq;
CREATE SEQUENCE action.actionidseq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 999999999999
  START 1
  CACHE 1;
ALTER TABLE action.actionidseq
  OWNER TO postgres;


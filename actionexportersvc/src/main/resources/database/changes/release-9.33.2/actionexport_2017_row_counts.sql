-- Create a table to store filename row counts

SET SCHEMA 'actionexporter';

CREATE TABLE filerowcount (
  filename character varying(100) NOT NULL,
  rowcount integer NOT NULL,
  datesent timestamp with time zone NOT NULL,
  sendresult boolean NOT NULL,
  reported boolean NOT NULL DEFAULT false
);

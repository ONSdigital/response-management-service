DELETE FROM actionexporter.reporttype
WHERE reporttype = 'PRINT_VOLUMES';

ALTER SEQUENCE actionexporter.messageseq RESTART WITH 1;
ALTER SEQUENCE actionexporter.reportidseq RESTART WITH 1;
ALTER SEQUENCE actionexporter.reporttypeidseq RESTART WITH 1;

-- Insert print volume report into report tables 
INSERT INTO actionexporter.reporttype(reporttype,orderid,displayname) VALUES('PRINT_VOLUMES',10,'Print Volumes');
-- CTPA-1045 Tidy up capitalisation of database string
-- welsh = Welsh
-- english = English
-- id = ID
-- Billingual = Bi-Lingual
-- billingual = Bi-Lingual

UPDATE action.actionplan
SET  description = replace(description,'welsh','Welsh');

UPDATE action.actionplan
SET  description = replace(description,'english','English');

UPDATE action.actionplan
SET  description = replace(description,'id','ID');

UPDATE action.actionplan
SET  description = replace(description,'Bilingual','Bi-Lingual');

UPDATE action.actionplan
SET description = replace(description,'bilingual','Bi-Lingual');


UPDATE action.actiontype
SET  description = replace(description,'welsh','Welsh');

UPDATE action.actiontype
SET  description = replace(description,'english','English');

UPDATE action.actiontype
SET  description = replace(description,'id','ID');

UPDATE action.actiontype
SET  description = replace(description,'Bilingual','Bi-Lingual');

UPDATE action.actiontype
SET description = replace(description,'bilingual','Bi-Lingual');



UPDATE action.actionrule
SET  description = replace(description,'welsh','Welsh');

UPDATE action.actionrule
SET  description = replace(description,'english','English');

UPDATE action.actionrule
SET  description = replace(description,'id','ID');

UPDATE action.actionrule
SET  description = replace(description,'Bilingual','Bi-Lingual');

UPDATE action.actionrule
SET description = replace(description,'bilingual','Bi-Lingual');
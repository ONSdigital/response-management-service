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
SET  description = replace(description,'sexual id','sexual ID');

UPDATE action.actionplan
SET  description = replace(description,'Bi-Lingual','Bilingual');

UPDATE action.actionplan
SET  description = replace(description,'bilingual','Bilingual');



UPDATE action.actiontype
SET  description = replace(description,'welsh','Welsh');

UPDATE action.actiontype
SET  description = replace(description,'english','English');

UPDATE action.actiontype
SET  description = replace(description,'sexual id','sexual ID');

UPDATE action.actiontype
SET  description = replace(description,'Bi-Lingual','Bilingual');

UPDATE action.actiontype
SET  description = replace(description,'bilingual','Bilingual');



UPDATE action.actionrule
SET  description = replace(description,'welsh','Welsh');

UPDATE action.actionrule
SET  description = replace(description,'english','English');

UPDATE action.actionrule
SET  description = replace(description,'sexual id','sexual ID');

UPDATE action.actionrule
SET  description = replace(description,'Bi-Lingual','Bilingual');

UPDATE action.actionrule
SET  description = replace(description,'bilingual','Bilingual');

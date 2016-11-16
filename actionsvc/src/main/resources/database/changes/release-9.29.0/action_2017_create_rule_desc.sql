-- Create action rule descriptions
UPDATE action.actionrule r
SET description = 
(SELECT CASE WHEN r.surveydatedaysoffset >= 0 THEN (trim(t.description) || '(SD+' || r.surveydatedaysoffset::text ||')') 
             ELSE  (trim(t.description) || '(SD' || r.surveydatedaysoffset::text ||')') END
 FROM  action.actiontype t
 WHERE r.actiontypeid = t.actiontypeid)
 WHERE r.actionruleid IN(SELECT r1.actionruleid FROM action.actionrule r1 WHERE r1.description = '');
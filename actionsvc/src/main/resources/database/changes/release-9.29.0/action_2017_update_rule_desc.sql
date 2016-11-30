 WITH ruledesc AS 
 (SELECT r.actionruleid
        ,r.description as old
        ,CASE WHEN r.surveydatedaysoffset >= 0 THEN (trim(t.description) || '(SD+' || r.surveydatedaysoffset::text ||')') 
              ELSE  (trim(t.description) || '(SD' || r.surveydatedaysoffset::text ||')') END as new
 FROM  action.actiontype t,action.actionrule r
 WHERE r.actiontypeid = t.actiontypeid)

 UPDATE action.actionrule ar
 SET description =  (SELECT r.new FROM ruledesc r WHERE r.old <> r.new AND ar.actionruleid = r.actionruleid)
 WHERE ar.actionruleid IN(SELECT actionruleid from ruledesc WHERE old <> new);

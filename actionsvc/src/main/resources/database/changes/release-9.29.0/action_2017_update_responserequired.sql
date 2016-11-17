UPDATE action.actiontype
SET responserequired = TRUE
WHERE actiontypeid = 52;

UPDATE action.actiontype
SET responserequired = FALSE
WHERE actiontypeid BETWEEN 44 AND 51;

UPDATE action.actiontype
SET responserequired = FALSE
WHERE actiontypeid IN(53,54);
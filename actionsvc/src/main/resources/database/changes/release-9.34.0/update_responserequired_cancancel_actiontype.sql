UPDATE action.actiontype 
SET responserequired = FALSE
   ,cancancel        = FALSE
WHERE handler          = 'Printer'
AND   responserequired = TRUE;
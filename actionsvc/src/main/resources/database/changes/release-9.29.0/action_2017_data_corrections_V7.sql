UPDATE action.actiontype
SET name = 'SMSE'
   ,description =  'Send Internet Access Code (English)'
WHERE actiontypeid = 40;

UPDATE action.actiontype
SET name = 'SMSB'
   ,description =  'Send Internet Access Code (Bilingual)'
WHERE actiontypeid = 43;

UPDATE action.actionrule
SET actiontypeid = 40
  , description = 'Send Internet Access Code (English) (SD-100)'
  , name = 'SMSE-100'
WHERE actiontypeid IN(40,43);

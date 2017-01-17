-- Update actiontype names

UPDATE action.actiontype
SET name = 'ICLAD1_2003'
WHERE actiontypeid = 4;

UPDATE action.actiontype
SET name = '1RLAD1_0504'
WHERE actiontypeid = 7;

UPDATE action.actiontype
SET name = '2RLAD1_1804'
WHERE actiontypeid = 10;

UPDATE action.actiontype
SET name = '3RLAD1_2604'
WHERE actiontypeid = 13;

UPDATE action.actiontype
SET name = 'H1_2604QR'
WHERE actiontypeid = 19;
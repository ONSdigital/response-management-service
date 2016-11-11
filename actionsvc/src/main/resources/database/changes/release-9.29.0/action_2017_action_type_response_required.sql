
set schema 'action';
ALTER TABLE action.actiontype add column responserequired boolean;

UPDATE action.actiontype set responserequired=false where actiontypeid between 1 and 4;
UPDATE action.actiontype set responserequired=true where actiontypeid between 5 and 13;
UPDATE action.actiontype set responserequired=true where actiontypeid=14;
UPDATE action.actiontype set responserequired=false where actiontypeid between 15 and 18;
UPDATE action.actiontype set responserequired=true where actiontypeid=19;
UPDATE action.actiontype set responserequired=true where actiontypeid between 20 and 23;
UPDATE action.actiontype set responserequired=false where actiontypeid between 24 and 43;

UPDATE action.actiontype set cancancel=false where actiontypeid between 1 and 4;
UPDATE action.actiontype set cancancel=true where actiontypeid between 5 and 14;
UPDATE action.actiontype set cancancel=false where actiontypeid between 15 and 18;
UPDATE action.actiontype set cancancel=true where actiontypeid=19;
UPDATE action.actiontype set cancancel=false where actiontypeid between 20 and 43;
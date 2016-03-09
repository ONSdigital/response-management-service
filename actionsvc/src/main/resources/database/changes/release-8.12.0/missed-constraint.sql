ALTER TABLE action.case
ADD CONSTRAINT actionplanjobid_fkey FOREIGN KEY (actionplanjobid)
      REFERENCES action.actionplanjob (actionplanjobid);

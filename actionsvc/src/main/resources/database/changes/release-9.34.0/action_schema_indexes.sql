CREATE INDEX action_state_index ON action.action(state);
CREATE INDEX action_actionplanid_index ON action.action(actionplanid);
CREATE INDEX action_actiontypeid_index ON action.action(actiontypeid);
CREATE INDEX actiontype_name_index ON action.actiontype(name);
CREATE INDEX actionrule_actionplanid_index on action.actionrule(actionplanid);
CREATE INDEX actionrule_actiontypeid_index on action.actionrule(actiontypeid);
CREATE INDEX actioncase_actionplanid_index on action.case(actionplanid);
CREATE INDEX action_optlockversion_index on action.action(optlockversion);

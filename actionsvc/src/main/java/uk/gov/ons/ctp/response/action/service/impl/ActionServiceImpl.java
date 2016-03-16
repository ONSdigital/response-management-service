package uk.gov.ons.ctp.response.action.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * An ActionService implementation which encapsulates all business logic
 * operating on the Action entity model.
 */

@Named
@Slf4j
public final class ActionServiceImpl implements ActionService {

  @Inject
  private ActionRepository actionRepo;

  @Override
  public List<Action> findActionsByTypeAndState(final String actionTypeName, final String state) {
    log.debug("Entering findActionsByTypeAndState with {} {}", actionTypeName, state);
    List<Action> actions = null;
    if (actionTypeName != null) {
      if (state != null) {
        actions = actionRepo.findByActionTypeNameAndState(actionTypeName, state);
      } else {
        actions = actionRepo.findByActionTypeName(actionTypeName);
      }
    } else {
      if (state != null) {
        actions = actionRepo.findByState(state);
      } else {
        actions = new ArrayList<Action>();
      }
    }
    return actions;
    /**
     * if (!(actionTypeName == null) && !(state == null)) { return
     * actionRepo.findByActionTypeNameAndState(actionTypeName, state); } else if
     * (!(actionTypeName == null) && (state == null)) { return
     * actionRepo.findByActionTypeName(actionTypeName); } else if
     * ((actionTypeName == null) && !(state == null)) { return
     * actionRepo.findByState(state); } else { return new ArrayList<Action>(); }
     **/
  }

  @Override
  public Action findActionByActionId(final Integer actionId) {
    log.debug("Entering findActionByActionId with {}", actionId);
    return actionRepo.findOne(actionId);
  }

  @Override
  public List<Action> findActionsByCaseId(final Integer caseId) {
    log.debug("Entering findActionsByCaseId with {}", caseId);
    return actionRepo.findByCaseId(caseId);
  }
}

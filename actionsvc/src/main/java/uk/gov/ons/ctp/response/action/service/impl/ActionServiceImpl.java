package uk.gov.ons.ctp.response.action.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

  private static final int TRANSACTION_TIMEOUT = 30;
  
  @Inject
  private ActionRepository actionRepo;

  @Override
  public List<Action> findActionsByTypeAndState(final String actionTypeName, final String state) {
    log.debug("Entering findActionsByTypeAndState with {} {}", actionTypeName, state);
    return actionRepo.findByActionTypeNameAndState(actionTypeName, state);
  }

  @Override
  public List<Action> findActionsByType(final String actionTypeName) {
    log.debug("Entering findActionsByType with {}", actionTypeName);
    return actionRepo.findByActionTypeName(actionTypeName);
  }

  @Override
  public List<Action> findActionsByState(final String state) {
    log.debug("Entering findActionsByState with {}", state);
    return actionRepo.findByState(state);
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

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public Action createAction(final Action action) {
    log.debug("Entering createAction with {}", action);
    action.setManuallyCreated(true);
    action.setCreatedDateTime(new Timestamp(System.currentTimeMillis()));
    return actionRepo.saveAndFlush(action);
  }
}

package uk.gov.ons.ctp.response.action.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionTypeRepository;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * An ActionService implementation which encapsulates all business logic
 * operating on the Action entity model.
 */

@Service
@Slf4j
public class ActionServiceImpl implements ActionService {

  private static final int TRANSACTION_TIMEOUT = 30;

  @Autowired
  private ActionRepository actionRepo;

  @Autowired
  private ActionTypeRepository actionTypeRepo;

  @Autowired
  private StateTransitionManager<ActionState, uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent> actionSvcStateTransitionManager;

  @Override
  public List<Action> findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(final String actionTypeName,
      final ActionDTO.ActionState state) {
    log.debug("Entering findActionsByTypeAndState with {} {}", actionTypeName, state);
    return actionRepo.findByActionTypeNameAndStateOrderByCreatedDateTimeDesc(actionTypeName, state);
  }

  @Override
  public List<Action> findActionsByType(final String actionTypeName) {
    log.debug("Entering findActionsByType with {}", actionTypeName);
    return actionRepo.findByActionTypeNameOrderByCreatedDateTimeDesc(actionTypeName);
  }

  @Override
  public List<Action> findActionsByState(final ActionDTO.ActionState state) {
    log.debug("Entering findActionsByState with {}", state);
    return actionRepo.findByStateOrderByCreatedDateTimeDesc(state);
  }

  @Override
  public Action findActionByActionId(final BigInteger actionId) {
    log.debug("Entering findActionByActionId with {}", actionId);
    return actionRepo.findOne(actionId);
  }

  @Override
  public List<Action> findActionsByCaseId(final Integer caseId) {
    log.debug("Entering findActionsByCaseId with {}", caseId);
    return actionRepo.findByCaseIdOrderByCreatedDateTimeDesc(caseId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public List<Action> cancelActions(final Integer caseId) {
    log.debug("Entering cancelAction with {}", caseId);

    List<Action> flushedActions = new ArrayList<>();
    List<Action> actions = actionRepo.findByCaseId(caseId);
    for (Action action : actions) {
      if (action.getActionType().getCanCancel()) {
        log.debug("Cancelling action {} of type {}", action.getActionId(), action.getActionType().getName());
        ActionDTO.ActionState nextState = actionSvcStateTransitionManager.transition(action.getState(),
            ActionEvent.REQUEST_CANCELLED);
        action.setState(nextState);
        action.setUpdatedDateTime(DateTimeUtil.nowUTC());
        actionRepo.saveAndFlush(action);
        flushedActions.add(action);
      }
    }
    return flushedActions;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public Action feedBackAction(ActionFeedback actionFeedback) {
    log.debug("Entering feedBackAction with {}", actionFeedback.getActionId());
    Action action = null;

    action = actionRepo.findOne(actionFeedback.getActionId());
    if (action != null) {
      ActionDTO.ActionEvent event = ActionDTO.ActionEvent.valueOf(actionFeedback.getOutcome().name());
      action.setSituation(actionFeedback.getSituation());
      action.setUpdatedDateTime(DateTimeUtil.nowUTC());
      ActionDTO.ActionState nextState = actionSvcStateTransitionManager.transition(action.getState(), event);
      action.setState(nextState);
      action = actionRepo.saveAndFlush(action);
    }
    return action;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public Action createAction(final Action action) {
    log.debug("Entering createAction with {}", action);
    // the incoming action has a placeholder action type with the name as
    // provided to the caller
    // but we need the entire action type object for that action type name
    ActionType actionType = actionTypeRepo.findByName(action.getActionType().getName());
    // guard against the caller providing an id - we would perform an update
    // otherwise
    action.setActionId(null);
    action.setActionType(actionType);
    action.setManuallyCreated(true);
    action.setCreatedDateTime(DateTimeUtil.nowUTC());
    action.setState(ActionDTO.ActionState.SUBMITTED);
    return actionRepo.saveAndFlush(action);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public Action updateAction(final Action action) {
    log.debug("Entering updateAction with {}", action);
    Action existingAction = actionRepo.findOne(action.getActionId());
    if (existingAction != null) {
      boolean needsUpdate = false;

      Integer newPriority = action.getPriority();
      log.debug("newPriority = {}", newPriority);
      if (newPriority != null) {
        needsUpdate = true;
        existingAction.setPriority(newPriority);
      }

      String newSituation = action.getSituation();
      log.debug("newSituation = {}", newSituation);
      if (newSituation != null) {
        needsUpdate = true;
        existingAction.setSituation(newSituation);
      }

      if (needsUpdate) {
        existingAction.setUpdatedDateTime(DateTimeUtil.nowUTC());
        log.debug("updating action with {}", existingAction);
        existingAction = actionRepo.saveAndFlush(existingAction);
      }
    }
    return existingAction;
  }

}

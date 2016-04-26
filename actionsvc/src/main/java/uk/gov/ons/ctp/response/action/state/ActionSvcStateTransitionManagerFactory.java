package uk.gov.ons.ctp.response.action.state;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import uk.gov.ons.ctp.common.state.BasicStateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManagerFactory;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;

/**
 * This is the state transition manager actory for the actionsvc. It intended
 * that this will be refactored into a common framework class and that it
 * initialises each entities manager from database held transitions.
 */
@Named
public class ActionSvcStateTransitionManagerFactory implements StateTransitionManagerFactory {

  public static final String ACTION_ENTITY = "Action";

  private Map<String, StateTransitionManager<?, ?>> managers;

  /**
   * Create and init the factory with concrete StateTransitionManagers for each
   * required entity
   */
  public ActionSvcStateTransitionManagerFactory() {
    managers = new HashMap<>();

    Map<ActionDTO.ActionState, Map<ActionEvent, ActionDTO.ActionState>> transitions = new HashMap<>();

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForSubmitted = new HashMap<>();
    transitionMapForSubmitted.put(ActionEvent.REQUEST_DISTRIBUTED, ActionDTO.ActionState.PENDING);
    transitionMapForSubmitted.put(ActionEvent.REQUEST_CANCELLED, ActionDTO.ActionState.ABORTED);
    transitions.put(ActionDTO.ActionState.SUBMITTED, transitionMapForSubmitted);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForPending = new HashMap<>();
    transitionMapForPending.put(ActionEvent.REQUEST_FAILED, ActionDTO.ActionState.SUBMITTED);
    transitionMapForPending.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForPending.put(ActionEvent.REQUEST_ACCEPTED, ActionDTO.ActionState.ACTIVE);
    transitionMapForPending.put(ActionEvent.REQUEST_COMPLETED, ActionDTO.ActionState.COMPLETED);
    transitions.put(ActionDTO.ActionState.PENDING, transitionMapForPending);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForActive = new HashMap<>();
    transitionMapForActive.put(ActionEvent.REQUEST_FAILED, ActionDTO.ActionState.SUBMITTED);
    transitionMapForActive.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForActive.put(ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.COMPLETED);
    transitions.put(ActionDTO.ActionState.ACTIVE, transitionMapForActive);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForCompleted = new HashMap<>();
    transitionMapForCompleted.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.COMPLETED);
    transitions.put(ActionDTO.ActionState.COMPLETED, transitionMapForCompleted);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForCancelSubmitted = new HashMap<>();
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_ACCEPTED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.CANCELLATION_DISTRIBUTED,
        ActionDTO.ActionState.CANCEL_PENDING);
    transitions.put(ActionDTO.ActionState.CANCEL_SUBMITTED, transitionMapForCancelSubmitted);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForCancelPending = new HashMap<>();
    transitionMapForCancelPending.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_FAILED,
        ActionDTO.ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_ACCEPTED,
        ActionDTO.ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_ACCEPTED,
        ActionDTO.ActionState.CANCELLING);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_COMPLETED,
        ActionDTO.ActionState.CANCELLED);
    transitions.put(ActionDTO.ActionState.CANCEL_PENDING, transitionMapForCancelPending);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForCancelling = new HashMap<>();
    transitionMapForCancelling.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCELLING);
    transitionMapForCancelling.put(ActionEvent.CANCELLATION_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelling.put(ActionEvent.CANCELLATION_COMPLETED,
        ActionDTO.ActionState.CANCELLED);
    transitions.put(ActionDTO.ActionState.CANCELLING, transitionMapForCancelling);

    Map<ActionEvent, ActionDTO.ActionState> transitionMapForCancelled = new HashMap<>();
    transitionMapForCancelled.put(ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCELLED);
    transitions.put(ActionDTO.ActionState.CANCELLED, transitionMapForCancelled);

    StateTransitionManager<ActionDTO.ActionState, ActionEvent> actionStateTransitionManager = new BasicStateTransitionManager<>(
        transitions);
    managers.put(ACTION_ENTITY, actionStateTransitionManager);

  }

  /*
   * (non-Javadoc)
   * 
   * @see uk.gov.ons.ctp.response.action.state.StateTransitionManagerFactory#
   * getStateTransitionManager(java.lang.String)
   */
  @Override
  public StateTransitionManager<?, ?> getStateTransitionManager(String entity) {
    return managers.get(entity);
  }

}

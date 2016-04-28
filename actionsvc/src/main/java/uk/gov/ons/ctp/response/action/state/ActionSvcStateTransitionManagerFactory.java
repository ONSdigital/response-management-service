package uk.gov.ons.ctp.response.action.state;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import uk.gov.ons.ctp.common.state.BasicStateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManagerFactory;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent;

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

    Map<ActionState, Map<ActionEvent, ActionState>> transitions = new HashMap<>();

    Map<ActionEvent, ActionState> transitionMapForSubmitted = new HashMap<>();
    transitionMapForSubmitted.put(ActionEvent.REQUEST_DISTRIBUTED, ActionState.PENDING);
    transitionMapForSubmitted.put(ActionEvent.REQUEST_CANCELLED, ActionState.ABORTED);
    transitions.put(ActionState.SUBMITTED, transitionMapForSubmitted);

    Map<ActionEvent, ActionState> transitionMapForPending = new HashMap<>();
    transitionMapForPending.put(ActionEvent.REQUEST_FAILED, ActionState.SUBMITTED);
    transitionMapForPending.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForPending.put(ActionEvent.REQUEST_ACCEPTED, ActionState.ACTIVE);
    transitionMapForPending.put(ActionEvent.REQUEST_COMPLETED, ActionState.COMPLETED);
    transitions.put(ActionState.PENDING, transitionMapForPending);

    Map<ActionEvent, ActionState> transitionMapForActive = new HashMap<>();
    transitionMapForActive.put(ActionEvent.REQUEST_FAILED, ActionState.SUBMITTED);
    transitionMapForActive.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForActive.put(ActionEvent.REQUEST_COMPLETED,
        ActionState.COMPLETED);
    transitions.put(ActionState.ACTIVE, transitionMapForActive);

    Map<ActionEvent, ActionState> transitionMapForCompleted = new HashMap<>();
    transitionMapForCompleted.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.COMPLETED);
    transitions.put(ActionState.COMPLETED, transitionMapForCompleted);

    Map<ActionEvent, ActionState> transitionMapForCancelSubmitted = new HashMap<>();
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_FAILED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_ACCEPTED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.REQUEST_COMPLETED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelSubmitted.put(ActionEvent.CANCELLATION_DISTRIBUTED,
        ActionState.CANCEL_PENDING);
    transitions.put(ActionState.CANCEL_SUBMITTED, transitionMapForCancelSubmitted);

    Map<ActionEvent, ActionState> transitionMapForCancelPending = new HashMap<>();
    transitionMapForCancelPending.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_FAILED,
        ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_ACCEPTED,
        ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.REQUEST_COMPLETED,
        ActionState.CANCEL_PENDING);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_FAILED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_ACCEPTED,
        ActionState.CANCELLING);
    transitionMapForCancelPending.put(ActionEvent.CANCELLATION_COMPLETED,
        ActionState.CANCELLED);
    transitions.put(ActionState.CANCEL_PENDING, transitionMapForCancelPending);

    Map<ActionEvent, ActionState> transitionMapForCancelling = new HashMap<>();
    transitionMapForCancelling.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCELLING);
    transitionMapForCancelling.put(ActionEvent.CANCELLATION_FAILED,
        ActionState.CANCEL_SUBMITTED);
    transitionMapForCancelling.put(ActionEvent.CANCELLATION_COMPLETED,
        ActionState.CANCELLED);
    transitions.put(ActionState.CANCELLING, transitionMapForCancelling);

    Map<ActionEvent, ActionState> transitionMapForCancelled = new HashMap<>();
    transitionMapForCancelled.put(ActionEvent.REQUEST_CANCELLED,
        ActionState.CANCELLED);
    transitions.put(ActionState.CANCELLED, transitionMapForCancelled);

    StateTransitionManager<ActionState, ActionEvent> actionStateTransitionManager =
        new BasicStateTransitionManager<>(transitions);

    managers.put(ACTION_ENTITY, actionStateTransitionManager);

  }

  /*
   * (non-Javadoc)
   * @see uk.gov.ons.ctp.response.action.state.StateTransitionManagerFactory#
   * getStateTransitionManager(java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public StateTransitionManager<?, ?> getStateTransitionManager(String entity) {
    return managers.get(entity);
  }

}

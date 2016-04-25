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
   * Create and init the factory with concrete StateTransitionManagers for each required entity
   */
  public ActionSvcStateTransitionManagerFactory() {
    managers = new HashMap<>();

    StateTransitionManager<ActionDTO.ActionState, ActionEvent> actionStateTransitionManager = new BasicStateTransitionManager<>();
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.SUBMITTED, ActionEvent.REQUEST_DISTRIBUTED,
        ActionDTO.ActionState.PENDING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.SUBMITTED, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.ABORTED);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.PENDING, ActionEvent.REQUEST_FAILED, ActionDTO.ActionState.SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.PENDING, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.PENDING, ActionEvent.REQUEST_ACCEPTED, ActionDTO.ActionState.ACTIVE);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.ACTIVE, ActionEvent.REQUEST_FAILED, ActionDTO.ActionState.SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.ACTIVE, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.ACTIVE, ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.COMPLETED);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.COMPLETED, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.COMPLETED);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_SUBMITTED, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_SUBMITTED, ActionEvent.REQUEST_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_SUBMITTED, ActionEvent.REQUEST_ACCEPTED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_SUBMITTED, ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_SUBMITTED, ActionEvent.CANCELLATION_DISTRIBUTED,
        ActionDTO.ActionState.CANCEL_PENDING);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCEL_PENDING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.REQUEST_FAILED,
        ActionDTO.ActionState.CANCEL_PENDING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.REQUEST_ACCEPTED,
        ActionDTO.ActionState.CANCEL_PENDING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.REQUEST_COMPLETED,
        ActionDTO.ActionState.CANCEL_PENDING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.CANCELLATION_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.CANCELLATION_ACCEPTED,
        ActionDTO.ActionState.CANCELLING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCEL_PENDING, ActionEvent.CANCELLATION_COMPLETED,
        ActionDTO.ActionState.CANCELLED);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCELLING, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCELLING);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCELLING, ActionEvent.CANCELLATION_FAILED,
        ActionDTO.ActionState.CANCEL_SUBMITTED);
    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCELLING, ActionEvent.CANCELLATION_COMPLETED,
        ActionDTO.ActionState.CANCELLED);

    actionStateTransitionManager.addTransition(ActionDTO.ActionState.CANCELLED, ActionEvent.REQUEST_CANCELLED,
        ActionDTO.ActionState.CANCELLED);

    managers.put(ACTION_ENTITY, actionStateTransitionManager);

  }

  /* (non-Javadoc)
   * @see uk.gov.ons.ctp.response.action.state.StateTransitionManagerFactory#getStateTransitionManager(java.lang.String)
   */
  @Override
  public StateTransitionManager<?, ?> getStateTransitionManager(String entity) {
    return managers.get(entity);
  }

}

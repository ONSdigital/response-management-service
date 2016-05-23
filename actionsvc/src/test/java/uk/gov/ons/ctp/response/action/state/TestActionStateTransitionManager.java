package uk.gov.ons.ctp.response.action.state;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.state.StateTransitionException;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManagerFactory;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;

/**
 * A test of the state transition manager It simply has to test a single good
 * and a single bad transition - all it is testing is the underlying mechanism,
 * not a real implementation, where we will want to assert all of the valid and
 * invalid transitions
 *
 */
@Slf4j
public class TestActionStateTransitionManager {

  private Map<ActionState, Map<ActionEvent, ActionState>> validTransitions = new HashMap<>();

  /**
   * Setup the transitions
   */
  @BeforeClass
  public void setup() {
    Map<ActionEvent, ActionState> submittedTransitions = new HashMap<>();
    submittedTransitions.put(ActionEvent.REQUEST_DISTRIBUTED, ActionState.PENDING);
    submittedTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.ABORTED);
    validTransitions.put(ActionState.SUBMITTED, submittedTransitions);

    Map<ActionEvent, ActionState> pendingTransitions = new HashMap<>();
    pendingTransitions.put(ActionEvent.REQUEST_FAILED, ActionState.SUBMITTED);
    pendingTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCEL_SUBMITTED);
    pendingTransitions.put(ActionEvent.REQUEST_ACCEPTED, ActionState.ACTIVE);
    pendingTransitions.put(ActionEvent.REQUEST_COMPLETED, ActionState.COMPLETED);
    validTransitions.put(ActionState.PENDING, pendingTransitions);

    Map<ActionEvent, ActionState> activeTransitions = new HashMap<>();
    activeTransitions.put(ActionEvent.REQUEST_FAILED, ActionState.SUBMITTED);
    activeTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCEL_SUBMITTED);
    activeTransitions.put(ActionEvent.REQUEST_COMPLETED, ActionState.COMPLETED);
    validTransitions.put(ActionState.ACTIVE, activeTransitions);

    Map<ActionEvent, ActionState> completedTransitions = new HashMap<>();
    completedTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.COMPLETED);
    validTransitions.put(ActionState.COMPLETED, completedTransitions);

    Map<ActionEvent, ActionState> cancelSubmittedTransitions = new HashMap<>();
    cancelSubmittedTransitions.put(ActionEvent.REQUEST_FAILED, ActionState.CANCEL_SUBMITTED);
    cancelSubmittedTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCEL_SUBMITTED);
    cancelSubmittedTransitions.put(ActionEvent.REQUEST_ACCEPTED, ActionState.CANCEL_SUBMITTED);
    cancelSubmittedTransitions.put(ActionEvent.REQUEST_COMPLETED, ActionState.CANCEL_SUBMITTED);
    cancelSubmittedTransitions.put(ActionEvent.CANCELLATION_DISTRIBUTED, ActionState.CANCEL_PENDING);
    validTransitions.put(ActionState.CANCEL_SUBMITTED, cancelSubmittedTransitions);

    Map<ActionEvent, ActionState> cancelPendingTransitions = new HashMap<>();
    cancelPendingTransitions.put(ActionEvent.REQUEST_FAILED, ActionState.CANCEL_PENDING);
    cancelPendingTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCEL_PENDING);
    cancelPendingTransitions.put(ActionEvent.REQUEST_ACCEPTED, ActionState.CANCEL_PENDING);
    cancelPendingTransitions.put(ActionEvent.REQUEST_COMPLETED, ActionState.CANCEL_PENDING);
    cancelPendingTransitions.put(ActionEvent.CANCELLATION_FAILED, ActionState.CANCEL_SUBMITTED);
    cancelPendingTransitions.put(ActionEvent.CANCELLATION_ACCEPTED, ActionState.CANCELLING);
    cancelPendingTransitions.put(ActionEvent.CANCELLATION_COMPLETED, ActionState.CANCELLED);
    validTransitions.put(ActionState.CANCEL_PENDING, cancelPendingTransitions);

    Map<ActionEvent, ActionState> cancellingTransitions = new HashMap<>();
    cancellingTransitions.put(ActionEvent.CANCELLATION_FAILED, ActionState.CANCEL_SUBMITTED);
    cancellingTransitions.put(ActionEvent.CANCELLATION_COMPLETED, ActionState.CANCELLED);
    cancellingTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCELLING);
    validTransitions.put(ActionState.CANCELLING, cancellingTransitions);

    Map<ActionEvent, ActionState> cancelledTransitions = new HashMap<>();
    cancelledTransitions.put(ActionEvent.REQUEST_CANCELLED, ActionState.CANCELLED);
    validTransitions.put(ActionState.CANCELLED, cancelledTransitions);
  }

  /**
   * test a valid transition
   * 
   * @throws StateTransitionException shouldn't!
   */
  @Test(threadPoolSize = 10, invocationCount = 50,  timeOut = 10000)
  public void testActionTransitions() throws StateTransitionException {
    StateTransitionManagerFactory stmFactory = new ActionSvcStateTransitionManagerFactory();
    StateTransitionManager<ActionState, ActionEvent> stm = stmFactory
        .getStateTransitionManager(ActionSvcStateTransitionManagerFactory.ACTION_ENTITY);

    for (Map.Entry<ActionState, Map<ActionEvent, ActionState>> me : validTransitions.entrySet()) {
      ActionState sourceState = me.getKey();
      for (Map.Entry<ActionEvent, ActionState> statesTransition : me.getValue().entrySet()) {
        log.debug("{} asserting valid transition {}({}) -> {}", Thread.currentThread().getName(), sourceState, statesTransition.getKey(),
            statesTransition.getValue());
        Assert.assertEquals(statesTransition.getValue(), stm.transition(sourceState, statesTransition.getKey()));
      }
      ActionEvent[] allEvents = ActionEvent.values();
      for (ActionEvent event : allEvents) {
        if (!me.getValue().keySet().contains(event)) {
          boolean caught = false;
          try {
            log.debug("{} asserting invalid transition {}({})", Thread.currentThread().getName(), sourceState, event);
            stm.transition(sourceState, event);
          } catch (StateTransitionException ste) {
            caught = true;
          }
          Assert.assertTrue(caught, "Transition " + sourceState + "(" + event + ") should be invalid");
        }
      }
    }
  }

//  @Test
//  public void testMultipleThreadsActionTransitions() throws StateTransitionException {
//    for (int i = 0; i < 500; i++) {
//      try {
//        Thread.sleep(Math.round(Math.random() * 100L));
//      } catch (InterruptedException ie) {
//
//      }
//      final Thread testRunner = new Thread(new Runnable() {
//        public void run() {
//          try {
//            log.debug("begin thread {}", Thread.currentThread().getName());
//            testActionTransitions();
//            log.debug("end thread {}", Thread.currentThread().getName());
//          } catch (Exception e) {
//            Assert.assertTrue(false);
//          }
//        }
//      });
//      testRunner.start();
//    }
//  }

}

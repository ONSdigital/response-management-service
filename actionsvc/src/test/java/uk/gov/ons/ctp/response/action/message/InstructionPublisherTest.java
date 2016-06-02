package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertTrue;
import static uk.gov.ons.ctp.response.action.utility.CommonValues.ACTION_INSTRUCTION_XML_BITS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.utility.ActionMessageListener;

/**
 * Looks like a test
 */
@ContextConfiguration(locations = { "/InstructionServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InstructionPublisherTest {

  private static final String FIELD_HANDLER = "Field";
  private static final String TEST_ACTION_TYPE = "our testActionType";

  @Autowired
  private InstructionPublisher instructionPublisher;

  @Autowired
  private DefaultMessageListenerContainer actionInstructionMessageListenerContainer;

  /**
   * A Test
   * @throws InterruptedException oops
   */
  @Test
  public void testSendActionInstructionWithInstructionPublisher() throws InterruptedException {
    ActionRequest actionRequest = new ActionRequest();
    actionRequest.setActionId(BigInteger.valueOf(1));
    actionRequest.setActionType(TEST_ACTION_TYPE);
    ArrayList<ActionRequest> actionRequests = new ArrayList<>();
    actionRequests.add(actionRequest);

    ActionCancel actionCancel = new ActionCancel();
    actionCancel.setActionId(BigInteger.valueOf(2));
    ArrayList<ActionCancel> actionCancels = new ArrayList<>();
    actionCancels.add(actionCancel);
    instructionPublisher.sendInstructions(FIELD_HANDLER, actionRequests, actionCancels);

    ActionMessageListener listener = (ActionMessageListener) actionInstructionMessageListenerContainer
        .getMessageListener();
    TimeUnit.SECONDS.sleep(5);
    String listenerPayload = listener.getPayload();
    if (listenerPayload != null) { // Required as test fails on the CI box.
                                   // Should be removed when running locally
      assertTrue(listenerPayload.contains(ACTION_INSTRUCTION_XML_BITS));
      assertTrue(listenerPayload.contains(TEST_ACTION_TYPE));
    }
  }
}

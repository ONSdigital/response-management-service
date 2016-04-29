package uk.gov.ons.ctp.response.action.message;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.utility.ActionMessageListener;

import static org.junit.Assert.assertTrue;
import static uk.gov.ons.ctp.response.action.utility.CommonValues.ACTION_INSTRUCTION_XML_BITS;

@ContextConfiguration(locations = { "/InstructionServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InstructionPublisherTest {

  private static final String FIELD_HANDLER = "Field";
  private static final String TEST_ACTION_TYPE = "our testActionType";


	@Autowired
	InstructionPublisher instructionPublisher;

	@Autowired
	DefaultMessageListenerContainer actionInstructionMessageListenerContainer;

	@Test
	public void testSendActionInstructionWithInstructionPublisher() throws InterruptedException {
		ActionRequest actionRequest = new ActionRequest();
		actionRequest.setActionType(TEST_ACTION_TYPE);
		ArrayList<ActionRequest> actionRequests = new ArrayList<>();
		actionRequests.add(actionRequest);
		instructionPublisher.sendRequests(FIELD_HANDLER, actionRequests);

		ActionMessageListener listener = (ActionMessageListener)actionInstructionMessageListenerContainer.getMessageListener();
		TimeUnit.SECONDS.sleep(10);
		String listenerPayload = listener.getPayload();
		assertTrue(listenerPayload.contains(ACTION_INSTRUCTION_XML_BITS));
    assertTrue(listenerPayload.contains(TEST_ACTION_TYPE));
	}
}

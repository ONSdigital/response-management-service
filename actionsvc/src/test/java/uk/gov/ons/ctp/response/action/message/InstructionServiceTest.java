package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.utility.ActionMessageListener;

@ContextConfiguration(locations = { "/InstructionServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InstructionServiceTest {

	@Before
	public void setUp() throws Exception {
		myMessageListener.setPayload("no payload: " + System.currentTimeMillis());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Autowired
	InstructionPublisher instructionService;

	@Autowired
	MessageChannel instructionOutbound;

	@Autowired
	QueueChannel testInstructionOutbound;

	@Autowired
	QueueChannel testInstructionXml;

	@Autowired
	ActionMessageListener myMessageListener;

	@Autowired
	DefaultMessageListenerContainer jmsContainer;
	
	// just required for debugging - remove later
	@Autowired
	CachingConnectionFactory connectionFactory;


	@Test
	public void testSendRequestViaInstructionService() {
		try {
			String handler = "Field";

			ActionRequest actionRequest = new ActionRequest();
			actionRequest.setActionType("testActionType");
			ArrayList<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
			actionRequests.add(actionRequest);

			ActionInstruction actionInstruction = instructionService.sendRequests(handler, actionRequests);

			// test if instructionOutboundMessage channel has an
			// ActionInstruction payload
			Message<?> instructionOutboundMessage = testInstructionOutbound.receive(0);
			assertNotNull("instructionOutboundMessage should not be null", instructionOutboundMessage);
			String payload = instructionOutboundMessage.getPayload().toString();
			System.out.println("instructionOutboundMessage: " + payload);
			assertTrue("instructionOutboundMessage message missing content",
					payload.contains("uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction"));
			

		} catch (Exception ex) {
			fail("testSendRequestViaInstructionService has failed " + ex.getMessage());
		}

	}

	@Test
	public void testSendRequestViaInstructionServiceConsumeMessage() {
		try {
			String handler = "Field";

			ActionRequest actionRequest = new ActionRequest();
			actionRequest.setActionType("testActionType");
			ArrayList<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
			actionRequests.add(actionRequest);

			// some debugging
			String destinationName = jmsContainer.getDestinationName();
			System.out.println("DestinationName: " + destinationName);
			
			Object obj = connectionFactory.getTargetConnectionFactory();
			System.out.println("TargetConnectionFactory: " + obj.toString());
			
			String myMessageListenerPayload = myMessageListener.getPayload();
			System.out.println("1 - myMessageListenerPayload: " + myMessageListenerPayload);
			
			ActionInstruction actionInstruction = instructionService.sendRequests(handler, actionRequests);

			ActionMessageListener listener = (ActionMessageListener)jmsContainer.getMessageListener();
			String listenerPayload = listener.getPayload();
			if (listener.equals(myMessageListener)) {
				System.out.println("3a - The two listeners are the same");
			}
			System.out.println("3b -ListenerPayload: " + listenerPayload);
			
			// wait for a few seconds
			TimeUnit.SECONDS.sleep(5);

			ActionMessageListener listener2 = (ActionMessageListener)jmsContainer.getMessageListener();
			String listenerPayload2 = listener2.getPayload();
			if (listener2.equals(myMessageListener)) {
				System.out.println("4a - The two listeners are the same");
			}
			System.out.println("4aa - ListenerPayload: " + listenerPayload2);
			
			// test ActiveMQ message generated with expected content
			myMessageListenerPayload = myMessageListener.getPayload();
			System.out.println("4b - myMessageListenerPayload: " + myMessageListenerPayload);
			assertEquals("MessageListenerPayload not found", "message found", myMessageListenerPayload);

		} catch (Exception ex) {
			fail("testSendRequestViaInstructionServiceConsumeMessage has failed " + ex.getMessage());
		}

	}
}

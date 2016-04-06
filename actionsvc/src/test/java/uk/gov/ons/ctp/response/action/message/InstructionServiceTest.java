package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;

@ContextConfiguration(locations = { "/InstructionServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InstructionServiceTest {

	public InstructionService instructionService = new InstructionService();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Autowired
	MessageChannel instructionOutbound;

	@Autowired
	QueueChannel testInstructionFieldChannel;

	@Autowired
	ActiveMQQueue fieldInstructionQueue;

	@Test
	public void testCreateOutBoundMessageToFieldHandler() {
		try {

			ActionInstruction instruction = new ActionInstruction();
			ActionRequest request = new ActionRequest();
			request.setActionType("testActionType");
			ActionRequests requests = new ActionRequests();
			requests.getActionRequests().add(request);
			instruction.setActionRequests(requests);

			instructionOutbound.send(MessageBuilder.withPayload(instruction).setHeader("HANDLER", "Field").build());

			Message<?> outMessage = testInstructionFieldChannel.receive(0);
			assertNotNull("outMessage should not be null", outMessage);
			// outMesage: <?xml version="1.0" encoding="UTF-8"
			// standalone="no"?>
			// //<ns2:actionInstruction
			// xmlns:ns2="http://ons.gov.uk/ctp/response/action/message/instruction">
			// //<actionRequests><actionRequest>
			// //<actionType>testActionType</actionType>
			// //</actionRequest></actionRequests>
			// //</ns2:actionInstruction>
			boolean payLoadContainsAdaptor = outMessage.getPayload().toString()
					.contains("<actionType>testActionType</actionType>");
			assertTrue("Payload does not contain reference to <actionType>testActionType</actionType>",
					payLoadContainsAdaptor);
			outMessage = testInstructionFieldChannel.receive(0);
			assertNull("Only one message expected from feedbackTransformed", outMessage);

			String queueName = fieldInstructionQueue.getQueueName();
			System.out.println("Queue name: " + queueName);
			Properties props = fieldInstructionQueue.getProperties();
			Set keys = props.keySet();
			Iterator itr = keys.iterator();
			while (itr.hasNext()) {
				String key = (String) itr.next();
				System.out.println("Key: " + key);
				System.out.println("value: " + props.getProperty(key));
			}

		} catch (Exception ex) {
			fail("testCreateOutBoundMessageToFieldHandler has failed " + ex.getMessage());
		}

	}

	@Test
	public void testSendRequest() {
		try {
			String handler = "Field";
			String actionType = "testActionType";
			instructionService.sendRequest(handler, actionType);

		} catch (Exception ex) {
			fail("testSendRequest has failed " + ex.getMessage());
		}

	}

}

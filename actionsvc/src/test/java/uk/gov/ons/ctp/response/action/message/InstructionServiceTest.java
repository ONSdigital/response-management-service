package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertNotNull;
import uk.gov.ons.ctp.response.action.message.impl.InstructionPublisherImpl;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.integration.test.matcher.HeaderMatcher.hasHeader;

import java.util.ArrayList;

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

	@Before
	public void setUp() throws Exception {
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
	
	@Test
	public void testCreateOutBoundMessageToFieldHandler() {
		try {

			ActionInstruction instruction = new ActionInstruction();
		    
			ActionRequest actionRequest = new ActionRequest();
			actionRequest.setActionType("testActionType");									
		    ActionRequests requests = new ActionRequests();
		    requests.getActionRequests().add(actionRequest);
		    instruction.setActionRequests(requests);
						
			instructionOutbound.send(MessageBuilder.withPayload(instruction).setHeader("HANDLER", "Field").build());

			Message<?> instructionXmlMessage = testInstructionXml.receive(0);
			assertNotNull("instructionXmlMessage should not be null", instructionXmlMessage);
			System.out.println("instructionXmlMessage: " + instructionXmlMessage);

			// test instructionXml payload contains <actionType> value testActionType
			boolean payLoadContainsAdaptor = instructionXmlMessage.getPayload().toString()
					.contains("<actionType>testActionType</actionType>");
			assertTrue("Payload does not contain reference to <actionType>testActionType</actionType>",
					payLoadContainsAdaptor);
			assertThat(instructionXmlMessage, hasHeader("HANDLER", "Field"));

			instructionXmlMessage = testInstructionXml.receive(0);
			assertNull("Only one message expected from instructionXml", instructionXmlMessage);
			
		
		} catch (Exception ex) {
			fail("testCreateOutBoundMessageToFieldHandler has failed " + ex.getMessage());
		}
	}

	@Test
	public void testSendRequestViaInstructionService() {
		try {
			String handler = "Field";
			
			ActionRequest actionRequest = new ActionRequest();
			actionRequest.setActionType("testActionType");			
			ArrayList<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
			actionRequests.add(actionRequest);
			
			ActionInstruction actionInstruction = instructionService.sendRequests(handler, actionRequests);
			
			// test if instructionOutboundMessage channel has an ActionInstruction payload
			Message<?> instructionOutboundMessage = testInstructionOutbound.receive(0);
			assertNotNull("instructionOutboundMessage should not be null", instructionOutboundMessage);
			String payload = instructionOutboundMessage.getPayload().toString();
			System.out.println("instructionOutboundMessage: " + payload);			
			assertTrue("instructionOutboundMessage message missing content", payload.contains("uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction"));
			
			
			// TODO
			// test ActiveMQ message generated with expected content
						
		} catch (Exception ex) {
			fail("testSendRequestViaInstructionService has failed " + ex.getMessage());
		}

	}

}

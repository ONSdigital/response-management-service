package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertNotNull;

import uk.gov.ons.ctp.response.action.message.impl.ActionMessageListener;
import uk.gov.ons.ctp.response.action.message.impl.InstructionPublisherImpl;
import uk.gov.ons.ctp.response.action.message.impl.ReceiverDelegate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.integration.test.matcher.HeaderMatcher.hasHeader;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
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
	
	@Autowired
	ActiveMQQueue fieldInstructionQueue;	
	
//	@Autowired 
//	CachingConnectionFactory connectionFactory;
	
//	@Autowired
//	DefaultMessageListenerContainer fieldInstructionQueueMessageListenerContainer;
	
//	@Autowired
//	ReceiverDelegate myMessageReceiverDelegate;
	
	@Autowired
	ActionMessageListener myMessageListener;
	
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
	
	
	@Test
	public void testSendRequestViaInstructionServiceConsumeMessage() {
		try {
			String handler = "Field";
			
			ActionRequest actionRequest = new ActionRequest();
			actionRequest.setActionType("testActionType");			
			ArrayList<ActionRequest> actionRequests = new ArrayList<ActionRequest>();
			actionRequests.add(actionRequest);
			
			ActionInstruction actionInstruction = instructionService.sendRequests(handler, actionRequests);
			
			
			
			// TODO
			// test ActiveMQ message generated with expected content
			
//			<bean id="connectionFactory" class="org.springframework.jms.connection.CachingConnectionFactory">
//		    <property name="targetConnectionFactory">
//		      <bean class="org.apache.activemq.ActiveMQConnectionFactory">
//		        <property name="brokerURL" value="tcp://localhost:61616" />
//		    </property>
//		    <property name="sessionCacheSize" value="10"/>
//		    </bean>
		  
//			CachingConnectionFactory factory = new CachingConnectionFactory();
//			ActiveMQConnectionFactory activemqFactory = new ActiveMQConnectionFactory();			
//			factory.setTargetConnectionFactory(activemqFactory);
//			Connection connection = factory.createConnection();
//				        
//			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//			
//			connection.start();
			
//			Connection connection = connectionFactory.createConnection();
//			Session session = connectionFactory.getTargetConnectionFactory().createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
//			Session session = connectionFactory.createQueueConnection().createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			
//			QueueConnection queueConnection = connectionFactory.createQueueConnection();
//			Session session = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
//			QueueBrowser browser = session.createBrowser((Queue) fieldInstructionQueue);
//	        Enumeration enumeration = browser.getEnumeration();
//	        // OR
//			MessageConsumer consumer = session.createConsumer(fieldInstructionQueue);			
//			Object obj = consumer.receive(0);
//			
//			queueConnection.close();
			
			
//			Object obj = feedbackMessageListenerContainer.getMessageListener();
			
//			String messagePayload = myMessageReceiverDelegate.getPayload();
//			System.out.print("messagePayload2: " + messagePayload);
			
			
			String myMessageListenerPayload = myMessageListener.getPayload();
			System.out.println("myMessageListenerPayload: " + myMessageListenerPayload);
					
						
		} catch (Exception ex) {
			fail("testSendRequestViaInstructionServiceConsumeMessage has failed " + ex.getMessage());
		}

	}
}

package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.impl.FeedbackReceiverImpl;
import uk.gov.ons.ctp.response.action.utility.ActionMessageListener;

@ContextConfiguration(locations = { "/FeedbackServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedbackServiceTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Autowired
	MessageChannel feedbackXml;

	@Autowired
	@Qualifier("feedbackUnmarshaller")
	Jaxb2Marshaller feedbackMarshaller;

	@Autowired
	QueueChannel feedbackXmlInvalid;

	@Autowired
	FeedbackReceiver feedbackService;
	
	@Test
	public void testSendactiveMQMessage() {
		try {

			String testMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<p:actionFeedback xmlns:p=\"http://ons.gov.uk/ctp/response/action/message/feedback\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ons.gov.uk/ctp/response/action/message/feedback inboundMessage.xsd \">"
					+ "<actionId>1</actionId>" + "<situation>situation</situation>" + "<isComplete>true</isComplete>"
					+ "<isFailed>true</isFailed>" + "<notes>notes</notes>" + "</p:actionFeedback>";

			feedbackXml.send(MessageBuilder.withPayload(testMessage).build());

			String jaxbContext = feedbackMarshaller.getJaxbContext().toString();
			assertTrue("Marshaller JAXB context does not contain reference to ActionFeedback",
					jaxbContext.contains("uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback"));

			// expect FeedbackService.acceptFeedback() to have been implemented

		} catch (Exception ex) {
			fail("testSendctiveMQMessage has failed " + ex.getMessage());
		}

	}

	@Test
	public void testSendactiveMQInvalidMessage() {
		try {

			FileUtils.cleanDirectory(new File("/var/log/ctp/responsemanagement/actionsvc/feedback"));
		    
			String testMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<p:actionFeedbackWrong xmlns:p=\"http://ons.gov.uk/ctp/response/action/message/feedback\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ons.gov.uk/ctp/response/action/message/feedback inboundMessage.xsd \">"
					+ "<actionId>1</actionId>" + "<situation>situation</situation>" + "<isComplete>true</isComplete>"
					+ "<isFailed>true</isFailed>" + "<notes>notes</notes>" + "</p:actionFeedbackWrong>";

			feedbackXml.send(MessageBuilder.withPayload(testMessage).build());
			
			// expect one file to be added to the log folder
			File logDir = new File("/var/log/ctp/responsemanagement/actionsvc/feedback");
			File[] files = logDir.listFiles();
			assertEquals("More of less than one file added to the log", files.length, 1);


		} catch (Exception ex) {
			fail("testSendactiveMQInvalidMessage has failed " + ex.getMessage());
		}

	}
}

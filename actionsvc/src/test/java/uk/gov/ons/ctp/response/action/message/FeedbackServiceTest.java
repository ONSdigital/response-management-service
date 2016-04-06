package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.integration.test.matcher.HeaderMatcher.hasHeaderKey;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration (locations = { "/FeedbackServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedbackServiceTest {
  
  public FeedbackService feedbackService = new FeedbackService();

  @Before
  public void setUp() throws Exception {
    System.out.println("Test");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Autowired
  MessageChannel feedbackXml;

  @Autowired
  QueueChannel testChannel;

  @Autowired
  @Qualifier("feedbackUnmarshaller")
  Jaxb2Marshaller feedbackMarshaller;

  @Test
  public void testSendctiveMQMessage() {
    try {

      String testMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<p:actionFeedback xmlns:p=\"http://ons.gov.uk/ctp/response/action/message/feedback\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ons.gov.uk/ctp/response/action/message/feedback inboundMessage.xsd \">" +
        "<actionId>1</actionId>" +
        "<situation>situation</situation>" +
        "<isComplete>true</isComplete>" +
        "<isFailed>true</isFailed>" +
        "<notes>notes</notes>" +
        "</p:actionFeedback>";

      feedbackXml.send(MessageBuilder.withPayload(testMessage).build());

      Message<?> outMessage = testChannel.receive(0);
      assertNotNull("outMessage should not be null", outMessage);
      boolean payLoadContainsAdaptor = outMessage.getPayload().toString().contains("uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback");
      assertTrue("Payload does not contain reference to ActionFeedback adaptor", payLoadContainsAdaptor);
      assertThat(outMessage, hasHeaderKey("timestamp"));
      assertThat(outMessage, hasHeaderKey("id"));
      outMessage = testChannel.receive(0);
      assertNull("Only one message expected from feedbackTransformed", outMessage);


      String contextPath = feedbackMarshaller.getContextPath();
      String jaxbContext = feedbackMarshaller.getJaxbContext().toString();

      System.out.println("Marshaller context path: " + contextPath);
      System.out.println("Marshaller jaxbContext: " + jaxbContext);

    }
    catch (Exception ex) {
      fail("testSendctiveMQMessage has failed " + ex.getMessage());
    }

  }

}

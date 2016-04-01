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
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.integration.samples.testing.gateway.VoidGateway;

//@ContextConfiguration (locations = { "/integration-context.xml" })  // commented out as using this configuration generates an error on startup
//@ContextConfiguration (locations = { "FeedbackServiceTest-context.xml" })
//@RunWith(SpringJUnit4ClassRunner.class)
public class FeedbackServiceTest {

  @Before
  public void setUp() throws Exception {
    // TODO mock ActiveMQ or embedded ActiveMQ required
  }

  @After
  public void tearDown() throws Exception {
  }

//  @Autowired
//  MessageChannel feedbackXml;
//
//  @Autowired
//  MessageChannel feedbackTransformed;
//  
//  @Autowired
//  QueueChannel feedbackXmlValid;
////  QueueChannel feedbackTransformed;
////  SubscribableChannel feedbackTransformed;
////  MessageChannel feedbackTransformed; 
////  PollableChannel feedbackTransformed;
////  VoidGateway feedbackTransformed;
//  
  @Test
  public void testOne() {
    try {
      
      System.out.println("testOne does nothing");
      
//      String testMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//        "<p:actionFeedback xmlns:p=\"http://ons.gov.uk/ctp/response/action/message/feedback\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ons.gov.uk/ctp/response/action/message/feedback inboundMessage.xsd \">" +
//        "<actionId>1</actionId>" +
//        "<situation>situation</situation>" + 
//        "<isComplete>true</isComplete>" +
//        "<isFailed>true</isFailed>" + 
//        "<notes>notes</notes>" + 
//        "</p:actionFeedback>";
//      
//      feedbackXml.send(MessageBuilder.withPayload(testMessage).build());
//      Message<?> outMessage = feedbackXmlValid.receive(0);
//      assertNotNull("outMessage should not be null", outMessage);
//      boolean payLoadContainsAdaptor = outMessage.getPayload().toString().contains("uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback");
//      assertTrue("Payload does not contain reference to ActionFeedback adaptor", payLoadContainsAdaptor);
//      assertThat(outMessage, hasHeaderKey("timestamp"));
//      assertThat(outMessage, hasHeaderKey("id"));
//      outMessage = feedbackXmlValid.receive(0);
//      assertNull("Only one message expected", outMessage);  
      

    }
    catch (Exception ex) {
      fail("testOne has failed " + ex.getMessage());
    }
    
  }

}

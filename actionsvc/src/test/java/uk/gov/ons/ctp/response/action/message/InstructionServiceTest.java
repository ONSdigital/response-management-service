package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.integration.test.matcher.HeaderMatcher.hasHeaderKey;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.activemq.command.ActiveMQQueue;
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
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;

@ContextConfiguration(locations = { "InstructionServiceTest-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class InstructionServiceTest {

  public InstructionService instructionService = new InstructionService();

  @Before
  public void setUp() throws Exception {
    System.out.println("Test");
  }

  @After
  public void tearDown() throws Exception {
  }

  @Autowired
  MessageChannel instructionOutbound;

  @Autowired
  QueueChannel testChannel;

//  @Header("HANDLER") String handler;
  
  @Test
  public void testCreateOutBoundMessage() {
    try {

      
      ActionInstruction instruction = new ActionInstruction();
      ActionRequest request = new ActionRequest();      
      request.setActionType("Field");
      ActionRequests requests = new ActionRequests();
      requests.getActionRequests().add(request);
      instruction.setActionRequests(requests);
      

//      instructionOutbound.send(MessageBuilder.withPayload(instruction).build());
//
//      Message<?> outMessage = testChannel.receive(0);
//      assertNotNull("outMessage should not be null", outMessage);
//      boolean payLoadContainsAdaptor = outMessage.getPayload().toString()
//          .contains("uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback");
//      assertTrue("Payload does not contain reference to ActionFeedback adaptor", payLoadContainsAdaptor);
//      assertThat(outMessage, hasHeaderKey("timestamp"));
//      assertThat(outMessage, hasHeaderKey("id"));
//      outMessage = testChannel.receive(0);
//      assertNull("Only one message expected from feedbackTransformed", outMessage);

    } catch (Exception ex) {
      fail("testCreateOutBoundMessage has failed " + ex.getMessage());
    }

  }

}

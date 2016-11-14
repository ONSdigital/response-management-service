package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * What have we here - oh - another test. Test we can receive feedback
 * @author centos
 *
 */
@ContextConfiguration(locations = {"/springintegration/FeedbackServiceTest-context.xml"})
@TestPropertySource("classpath:/application-test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class ActionFeedbackReceiverTest {

  @Inject
  private MessageChannel actionFeedbackXml;

  @Inject
  @Qualifier("actionFeedbackUnmarshaller")
  private Jaxb2Marshaller actionFeedbackUnmarshaller;

  private static final String PACKAGE_ACTION_FEEDBACK
     = "uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback";

  @Before
  public void setUpAndInitialVerification() throws Exception {
    String jaxbContext = actionFeedbackUnmarshaller.getJaxbContext().toString();
    assertTrue(jaxbContext.contains(PACKAGE_ACTION_FEEDBACK));
  }

  /**
   * A Test
   */
  @Test
  public void testSendActiveMQValidMessage() {
    String testMessage = "<feed:actionFeedback xmlns:feed=\"http://ons.gov.uk/ctp/response/action/message/feedback\">\n"
        +
        "  <actionId>201</actionId>\n"
        + "  <situation>string</situation>\n"
        + "  <outcome>CANCELLATION_FAILED</outcome>\n"
        + "  <notes>string</notes>\n"
        + "</feed:actionFeedback>";
    // TODO actionFeedbackXml.send(MessageBuilder.withPayload(testMessage).build());

    // TODO Check no msg on Invalid queue

    /**
     * The message above is picked up by ActionFeedbackReceiverImpl. This can be
     * verified putting a debug point at the entrance of acceptFeedback.
     */
    // TODO further assertions once acceptFeedback has been implemented
  }

  /**
   * A Test
   * @throws IOException darn it
   */
  @Test
  public void testSendActiveMQInvalidMessage() throws IOException {
    String testMessage = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<p:actionFeedbackWrong xmlns:p=\"http://ons.gov.uk/ctp/response/action/message/feedback\""
        + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
        + " xsi:schemaLocation=\"http://ons.gov.uk/ctp/response/action/message/feedback inboundMessage.xsd \">"
        + "<actionId>1</actionId>" + "<situation>situation</situation>" + "<isComplete>true</isComplete>"
        + "<isFailed>true</isFailed>" + "<notes>notes</notes>" + "</p:actionFeedbackWrong>";

    // TODO actionFeedbackXml.send(MessageBuilder.withPayload(testMessage).build());

    // TODO Check msg ends up on Invalid queue
  }
}

package uk.gov.ons.ctp.response.action.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * What have we here - oh - another test. Test we can receive feedback
 * @author centos
 *
 */
@ContextConfiguration(locations = { "/FeedbackServiceTest-context.xml" })
@TestPropertySource("classpath:/application-test.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class FeedbackReceiverTest {

  @Autowired
  private MessageChannel feedbackXml;

  @Autowired
  @Qualifier("feedbackUnmarshaller")
  private Jaxb2Marshaller feedbackUnmarshaller;

  private static final String INVALID_ACTION_FEEDBACK_LOG_DIRECTORY_NAME
    = "/tmp/ctp/logs/actionsvc/feedback";

  private static final String PACKAGE_ACTION_FEEDBACK
     = "uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback";

  /**
   * A Test
   * @throws Exception oops
   */
  @Before
  public void setUpAndInitialVerification() throws Exception {
    
    File logDir = new File(INVALID_ACTION_FEEDBACK_LOG_DIRECTORY_NAME);
    if (!logDir.exists()) {
      logDir.mkdir();
    }
    FileUtils.cleanDirectory(logDir);
    File[] files = logDir.listFiles();
    assertEquals(0, files.length);

    String jaxbContext = feedbackUnmarshaller.getJaxbContext().toString();
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
    feedbackXml.send(MessageBuilder.withPayload(testMessage).build());

    File logDir = new File(INVALID_ACTION_FEEDBACK_LOG_DIRECTORY_NAME);
    File[] files = logDir.listFiles();
    assertEquals(0, files.length); // This validates the xml testMessage was
                                   // deemed OK.

    /**
     * The message above is picked up by FeedbackReceiverImpl. This can be
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

    feedbackXml.send(MessageBuilder.withPayload(testMessage).build());

    // expect one file to be added to the log folder
    File logDir = new File(INVALID_ACTION_FEEDBACK_LOG_DIRECTORY_NAME);
    File[] files = logDir.listFiles();
    assertEquals(1, files.length);
  }
}

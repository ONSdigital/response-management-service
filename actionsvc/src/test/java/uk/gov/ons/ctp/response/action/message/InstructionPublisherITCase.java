package uk.gov.ons.ctp.response.action.message;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.w3c.dom.Document;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.message.JmsHelper;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.utility.ActionMessageListener;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InstructionPublisherITCaseConfig.class)
public class InstructionPublisherITCase {

  private static final String FIELD_HANDLER = "Field";

  @Inject
  private InstructionPublisher instructionPublisher;

  @Inject
  private DefaultMessageListenerContainer actionInstructionMessageListenerContainer;

  @Inject
  CachingConnectionFactory connectionFactory;

  @Inject
  MessageChannel instructionXml;

  private Connection connection;
  private int initialCounter;

  private static final String INVALID_ACTION_INSTRUCTIONS_QUEUE = "Action.InvalidActionInstructions";
  private static final String TEST = "test";

  @Before
  public void setUp() throws Exception {
    connection = connectionFactory.createConnection();
    connection.start();
    initialCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_ACTION_INSTRUCTIONS_QUEUE);

    ActionMessageListener listener = (ActionMessageListener) actionInstructionMessageListenerContainer.getMessageListener();
    listener.setPayload(null);
  }

  @After
  public void finishCleanly() throws JMSException {
    connection.close();
  }

  @Test
  public void testPublishValidActionInstruction() throws InterruptedException, JMSException {
    ArrayList<ActionRequest> actionRequests = new ArrayList<>();
    actionRequests.add(buildValidActionRequest());

    ArrayList<ActionCancel> actionCancels = new ArrayList<>();
    actionCancels.add(buildValidActionCancel());

    instructionPublisher.sendInstructions(FIELD_HANDLER, actionRequests, actionCancels);

    /**
     * We check that no additional message has been put on the xml invalid queue
     */
    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_ACTION_INSTRUCTIONS_QUEUE);
    assertEquals(initialCounter, finalCounter);

    /**
     * The section below verifies that an ActionInstruction ends up on the queue
     */
    ActionMessageListener listener = (ActionMessageListener) actionInstructionMessageListenerContainer.getMessageListener();
    TimeUnit.SECONDS.sleep(5);
    String payload = listener.getPayload();

    Document doc = parse(payload);
    assertThat(doc, hasXPath("/actionInstruction/actionRequests/actionRequest[1]/actionPlan", equalTo(TEST)));
    assertThat(doc, hasXPath("/actionInstruction/actionCancels/actionCancel[1]/reason", equalTo(TEST)));
  }

  @Test
  public void testPublishInvalidActionInstruction() throws IOException, JMSException {
    String testMessage = FileUtils.readFileToString(provideTempFile("/xmlSampleFiles/invalidActionInstruction.xml.txt"), "UTF-8");
    instructionXml.send(org.springframework.messaging.support.MessageBuilder.withPayload(testMessage).build());

    /**
     * We check that the invalid xml ends up on the invalid queue.
     */
    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_ACTION_INSTRUCTIONS_QUEUE);
    assertEquals(1, finalCounter - initialCounter);
  }

  /**
   * Create XML Document from String message on queue
   * @param xmlMessage XML String
   * @return doc Document
   */
  private Document parse(String xmlMessage) {
    Document doc = null;
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(false);
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      doc = documentBuilder.parse(new ByteArrayInputStream(xmlMessage.getBytes()));
    } catch (Exception ex) {
      log.error("Error parsing Published XML", ex.getMessage());
    }
    return doc;
  }

  private File provideTempFile(String inputStreamLocation) throws IOException {
    InputStream is = getClass().getResourceAsStream(inputStreamLocation);
    File tempFile = File.createTempFile("prefix","suffix");
    tempFile.deleteOnExit();
    FileOutputStream out = new FileOutputStream(tempFile);
    IOUtils.copy(is, out);
    return tempFile;
  }

  private ActionRequest buildValidActionRequest() {
    ActionRequest actionRequest = new ActionRequest();
    actionRequest.setActionId(BigInteger.valueOf(1));
    actionRequest.setActionPlan(TEST);
    actionRequest.setActionType(TEST);
    actionRequest.setQuestionSet(TEST);
    actionRequest.setCaseId(Integer.valueOf(1));
    actionRequest.setCaseRef(TEST);
    actionRequest.setIac(TEST);
    ActionAddress actionAddress = new ActionAddress();
    actionAddress.setUprn(BigInteger.valueOf(123));
    actionAddress.setPostcode(TEST);
    actionAddress.setLatitude(BigDecimal.TEN);
    actionAddress.setLongitude(BigDecimal.TEN);
    actionRequest.setAddress(actionAddress);
    actionRequest.setEvents(new ActionEvent());
    actionAddress.setLadCode(TEST);
    return actionRequest;
  }

  private ActionCancel buildValidActionCancel() {
    ActionCancel actionCancel = new ActionCancel();
    actionCancel.setActionId(BigInteger.valueOf(2));
    actionCancel.setReason(TEST);
    return actionCancel;
  }
}

package uk.gov.ons.ctp.response.action.message;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.JMSException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.junit4.SpringRunner;

import uk.gov.ons.ctp.common.message.JmsHelper;
import uk.gov.ons.ctp.response.action.service.CaseNotificationService;

/**
 * Test focusing on Spring Integration
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CaseNotificationReceiverITCaseConfig.class)
public class CaseNotificationReceiverITCase {

  @Inject
  private MessageChannel caseLifecycleEventsXml;

  @Inject
  private MessageChannel testOutbound;

  @Inject
  private QueueChannel activeMQDLQXml;

  @Inject
  @Qualifier("caseNotificationUnmarshaller")
  private Jaxb2Marshaller caseNotificationUnmarshaller;

  @Inject
  private CachingConnectionFactory connectionFactory;

  @Inject
  private CaseNotificationService caseNotificationService;

  private Connection connection;
  private int initialCounter;

  private static final int RECEIVE_TIMEOUT = 5000;
  private static final String INVALID_CASE_NOTIFICATIONS_QUEUE = "Case.InvalidCaseNotifications";
  private static final String PACKAGE_CASE_NOTIFICATION = "uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotification";

  @Before
  public void setUp() throws Exception {
    connection = connectionFactory.createConnection();
    connection.start();
    initialCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_CASE_NOTIFICATIONS_QUEUE);

    String jaxbContext = caseNotificationUnmarshaller.getJaxbContext().toString();
    assertTrue(jaxbContext.contains(PACKAGE_CASE_NOTIFICATION));

    activeMQDLQXml.clear();

    reset(caseNotificationService);
  }

  @After
  public void finishCleanly() throws JMSException {
    connection.close();
  }

  @Test
  public void testReceivingCaseNotificationInvalidXml() throws IOException, JMSException {
    String testMessage = FileUtils.readFileToString(provideTempFile("/xmlSampleFiles/invalidCaseNotification.xml.txt"), "UTF-8");

    caseLifecycleEventsXml.send(org.springframework.messaging.support.MessageBuilder.withPayload(testMessage).build());

    /**
     * We check that the invalid xml ends up on the invalid queue.
     */
    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_CASE_NOTIFICATIONS_QUEUE);
    assertEquals(1, finalCounter - initialCounter);
  }

  @Test
  public void testReceivingCaseNotificationXmlBadlyFormed() throws IOException, JMSException {
    String testMessage = FileUtils.readFileToString(provideTempFile("/xmlSampleFiles/badlyFormedCaseNotification.xml.txt"), "UTF-8");
    testOutbound.send(org.springframework.messaging.support.MessageBuilder.withPayload(testMessage).build());

    // TODO The below works in an IDE but fails on the command line.
    // TODO Exact same code than ActionInstructionReceiverImplITCase in rm-notify-gateway so?
//    /**
//     * We check that the badly formed xml ends up on the dead letter queue.
//     */
//    Message<?> message = activeMQDLQXml.receive(RECEIVE_TIMEOUT);
//    String payload = (String) message.getPayload();
//    assertEquals(testMessage, payload);

    /**
     * We check that no badly formed xml ends up on the invalid queue.
     */
    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_CASE_NOTIFICATIONS_QUEUE);
    assertEquals(0, finalCounter - initialCounter);
  }

//  @SuppressWarnings("unchecked")
//  @Test
//  public void testReceivingCaseNotificationValidXml() throws InterruptedException, IOException, JMSException {
//    // Set up CountDownLatch for synchronisation with async call
//    final CountDownLatch caseNotificationServiceInvoked = new CountDownLatch(1);
//    // Release all waiting threads when mock caseNotificationService.acceptFeedback method is called
//    doAnswer(countsDownLatch(caseNotificationServiceInvoked)).when(caseNotificationService).acceptNotification(anyList());
//
//    String testMessage = FileUtils.readFileToString(provideTempFile("/xmlSampleFiles/validCaseNotification.xml.txt"), "UTF-8");
//    testOutbound.send(org.springframework.messaging.support.MessageBuilder.withPayload(testMessage).build());
//
//    // Await synchronisation with the asynchronous message call
//    caseNotificationServiceInvoked.await(RECEIVE_TIMEOUT, MILLISECONDS);
//
//    /**
//     * We check that no xml ends up on the invalid queue.
//     */
//    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_CASE_NOTIFICATIONS_QUEUE);
//    assertEquals(initialCounter, finalCounter);
//
//    /**
//     * We check that no xml ends up on the dead letter queue.
//     */
//    Message<?> message = activeMQDLQXml.receive(RECEIVE_TIMEOUT);
//    assertNull(message);
//
//    /**
//     * We check the message was processed
//     */
//    verify(caseNotificationService).acceptNotification(anyList());
//  }

//  @SuppressWarnings("unchecked")
//  @Test
//  public void testReceivingCaseNotificationValidXmlExceptionThrownInProcessing()
//          throws InterruptedException, IOException, JMSException {
//    // Set up CountDownLatch for synchronisation with async call
//    final CountDownLatch caseNotificationServiceInvoked = new CountDownLatch(1);
//    // Release all waiting threads when mock caseNotificationService.acceptFeedback method is called
//    doAnswer(countsDownLatch(caseNotificationServiceInvoked)).when(caseNotificationService).acceptNotification(any(List.class));
//
//    Mockito.doThrow(new RuntimeException()).when(caseNotificationService).acceptNotification(anyList());
//
//    String testMessage = FileUtils.readFileToString(provideTempFile("/xmlSampleFiles/validCaseNotification.xml.txt"), "UTF-8");
//    testOutbound.send(org.springframework.messaging.support.MessageBuilder.withPayload(testMessage).build());
//
//    // Await synchronisation with the asynchronous message call
//    caseNotificationServiceInvoked.await(RECEIVE_TIMEOUT, MILLISECONDS);
//
//    /**
//     * We check that no xml ends up on the invalid queue.
//     */
//    int finalCounter = JmsHelper.numberOfMessagesOnQueue(connection, INVALID_CASE_NOTIFICATIONS_QUEUE);
//    assertEquals(initialCounter, finalCounter);
//
//    /**
//     * We check that the xml ends up on the dead letter queue.
//     */
//    Message<?> message = activeMQDLQXml.receive(RECEIVE_TIMEOUT);
//    String payload = (String) message.getPayload();
//    assertEquals(testMessage, payload);
//
//    /**
//     * We check the message was processed AND NOT re-processed (see maximumRedeliveries in test-broker.xml)
//     */
//    // TODO Assertion below is ok in IDE but not from command line?
//    // verify(caseNotificationService, times(1)).acceptNotification(anyList());
//  }

  /**
   * Should be called when mock method is called in asynchronous test to
   * countDown the CountDownLatch test thread is waiting on.
   *
   * @param serviceInvoked CountDownLatch to countDown
   * @return Answer<CountDownLatch> Mockito Answer object
   */
  private Answer<CountDownLatch> countsDownLatch(final CountDownLatch serviceInvoked) {
    return new Answer<CountDownLatch>() {
      @Override
      public CountDownLatch answer(InvocationOnMock invocationOnMock)
          throws Throwable {
        serviceInvoked.countDown();
        return null;
      }
    };
  }

  private File provideTempFile(String inputStreamLocation) throws IOException {
    InputStream is = getClass().getResourceAsStream(inputStreamLocation);
    File tempFile = File.createTempFile("prefix","suffix");
    tempFile.deleteOnExit();
    FileOutputStream out = new FileOutputStream(tempFile);
    IOUtils.copy(is, out);
    return tempFile;
  }
}

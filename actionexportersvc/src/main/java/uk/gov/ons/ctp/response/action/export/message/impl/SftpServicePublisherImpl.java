package uk.gov.ons.ctp.response.action.export.message.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Publisher;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.GenericMessage;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.export.message.ActionFeedbackPublisher;
import uk.gov.ons.ctp.response.action.export.message.SftpServicePublisher;
import uk.gov.ons.ctp.response.action.export.scheduled.ExportInfo;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.message.feedback.Outcome;

/**
 * Service implementation responsible for publishing transformed ActionRequests
 * via sftp. See Spring Integration flow for details of sftp outbound channel.
 *
 */
@Named
@MessageEndpoint
@Slf4j
public class SftpServicePublisherImpl implements SftpServicePublisher {

  private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
  private static final String ACTION_LIST = "list_actionIds";
  private static final int BATCH_SIZE = 10000;

  @Inject
  private ActionRequestService actionRequestService;

  @Inject
  private ActionFeedbackPublisher actionFeedbackPubl;

  @Inject
  private ExportInfo exportInfo;

  @Override
  @Publisher(channel = "sftpOutbound")
  public byte[] sendMessage(@Header(FileHeaders.REMOTE_FILE) String filename,
      @Header(ACTION_LIST) List<String> actionIds,
      ByteArrayOutputStream stream) {
    return stream.toByteArray();
  }

  /**
   * Using JPA entities to update repository for actionIds exported was slow.
   * JPQL queries used for performance reasons. To increase performance updates
   * batched with IN clause.
   * 
   * @param message Spring integration message sent
   */
  @SuppressWarnings("unchecked")
  @Override
  @ServiceActivator(inputChannel = "sftpSuccessProcess")
  public void sftpSuccessProcess(GenericMessage<GenericMessage<byte[]>> message) {
    List<String> actionList = (List<String>) message.getPayload().getHeaders().get(ACTION_LIST);
    Set<BigInteger> actionIds = new HashSet<BigInteger>();
    Timestamp now = DateTimeUtil.nowUTC();
    String dateStr = new SimpleDateFormat(DATE_FORMAT).format(now);
    List<List<String>> subLists = Lists.partition(actionList, BATCH_SIZE);
    subLists.forEach((batch) -> {
      batch.forEach((actionId) -> {
        actionIds.add(new BigInteger(actionId));
      });
      int saved = actionRequestService.updateDateSentByActionId(actionIds, now);
      if (actionIds.size() == saved) {
        sendFeedbackMessage(actionRequestService.retrieveResponseRequiredByActionId(actionIds), dateStr);
      } else {
        log.error("ActionRequests {} failed to update DateSent", actionIds);
      }
      actionIds.clear();
    });

    log.info("Sftp transfer complete for file {}", message.getPayload().getHeaders().get(FileHeaders.REMOTE_FILE));
    exportInfo.addOutcome((String) message.getPayload().getHeaders().get(FileHeaders.REMOTE_FILE) + " transferred with "
        + Integer.toString(actionList.size()) + " requests.");
  }

  @Override
  @ServiceActivator(inputChannel = "sftpFailedProcess")
  public void sftpFailedProcess(ErrorMessage message) {
    log.error("Sftp transfer failed for file {} for action requests {}",
        ((MessagingException) message.getPayload()).getFailedMessage().getHeaders().get(FileHeaders.REMOTE_FILE),
        ((MessagingException) message.getPayload()).getFailedMessage().getHeaders().get(ACTION_LIST));
    exportInfo.addOutcome((String) ((MessagingException) message.getPayload()).getFailedMessage().getHeaders()
        .get(FileHeaders.REMOTE_FILE) + " failed to transfer.");

  }

  /**
   * Send ActionFeedback
   *
   * @param actionIds of ActionRequests for which to send ActionFeedback.
   * @param dateStr when actioned.
   */
  private void sendFeedbackMessage(List<BigInteger> actionIds, String dateStr) {

    actionIds.forEach((actionId) -> {
      ActionFeedback actionFeedback = new ActionFeedback(actionId,
          "ActionExport Sent: " + dateStr, Outcome.REQUEST_COMPLETED);
      actionFeedbackPubl.sendActionFeedback(actionFeedback);
    });
  }
}

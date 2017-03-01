package uk.gov.ons.ctp.response.action.export.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.message.ActionFeedbackPublisher;
import uk.gov.ons.ctp.response.action.export.repository.ActionRequestRepository;
import uk.gov.ons.ctp.response.action.export.repository.AddressRepository;
import uk.gov.ons.ctp.response.action.export.service.ActionExportService;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.message.feedback.Outcome;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

/**
 * Service implementation responsible for persisting action export requests
 */
@Named
@Slf4j
public class ActionExportServiceImpl implements ActionExportService {

  private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

  private static final int TRANSACTION_TIMEOUT = 60;

  @Inject
  private ActionFeedbackPublisher actionFeedbackPubl;

  @Inject
  private MapperFacade mapperFacade;

  @Inject
  private ActionRequestRepository actionRequestRepo;

  @Inject
  private AddressRepository addressRepo;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public void acceptInstruction(ActionInstruction instruction) {
    if ((instruction.getActionRequests() != null) &&
        (instruction.getActionRequests().getActionRequests().size() > 0)) {
      processActionRequests(instruction.getActionRequests().getActionRequests());
    } else {
      log.info("No ActionRequests to process");
    }
    if ((instruction.getActionCancels() != null) &&
        (instruction.getActionCancels().getActionCancels().size() > 0)) {
      processActionCancels(instruction.getActionCancels().getActionCancels());
    } else {
      log.info("No ActionCancels to process");
    }
  }

  /**
   * To process a list of actionRequests
   *
   * @param actionRequests list to be processed
   */
  private void processActionRequests(List<ActionRequest> actionRequests) {
    log.debug("Saving {} actionRequests", actionRequests.size());
    List<ActionRequestInstruction> actionRequestDocs = mapperFacade.mapAsList(actionRequests,
        ActionRequestInstruction.class);
    Timestamp now = DateTimeUtil.nowUTC();
    actionRequestDocs.forEach(actionRequestDoc -> {
      actionRequestDoc.setDateStored(now);
      if (!addressRepo.tupleExists(actionRequestDoc.getAddress().getUprn())) {
        // Address should never change so do not save if already exists
        addressRepo.persist(actionRequestDoc.getAddress());
      }
//      if (actionRequestRepo.tupleExists(actionRequestDoc.getActionId())) {
//        // ActionRequests should never be sent twice with same actionId but...
//        log.warn("Key ActionId {} already exists", actionRequestDoc.getActionId());
//        actionRequestRepo.save(actionRequestDoc);
//      } else {
        actionRequestRepo.persist(actionRequestDoc);
//      }
    });

    String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(now);
    actionRequestDocs.forEach(actionRequestDoc -> {
      if (actionRequestDoc.isResponseRequired()) {
        ActionFeedback actionFeedback = new ActionFeedback(actionRequestDoc.getActionId(),
            "ActionExport Stored: " + timeStamp, Outcome.REQUEST_ACCEPTED);
        actionFeedbackPubl.sendActionFeedback(actionFeedback);
      }
    });
  }

  /**
   * To process a list of actionCancels
   *
   * @param actionCancels list to be processed
   */
  private void processActionCancels(List<ActionCancel> actionCancels) {
    log.debug("Processing {} actionCancels", actionCancels.size());
    String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
    boolean cancelled = false;
    for (ActionCancel actionCancel : actionCancels) {
      ActionRequestInstruction actionRequest = actionRequestRepo.findOne(actionCancel.getActionId());
      if (actionRequest != null && actionRequest.getDateSent() == null) {
        actionRequestRepo.delete(actionCancel.getActionId());
        cancelled = true;
      } else {
        cancelled = false;
      }
      if (actionCancel.isResponseRequired()) {
        ActionFeedback actionFeedback = new ActionFeedback(actionCancel.getActionId(),
            "ActionExport Cancelled: " + timeStamp,
            cancelled ? Outcome.CANCELLATION_COMPLETED : Outcome.CANCELLATION_FAILED);
        actionFeedbackPubl.sendActionFeedback(actionFeedback);
      }
    }
  }
}

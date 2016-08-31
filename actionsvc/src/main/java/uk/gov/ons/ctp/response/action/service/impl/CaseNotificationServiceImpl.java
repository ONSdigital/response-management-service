package uk.gov.ons.ctp.response.action.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.service.CaseNotificationService;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotification;

/**
 * Save to Action.Case table for case creation life cycle events, delete for
 * case close life cycle events.
 *
 */
@Named
@Slf4j
public class CaseNotificationServiceImpl implements CaseNotificationService {

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private ActionCaseRepository actionCaseRepo;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  public void acceptNotification(List<CaseNotification> notifications) {
    notifications.forEach((notification) -> {
      ActionCase actionCase = new ActionCase(notification.getActionPlanId(), notification.getCaseId());
      switch (notification.getNotificationType()) {
      case CREATED:
        actionCaseRepo.save(actionCase);
        break;
      case CLOSED:
        actionCaseRepo.delete(actionCase);
        break;
      default:
        log.warn("Unknown Case lifecycle event {}", notification.getNotificationType());
        break;
      }
    });
    actionCaseRepo.flush();
  }
}

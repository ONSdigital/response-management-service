package uk.gov.ons.ctp.response.action.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.service.CaseNotificationService;
import uk.gov.ons.ctp.response.casesvc.message.notification.NotificationType;

/**
 * Save to Action.Case table for case creation life cycle events, delete for
 * case close life cycle events.
 *
 */
@Named
@Slf4j
public class CaseNotificationServiceImpl implements CaseNotificationService {

  @Inject
  private ActionCaseRepository actionCaseRepo;

  @Override
  public void acceptNotification(List<ActionCase> cases) {
    cases.forEach((caseMessage) -> {
      if (caseMessage.getNotificationType() == NotificationType.CREATED) {
        actionCaseRepo.save(caseMessage);
      } else if (caseMessage.getNotificationType() == NotificationType.CLOSED) {
        actionCaseRepo.delete(caseMessage);
      } else {
        log.warn("Unknown Case lifecycle event {}", caseMessage.getNotificationType());
      }
    });
    actionCaseRepo.flush();
  }
}

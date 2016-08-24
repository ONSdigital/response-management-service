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
  public void acceptNotification(List<ActionCase> cases) {
    cases.forEach((caseMessage) -> {
      switch (caseMessage.getNotificationType()) {
      case CREATED:
        actionCaseRepo.save(caseMessage);
        break;
      case CLOSED:
        actionCaseRepo.delete(caseMessage);
        break;
      default:
        log.warn("Unknown Case lifecycle event {}", caseMessage.getNotificationType());
        break;
      }
    });
    actionCaseRepo.flush();
  }
}

package uk.gov.ons.ctp.response.action.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.Survey;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.domain.repository.SurveyRepository;
import uk.gov.ons.ctp.response.action.service.ActionService;
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

  @Inject
  private ActionPlanRepository actionPlanRepo;
  
  @Inject 
  private ActionService actionService;
  
  @Inject
  private SurveyRepository surveyRepo;

  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  public void acceptNotification(List<CaseNotification> notifications) {
    notifications.forEach((notif) -> {
      ActionPlan actionPlan = actionPlanRepo.findOne(notif.getActionPlanId());

      if (actionPlan != null) {
        Survey survey = surveyRepo.findOne(actionPlan.getSurveyId());
        ActionCase actionCase = ActionCase.builder().actionPlanId(notif.getActionPlanId()).caseId(notif.getCaseId()).build();
        switch (notif.getNotificationType()) {
        case REPLACED:
          actionCase.setActionPlanStartDate(DateTimeUtil.nowUTC());
          actionCaseRepo.save(actionCase);
        case ACTIVATED:
          actionCase.setActionPlanStartDate(survey.getSurveyStartDate());
          actionCaseRepo.save(actionCase);
          break;
        case DISABLED:
        case DEACTIVATED:
          actionService.cancelActions(notif.getCaseId());
          actionCaseRepo.delete(actionCase);
          break;
        default:
          log.warn("Unknown Case lifecycle event {}", notif.getNotificationType());
          break;
        }
      } else {
        log.warn("Cannot accept CaseNotification for none existent actionplan {}", notif.getActionPlanId());
      }
    });
    actionCaseRepo.flush();
  }
}

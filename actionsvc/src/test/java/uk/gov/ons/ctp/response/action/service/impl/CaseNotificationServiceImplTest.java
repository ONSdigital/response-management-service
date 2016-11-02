package uk.gov.ons.ctp.response.action.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.ons.ctp.response.casesvc.message.notification.NotificationType.ACTIVATED;
import static uk.gov.ons.ctp.response.casesvc.message.notification.NotificationType.DISABLED;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.Survey;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.domain.repository.SurveyRepository;
import uk.gov.ons.ctp.response.action.service.ActionService;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotification;

/**
 * Tests for the CaseNotificationServiceImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class CaseNotificationServiceImplTest {

  @Mock
  private ActionCaseRepository actionCaseRepo;

  @Mock
  private ActionPlanRepository actionPlanRepo;
  
  @Mock 
  private ActionService actionService;
  
  @Mock
  private SurveyRepository surveyRepo;

  @InjectMocks
  private CaseNotificationServiceImpl caseNotificationService;

  /**
   * Test calls repository correctly
   */
  @Test
  public void testAcceptNotification() throws Exception {

    List<ActionPlan> actionPlans = FixtureHelper.loadClassFixtures(ActionPlan[].class);
    List<Survey> surveys = FixtureHelper.loadClassFixtures(Survey[].class);

    // Setup Test data
    List<CaseNotification> lifeCycleEvents = new ArrayList<CaseNotification>();
    lifeCycleEvents.add(new CaseNotification(1, 3, ACTIVATED));
    lifeCycleEvents.add(new CaseNotification(2, 3, DISABLED));
    lifeCycleEvents.add(new CaseNotification(3, 3, ACTIVATED));
    lifeCycleEvents.add(new CaseNotification(4, 4, ACTIVATED));

    Mockito.when(actionPlanRepo.findOne(3)).thenReturn(actionPlans.get(0));
    Mockito.when(actionPlanRepo.findOne(4)).thenReturn(actionPlans.get(1));
    Mockito.when(surveyRepo.findOne(1)).thenReturn(surveys.get(0));

    // Call method
    caseNotificationService.acceptNotification(lifeCycleEvents);

    // Verify calls made
    verify(actionCaseRepo, times(1)).delete(new ActionCase(3, 2, any(Timestamp.class)));
    verify(actionCaseRepo, times(3)).save(any(ActionCase.class));
    verify(actionCaseRepo).flush();

  }
}

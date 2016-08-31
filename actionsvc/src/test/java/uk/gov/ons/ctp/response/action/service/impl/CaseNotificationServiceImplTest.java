package uk.gov.ons.ctp.response.action.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static uk.gov.ons.ctp.response.casesvc.message.notification.NotificationType.CLOSED;
import static uk.gov.ons.ctp.response.casesvc.message.notification.NotificationType.CREATED;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotification;

/**
 * Tests for the CaseNotificationServiceImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class CaseNotificationServiceImplTest {

  @Mock
  private ActionCaseRepository actionCaseRepo;

  @InjectMocks
  private CaseNotificationServiceImpl caseNotificationService;

  /**
   * Test calls repository correctly
   */
  @Test
  public void testAcceptNotification() {

    // Setup Test data
    List<CaseNotification> lifeCycleEvents = new ArrayList<CaseNotification>();
    lifeCycleEvents.add(new CaseNotification(1, 3, CREATED));
    lifeCycleEvents.add(new CaseNotification(2, 3, CLOSED));
    lifeCycleEvents.add(new CaseNotification(3, 3, CREATED));
    lifeCycleEvents.add(new CaseNotification(4, 4, CREATED));

    // Call method
    caseNotificationService.acceptNotification(lifeCycleEvents);

    // Verify calls made
    verify(actionCaseRepo, times(1)).delete(new ActionCase(3, 2));
    verify(actionCaseRepo, times(3)).save(any(ActionCase.class));
    verify(actionCaseRepo).flush();

  }
}

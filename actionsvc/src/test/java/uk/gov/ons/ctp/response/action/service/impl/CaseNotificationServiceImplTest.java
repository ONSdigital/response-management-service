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
    List<ActionCase> lifeCycleEvents = new ArrayList<ActionCase>();
    lifeCycleEvents.add(new ActionCase(3, 1, CLOSED));
    lifeCycleEvents.add(new ActionCase(3, 2, CREATED));
    lifeCycleEvents.add(new ActionCase(3, 3, CREATED));
    lifeCycleEvents.add(new ActionCase(3, 4, CREATED));

    // Call method
    caseNotificationService.acceptNotification(lifeCycleEvents);

    // Verify calls made
    verify(actionCaseRepo).delete(any(ActionCase.class));
    verify(actionCaseRepo, times(3)).save(any(ActionCase.class));
    verify(actionCaseRepo).flush();

  }
}

package uk.gov.ons.ctp.response.action.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.config.CaseSvc;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.service.CaseSvcClientService;

/**
 * Tests for the ActionPlanJobServiceImpl
 */
@RunWith(MockitoJUnitRunner.class)
public class ActionPlanJobServiceImplTest {

  @Mock
  private AppConfig appConfig;

  @Mock
  private ActionPlanRepository actionPlanRepo;

  @Mock
  private ActionCaseRepository actionCaseRepo;

  @Mock
  private ActionPlanJobRepository actionPlanJobRepo;

  @Mock
  private CaseSvcClientService caseSvcClientService;

  @InjectMocks
  private ActionPlanJobServiceImpl actionPlanJobServiceImpl;

  /**
   * Before the test
   */
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test that when we fail at first hurdle to load ActionTypes we do not go on
   * to call anything else In reality the wake up method would then be called
   * again after a sleep interval by spring but we cannot test that here
   *
   * @throws Exception oops
   */
  @Test
  public void testCreateAndExecuteActionPlanJob() throws Exception {
    // set up dummy data
    CaseSvc caseSvcConfig = new CaseSvc();

    List<ActionPlan> actionPlans = FixtureHelper.loadClassFixtures(ActionPlan[].class);
    List<ActionPlanJob> actionPlanJobs = FixtureHelper.loadClassFixtures(ActionPlanJob[].class);

    // wire up mock responses
    Mockito.when(appConfig.getCaseSvc()).thenReturn(caseSvcConfig);

    Mockito.when(caseSvcClientService.getOpenCasesForActionPlan(eq(1)))
        .thenReturn(Arrays.asList(1, 2, 3, 4, 5, 6));

    Mockito.when(actionPlanRepo.findOne(1)).thenReturn(actionPlans.get(0));
    Mockito.when(actionPlanJobRepo.save(actionPlanJobs.get(0))).thenReturn(actionPlanJobs.get(0));
    Mockito.when(actionCaseRepo.createActions(1)).thenReturn(Boolean.TRUE);

    // let it roll
    actionPlanJobServiceImpl.createAndExecuteActionPlanJob(actionPlanJobs.get(0));

    // assert the right calls were made
    verify(actionPlanRepo).findOne(1);
    verify(actionCaseRepo).createActions(1);
    verify(actionPlanJobRepo).save(actionPlanJobs.get(0));
    verify(caseSvcClientService).getOpenCasesForActionPlan(eq(1));
    verify(actionCaseRepo, times(6)).save(any(ActionCase.class));
    verify(actionCaseRepo, times(1)).flush();
  }

}

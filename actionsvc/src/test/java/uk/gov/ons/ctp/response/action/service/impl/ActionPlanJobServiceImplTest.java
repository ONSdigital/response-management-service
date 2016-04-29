package uk.gov.ons.ctp.response.action.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.config.CaseFrameSvc;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;

@RunWith(MockitoJUnitRunner.class)
public class ActionPlanJobServiceImplTest {

  @Mock
  AppConfig appConfig;

  @Mock
  RestClient caseFrameClient;

  @Mock
  ActionPlanRepository actionPlanRepo;

  @Mock
  ActionCaseRepository actionCaseRepo;

  @Mock
  ActionPlanJobRepository actionPlanJobRepo;

  @InjectMocks
  ActionPlanJobServiceImpl actionPlanJobServiceImpl;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test that when we fail at first hurdle to load ActionTypes we do not go on
   * to call anything else In reality the wakeup mathod would then be called
   * again after a sleep interval by spring but we cannot test that here
   * 
   * @throws Exception
   */
  @Test
  public void testCreateAndExecuteActionPlanJob() throws Exception {
    // set up dummy data
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();

    List<ActionPlan> actionPlans = FixtureHelper.loadClassFixtures(ActionPlan[].class);
    List<ActionPlanJob> actionPlanJobs = FixtureHelper.loadClassFixtures(ActionPlanJob[].class);

    // wire up mock responses
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);

    Mockito.when(caseFrameClient.getResources(anyString(), eq(Integer[].class), any(Map.class), any(MultiValueMap.class), eq(1)))
    .thenReturn(Arrays.asList(1,2,3,4,5,6));

    Mockito.when(actionPlanRepo.findOne(1)).thenReturn(actionPlans.get(0));
    Mockito.when(actionPlanJobRepo.save(actionPlanJobs.get(0))).thenReturn(actionPlanJobs.get(0));
    Mockito.when(actionCaseRepo.createActions(1)).thenReturn(Boolean.TRUE);

    // let it roll
    actionPlanJobServiceImpl.createAndExecuteActionPlanJob(actionPlanJobs.get(0));

    // assert the right calls were made
    verify(actionPlanRepo).findOne(1);
    verify(actionCaseRepo).createActions(1);
    verify(actionPlanJobRepo).save(actionPlanJobs.get(0)); 
    verify(caseFrameClient).getResources(anyString(), eq(Integer[].class), any(Map.class), any(MultiValueMap.class), eq(1));
    verify(actionCaseRepo, times(6)).saveAndFlush(any(ActionCase.class));
  }

}

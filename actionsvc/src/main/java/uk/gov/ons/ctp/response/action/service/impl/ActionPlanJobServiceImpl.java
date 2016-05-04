package uk.gov.ons.ctp.response.action.service.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;

/**
 * Implementation
 */
@Named
@Slf4j
public class ActionPlanJobServiceImpl implements ActionPlanJobService {

  // The sql function might take a while
  private static final int TRANSACTION_TIMEOUT = 300;

  @Inject
  private AppConfig appConfig;

  @Inject
  private RestClient caseFrameClient;

  @Inject
  private ActionPlanRepository actionPlanRepo;

  @Inject
  private ActionCaseRepository actionCaseRepo;

  @Inject
  private ActionPlanJobRepository actionPlanJobRepo;

  @Override
  public final ActionPlanJob findActionPlanJob(final Integer actionPlanJobId) {
    log.debug("Entering findActionPlanJob with {}", actionPlanJobId);
    return actionPlanJobRepo.findOne(actionPlanJobId);
  }

  @Override
  public final List<ActionPlanJob> findActionPlanJobsForActionPlan(final Integer actionPlanId) {
    log.debug("Entering findActionPlanJobsForActionPlan with {}", actionPlanId);
    return actionPlanJobRepo.findByActionPlanId(actionPlanId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public final ActionPlanJob createAndExecuteActionPlanJob(final ActionPlanJob actionPlanJob) {
    Integer actionPlanId = actionPlanJob.getActionPlanId();
    log.debug("Entering executeActionPlan wth plan id {}", actionPlanId);
    ActionPlan actionPlan = actionPlanRepo.findOne(actionPlanId);
    ActionPlanJob createdJob = null;
    if (actionPlan != null) {
      // enrich and save the job
      actionPlanJob.setState(ActionPlanJobDTO.ActionPlanJobState.SUBMITTED);
      Timestamp now = new Timestamp(new Date().getTime());
      actionPlanJob.setCreatedDateTime(now);
      actionPlanJob.setUpdatedDateTime(now);
      createdJob = actionPlanJobRepo.save(actionPlanJob);

      // setup and make the call to caseframe to find open cases for plan
      MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();
      queryParamMap.put("status", Arrays.asList(CaseDTO.CaseState.INIT.name()));
      //TODO - move to caseframesvcclientservice
      List<Integer> openCasesForPlan = caseFrameClient.getResources(
          appConfig.getCaseFrameSvc().getCaseByStatusAndActionPlanPath(), Integer[].class, null, queryParamMap,
          actionPlanId);

      // create entry in action.case for each open case for job
      for (Integer openCaseId : openCasesForPlan) {
        log.debug("Creating action.case for case {} and actionplanid {}", openCaseId, createdJob.getActionPlanId());
        ActionCase actionCase = new ActionCase(createdJob.getActionPlanJobId(), openCaseId);
        actionCaseRepo.saveAndFlush(actionCase);
      }

      // get the repo to call sql function to create actions
      actionCaseRepo.createActions(createdJob.getActionPlanJobId());
    }
    return createdJob;
  }
}

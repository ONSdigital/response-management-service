package uk.gov.ons.ctp.response.action.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.distributed.DistributedLockManager;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

/**
 * Implementation
 */
@Service
@Slf4j
public class ActionPlanJobServiceImpl implements ActionPlanJobService {

  private static final String ACTION_PLAN_SPAN = "automatedActionPlanExecution";
  private static final String CREATED_BY_SYSTEM = "SYSTEM";

  @Autowired
  private DistributedLockManager actionPlanExecutionLockManager;

  @Autowired
  private Tracer tracer;

  @Autowired
  private AppConfig appConfig;

  @Autowired
  private ActionPlanRepository actionPlanRepo;

  @Autowired
  private ActionCaseRepository actionCaseRepo;

  @Autowired
  private ActionPlanJobRepository actionPlanJobRepo;

  @Override
  public Optional<ActionPlanJob> findActionPlanJob(final Integer actionPlanJobId) {
    log.debug("Entering findActionPlanJob with {}", actionPlanJobId);
    return Optional.ofNullable(actionPlanJobRepo.findOne(actionPlanJobId));
  }

  @Override
  public List<ActionPlanJob> findActionPlanJobsForActionPlan(final Integer actionPlanId) {
    log.debug("Entering findActionPlanJobsForActionPlan with {}", actionPlanId);
    return actionPlanJobRepo.findByActionPlanId(actionPlanId);
  }

  @Override
  public List<ActionPlanJob> createAndExecuteAllActionPlanJobs() {
    Span span = tracer.createSpan(ACTION_PLAN_SPAN);
    List<ActionPlanJob> executedJobs = new ArrayList<>();
    actionPlanRepo.findAll().forEach(actionPlan -> {
      ActionPlanJob job = new ActionPlanJob();
      job.setActionPlanId(actionPlan.getActionPlanId());
      job.setCreatedBy(CREATED_BY_SYSTEM);
      createAndExecuteActionPlanJob(job, false).ifPresent(j -> executedJobs.add(j));
    });
    tracer.close(span);
    return executedJobs;
  }

  @Override
  public Optional<ActionPlanJob> createAndExecuteActionPlanJob(final ActionPlanJob actionPlanJob) {
    return createAndExecuteActionPlanJob(actionPlanJob, true);
  }

  /**
   * the root method for executing an action plan - called indirectly by the
   * restful endpoint when executing a single plan manually and by the scheduled
   * execution of all plans in sequence. See the other createAndExecute plan
   * methods in this class
   * 
   * @param actionPlanJob the plan to execute
   * @param forcedExecution true when called indirectly for manual execution -
   *          the plan lock is still used (we don't want more than one
   *          concurrent plan execution), but we skip the last run time check
   * @return the plan job if it was run or null if not
   */
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  private Optional<ActionPlanJob> createAndExecuteActionPlanJob(final ActionPlanJob actionPlanJob,
      boolean forcedExecution) {
    Integer actionPlanId = actionPlanJob.getActionPlanId();

    ActionPlanJob createdJob = null;
    // load the action plan
    ActionPlan actionPlan = actionPlanRepo.findOne(actionPlanId);
    if (actionPlan != null) {

      if (actionPlanExecutionLockManager.lock(actionPlan.getName())) {
        try {
          Timestamp now = DateTimeUtil.nowUTC();
          if (!forcedExecution) {
            Date lastExecutionTime = new Date(
                now.getTime() - appConfig.getPlanExecution().getDelayMilliSeconds());

            if (actionPlan.getLastRunDateTime() != null
                && actionPlan.getLastRunDateTime().after(lastExecutionTime)) {
              log.debug("Job for plan {} has been run since last wake up - skipping", actionPlanId);
              return Optional.empty();
            }
          }

          // if no cases for actionplan why bother?
          if (actionCaseRepo.countByActionPlanId(actionPlanId) == 0) {
            log.debug("No open cases for action plan {} - skipping", actionPlanId);
            return Optional.empty();
          }

          // enrich and save the job
          actionPlanJob.setState(ActionPlanJobDTO.ActionPlanJobState.SUBMITTED);
          actionPlanJob.setCreatedDateTime(now);
          actionPlanJob.setUpdatedDateTime(now);
          // save the new job record
          createdJob = actionPlanJobRepo.save(actionPlanJob);
          log.info("Running actionplanjobid {} actionplanid {}", createdJob.getActionPlanJobId(),
              createdJob.getActionPlanId());
          // get the repo to call sql function to create actions
          actionCaseRepo.createActions(createdJob.getActionPlanJobId());
        } finally {
          log.debug("Releasing lock on action plan {}", actionPlanId);
          actionPlanExecutionLockManager.unlock(actionPlan.getName());
        }
      } else {
        log.debug("Could not get lock on action plan {}", actionPlanId);
      }
    }

    return Optional.ofNullable(createdJob);
  }
}

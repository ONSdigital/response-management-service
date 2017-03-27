package uk.gov.ons.ctp.response.action.scheduled.plan;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

/**
 * This bean will have the actionPlanJobService injected into it by spring on
 * constructions. It will then schedule the running of the actionPlanJobService
 * createAndExecuteAllActionPlanJobs using details from the AppConfig
 */
@Named
@Slf4j
public class PlanScheduler implements HealthIndicator {

  @Inject
  private ActionPlanJobService actionPlanJobServiceImpl;

  private PlanExecutionInfo executionInfo = new PlanExecutionInfo();

  /**
   * schedule the Execution of Action Plans It is simply a scheduled trigger for
   * the service layer method.
   *
   */
  @Scheduled(fixedDelayString = "#{appConfig.planExecution.delayMilliSeconds}")
  public void run() {
    log.info("Executing ActionPlans");
    try {
      executionInfo = new PlanExecutionInfo();
      executionInfo.setExecutedJobs(actionPlanJobServiceImpl.createAndExecuteAllActionPlanJobs());
    } catch (Exception e) {
      log.error("Exception in action plan scheduler", e);
    }
  }

  @Override
  public Health health() {
    return Health.up()
        .withDetail("planExecutionInfo", executionInfo)
        .build();
  }
}

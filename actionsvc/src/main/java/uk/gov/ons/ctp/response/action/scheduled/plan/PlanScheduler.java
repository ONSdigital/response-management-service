package uk.gov.ons.ctp.response.action.scheduled.plan;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

/**
 * This bean will have the actionPlanJobService injected into it by spring on
 * constructions. It will then schedule the running of the actionPlanJobService createAndExecuteAllActionPlanJobs using
 * details from the AppConfig
 */
@Named
public class PlanScheduler implements HealthIndicator {


  @Inject
  private ActionPlanJobService actionPlanJobServiceImpl;

  private PlanExecutionInfo executionInfo = new PlanExecutionInfo();

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  /**
   * Create the scheduler for the Execution of Action Plans
   * It is simply a scheduled trigger for the service layer method.
   *
   * @param applicationConfig injected app config needs injecting as cannot use
   *          the class appConfig - is not injected until this class created -
   *          chicken meet egg
   */
  @Inject
  public PlanScheduler(AppConfig applicationConfig) {
    final Runnable planExecutionRunnable = new Runnable() {
      public void run() {
        executionInfo.setExecutedJobs(actionPlanJobServiceImpl.createAndExecuteAllActionPlanJobs());
      }
    };

    scheduler.scheduleAtFixedRate(planExecutionRunnable,
        applicationConfig.getPlanExecution().getInitialDelaySeconds(),
        applicationConfig.getPlanExecution().getSubsequentDelaySeconds(), SECONDS);
  }

  @Override
  public Health health() {
    return Health.up()
        .withDetail("planExecutionInfo", executionInfo)
        .build();
  }
}

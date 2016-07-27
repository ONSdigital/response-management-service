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
 * This bean will have the actiondistributor injected into it by spring on
 * constructions. It will then schedule the running of the distributor using
 * details from the AppConfig
 */
@Named
public class PlanScheduler implements HealthIndicator {

  @Override
  public Health health() {
    return Health.up()
        .withDetail("planExecutionInfo", executionInfo)
        .build();
  }

  @Inject
  private ActionPlanJobService actionPlanJobServiceImpl;

  private PlanExecutionInfo executionInfo = new PlanExecutionInfo();

  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  /**
   * Create the scheduler for the Action Distributor
   *
   * @param applicationConfig injected app config needs injecting as cannot use
   *          the class appConfig - is not injected until this class created -
   *          chicken meet egg
   */
  @Inject
  public PlanScheduler(AppConfig applicationConfig) {
    final Runnable distributorRunnable = new Runnable() {
      public void run() {
        actionPlanJobServiceImpl.createAndExecuteAllActionPlanJobs();
      }
    };

    scheduler.scheduleAtFixedRate(distributorRunnable,
        applicationConfig.getPlanExecution().getInitialDelaySeconds(),
        applicationConfig.getPlanExecution().getSubsequentDelaySeconds(), SECONDS);
  }
}

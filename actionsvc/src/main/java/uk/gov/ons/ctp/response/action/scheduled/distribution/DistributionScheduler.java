package uk.gov.ons.ctp.response.action.scheduled.distribution;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.config.AppConfig;

/**
 * This bean will have the actionDistributor injected into it by spring on
 * constructions. It will then schedule the running of the distributor using
 * details from the AppConfig
 */
@Named
@Slf4j
public class DistributionScheduler implements HealthIndicator {

  @Override
  public Health health() {
    return Health.up()
        .withDetail("distributionInfo", distributionInfo)
        .build();
  }

  @Inject
  private ActionDistributor actionDistributorImpl;

  private DistributionInfo distributionInfo = new DistributionInfo();

  /**
   * Create the scheduler for the Action Distributor
   *
   * @param applicationConfig injected app config needs injecting as cannot use
   *          the class appConfig - is not injected until this class created -
   *          chicken meet egg
   */
  @Inject
  public DistributionScheduler(AppConfig applicationConfig) {
    final Runnable distributorRunnable = new Runnable() {
      @Override public void run() {
        try {
          distributionInfo = actionDistributorImpl.distribute();
        } catch (Exception e) {
          log.error("Exception in action distributor", e);
        }
      }
    };

    log.debug("Scheduling Action Distribution initial delay={}, subsequent delay={}",applicationConfig.getActionDistribution().getInitialDelaySeconds(),
        applicationConfig.getActionDistribution().getSubsequentDelaySeconds(), SECONDS);

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleAtFixedRate(distributorRunnable,
        applicationConfig.getActionDistribution().getInitialDelaySeconds(),
        applicationConfig.getActionDistribution().getSubsequentDelaySeconds(), SECONDS);
  }
}

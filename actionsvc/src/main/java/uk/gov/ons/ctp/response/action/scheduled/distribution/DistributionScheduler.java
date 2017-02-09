package uk.gov.ons.ctp.response.action.scheduled.distribution;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

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
   * Scheduled execution of the Action Distributor
   *
   */
  @Scheduled(fixedDelayString = "#{appConfig.actionDistribution.delayMilliSeconds}")
  public void run() {
    try {
      distributionInfo = actionDistributorImpl.distribute();
    } catch (Exception e) {
      log.error("Exception in action distributor", e);
    }
  }
}

package uk.gov.ons.ctp.response.action.scheduled;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import uk.gov.ons.ctp.response.action.config.AppConfig;

/**
 * This bean will have the actiondistributor injected into it by spring on constructions.
 * It will then schedule the running of the distributor using details from the AppConfig 
 */
@Named
public class DistributionScheduler implements HealthIndicator {

  @Inject
  private AppConfig appConfig;

  @Override
  public Health health() {
      return Health.up()
          .withDetail("distributionInfo", distributionInfo)
          .withDetail("distributionConfig", appConfig.getActionDistribution())
          .build();
  }

  @Inject
  private ActionDistributorImpl actionDistributorImpl;
  
  private DistributionInfo distributionInfo;
  
  private final ScheduledExecutorService scheduler =
    Executors.newScheduledThreadPool(1);

  @Inject
  public DistributionScheduler (AppConfig appConfig) {
    final Runnable distributorRunnable = new Runnable() {
      public void run() { distributionInfo = actionDistributorImpl.distribute(); }
    };

    scheduler.scheduleAtFixedRate(distributorRunnable, appConfig.getActionDistribution().getInitialDelaySeconds(),
        appConfig.getActionDistribution().getSubsequentDelaySeconds(), SECONDS);
  }
  
}
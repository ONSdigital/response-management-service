package uk.gov.ons.ctp.response.action.scheduled;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import uk.gov.ons.ctp.response.action.config.AppConfig;

/**
 * This bean will have the actiondistributor injected into it by spring on constructions.
 * It will then schedule the running of the distributor using details from the AppConfig 
 */
@Named
public class DistributionScheduler {

  @Inject
  private ActionDistributorImpl actionDistributorImpl;
  
  private final ScheduledExecutorService scheduler =
    Executors.newScheduledThreadPool(1);

  @Inject
  public DistributionScheduler (AppConfig appConfig) {
    final Runnable distributorRunnable = new Runnable() {
      public void run() { actionDistributorImpl.wakeUp(); }
    };

    scheduler.scheduleAtFixedRate(distributorRunnable, appConfig.getActionDistribution().getInitialDelaySeconds(),
        appConfig.getActionDistribution().getSubsequentDelaySeconds(), SECONDS);
  }
}
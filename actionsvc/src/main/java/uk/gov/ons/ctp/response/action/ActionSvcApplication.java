package uk.gov.ons.ctp.response.action;

import java.math.BigInteger;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.gov.ons.ctp.common.distributed.DistributedListManager;
import uk.gov.ons.ctp.common.distributed.DistributedListManagerRedissonImpl;
import uk.gov.ons.ctp.common.distributed.DistributedLockManager;
import uk.gov.ons.ctp.common.distributed.DistributedLockManagerRedissonImpl;
import uk.gov.ons.ctp.common.error.RestExceptionHandler;
import uk.gov.ons.ctp.common.jackson.CustomObjectMapper;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManagerFactory;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.state.ActionSvcStateTransitionManagerFactory;

/**
 * The main entry point into the Action Service SpringBoot Application.
 */
@SpringBootApplication
@EnableTransactionManagement
@IntegrationComponentScan
@EnableAsync
@EnableCaching
@EnableScheduling
@ImportResource("springintegration/main.xml")
public class ActionSvcApplication {

  public static final String ACTION_DISTRIBUTION_LIST = "actionsvc.action.distribution";
  public static final String ACTION_EXECUTION_LOCK = "actionsvc.action.execution";

  @Autowired
  private AppConfig appConfig;

  @Bean
  public DistributedListManager<BigInteger> actionDistributionListManager(RedissonClient redissonClient) {
    return new DistributedListManagerRedissonImpl<BigInteger>(ActionSvcApplication.ACTION_DISTRIBUTION_LIST, redissonClient,
        appConfig.getDataGrid().getListTimeToWaitSeconds(),
        appConfig.getDataGrid().getListTimeToLiveSeconds());
  }

  @Bean
  public DistributedLockManager actionPlanExecutionLockManager(RedissonClient redissonClient) {
    return new DistributedLockManagerRedissonImpl(ActionSvcApplication.ACTION_EXECUTION_LOCK, redissonClient,
        appConfig.getDataGrid().getLockTimeToLiveSeconds());
  }

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config.useSingleServer()
        .setAddress(appConfig.getDataGrid().getAddress())
        .setPassword(appConfig.getDataGrid().getPassword());
    return Redisson.create(config);
  }

  /**
   * Bean used to access case frame service through REST calls
   * 
   * @return the service client
   */
  @Bean
  public RestClient caseClient() {
    RestClient restHelper = new RestClient(appConfig.getCaseSvc().getConnectionConfig());
    return restHelper;
  }

  @Autowired
  private StateTransitionManagerFactory actionSvcStateTransitionManagerFactory;

  /**
   * Bean to allow application to make controlled state transitions of Actions
   * 
   * @return the state transition manager specifically for Actions
   */
  @Bean
  public StateTransitionManager<ActionDTO.ActionState, ActionDTO.ActionEvent> actionSvcStateTransitionManager() {
    return actionSvcStateTransitionManagerFactory.getStateTransitionManager(
        ActionSvcStateTransitionManagerFactory.ACTION_ENTITY);
  }

  @Bean
  public RestExceptionHandler restExceptionHandler() {
    return new RestExceptionHandler();
  }

  @Bean @Primary
  public CustomObjectMapper CustomObjectMapper() {
    return new CustomObjectMapper();
  }

  /**
   * This method is the entry point to the Spring Boot application.
   *
   * @param args These are the optional command line arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(ActionSvcApplication.class, args);
  }
}

package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;

import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.state.StateTransitionManagerFactory;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.endpoint.ActionEndpoint;
import uk.gov.ons.ctp.response.action.endpoint.ActionPlanEndpoint;
import uk.gov.ons.ctp.response.action.endpoint.ActionPlanJobEndpoint;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
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
@ImportResource("main-int.xml")
public class ActionSvcApplication {

  public static final String ACTION_DISTRIBUTION_MAP = "actionsvc.action.distribution";

  @Autowired
  private AppConfig appConfig;

  
  /**
   * To config Hazelcast
   * @return the config
   */
  @Bean
  public Config hazelcastConfig() {
    Config hazelcastConfig = new Config();
    hazelcastConfig.addMapConfig(new MapConfig().setName(ACTION_DISTRIBUTION_MAP));
    NetworkConfig networkConfig = hazelcastConfig.getNetworkConfig();

    JoinConfig joinConfig = networkConfig.getJoin();
    MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
    multicastConfig.setEnabled(true);

    return hazelcastConfig;
  }
  
  /**
   * Bean used to access case frame service through REST calls
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
   * @return the state transition manager specifically for Actions
   */
  @Bean
  public StateTransitionManager<ActionDTO.ActionState, ActionDTO.ActionEvent> actionSvcStateTransitionManager() {
    return actionSvcStateTransitionManagerFactory.getStateTransitionManager(
        ActionSvcStateTransitionManagerFactory.ACTION_ENTITY);
  }

  /**
   * To register classes in the JAX-RS world.
   */
  @Named
  public static class JerseyConfig extends ResourceConfig {
    /**
     * Its public constructor.
     */
    public JerseyConfig() {

      register(ActionEndpoint.class);
      register(new CTPMessageBodyReader<ActionDTO>(ActionDTO.class) {
      });

      register(ActionPlanEndpoint.class);
      register(new CTPMessageBodyReader<ActionPlanDTO>(ActionPlanDTO.class) {
      });

      register(ActionPlanJobEndpoint.class);
      register(new CTPMessageBodyReader<ActionPlanJobDTO>(ActionPlanJobDTO.class) {
      });

      System.setProperty("ma.glasnost.orika.writeSourceFiles", "false");
      System.setProperty("ma.glasnost.orika.writeClassFiles", "false");
    }
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

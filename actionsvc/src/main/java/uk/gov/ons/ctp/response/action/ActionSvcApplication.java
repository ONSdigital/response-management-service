package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.endpoint.ActionEndpoint;
import uk.gov.ons.ctp.response.action.endpoint.ActionPlanEndpoint;
import uk.gov.ons.ctp.response.action.endpoint.ActionPlanJobEndpoint;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;

/**
 * The 'main' entry point into the Action Service SpringBoot Application.
 */
@SpringBootApplication
@EnableTransactionManagement
@IntegrationComponentScan
@EnableAsync
@EnableScheduling
@ImportResource("main-integration-context.xml")
public class ActionSvcApplication {

  @Autowired
  private AppConfig appConfig;
  
  @Bean
  public RestClient caseFrameClient () {
    RestClient restHelper = new RestClient (appConfig.getCaseFrameSvc().getScheme(), appConfig.getCaseFrameSvc().getHost(), appConfig.getCaseFrameSvc().getPort());
    return restHelper;
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
      packages("uk.gov.ons.ctp");

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

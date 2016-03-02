package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.gov.ons.ctp.response.action.endpoint.ActionEndpoint;
import uk.gov.ons.ctp.response.action.endpoint.ActionPlanEndpoint;

/**
 * The 'main' entry point for the Action SpringBoot Application
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class ActionSvcApplication {
  
  @Named
  public static class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
      packages("uk.gov.ons.ctp");

      register(ActionEndpoint.class);
      register(ActionPlanEndpoint.class);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ActionSvcApplication.class, args);
  }
}

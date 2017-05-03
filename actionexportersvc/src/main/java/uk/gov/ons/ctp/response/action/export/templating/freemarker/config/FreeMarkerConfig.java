package uk.gov.ons.ctp.response.action.export.templating.freemarker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import freemarker.template.TemplateExceptionHandler;

/**
 * Configuration specific to FreeMarker templating
 */
@Configuration
public class FreeMarkerConfig {

  @Value("${freemarker.delayfornewtemplates}")
  private long delayForNewTemplates;

  /**
   * The bean to store FreeMarker templates in MongoDB
   *
   * @return the loader to store FreeMarker templates in MongoDB
   */
  @Bean
  public FreeMarkerTemplateLoader templateLoader() {
    return new FreeMarkerTemplateLoader();
  }

  /**
   * The FreeMarker configuration
   *
   * @return the FreeMarker configuration
   */
  @Bean
  public freemarker.template.Configuration configuration() {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(
        freemarker.template.Configuration.VERSION_2_3_25);
    configuration.setTemplateLoader(templateLoader());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    configuration.setLogTemplateExceptions(false);
    configuration.setTemplateUpdateDelayMilliseconds(delayForNewTemplates);
    return configuration;
  }
}

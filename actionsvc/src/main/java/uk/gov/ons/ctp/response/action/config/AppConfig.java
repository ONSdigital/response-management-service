package uk.gov.ons.ctp.response.action.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties
@Data
public class AppConfig {
  private CaseFrameSvc caseFrameSvc;
}

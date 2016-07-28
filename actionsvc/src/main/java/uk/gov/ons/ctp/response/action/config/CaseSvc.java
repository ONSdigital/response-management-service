package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

/**
 * App config POJO for Case service access - host/location and endpoint locations
 *
 */
@Data
public class CaseSvc {
  private String scheme;
  private String host;
  private String port;
  private String caseByCaseGetPath;
  private String questionnairesByCaseGetPath;
  private String caseEventsByCaseGetPath;
  private String caseEventsByCasePostPath;
  private String addressByUprnGetPath;
  private String caseByStatusAndActionPlanPath;
}

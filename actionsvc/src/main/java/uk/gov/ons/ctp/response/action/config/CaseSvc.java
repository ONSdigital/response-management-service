package uk.gov.ons.ctp.response.action.config;

import lombok.Data;
import uk.gov.ons.ctp.common.rest.RestClientConfig;

/**
 * App config POJO for Case service access - host/location and endpoint locations
 *
 */
@Data
public class CaseSvc {
  private RestClientConfig connectionConfig;
  private String caseByCaseGetPath;
  private String questionnairesByCaseGetPath;
  private String caseEventsByCaseGetPath;
  private String caseEventsByCasePostPath;
  private String addressByUprnGetPath;
  private String caseByStatusAndActionPlanPath;
}

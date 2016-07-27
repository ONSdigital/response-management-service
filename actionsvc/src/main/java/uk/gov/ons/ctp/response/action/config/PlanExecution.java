package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

/**
 * Config POJO for action plan exec params
 *
 */
@Data
public class PlanExecution {
  private Integer retrySleepSeconds;
  private Integer initialDelaySeconds;
  private Integer subsequentDelaySeconds;
}

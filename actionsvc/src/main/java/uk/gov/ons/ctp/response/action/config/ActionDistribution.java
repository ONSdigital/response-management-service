package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

/**
 * Config POJO for distribition params
 *
 */
@Data
public class ActionDistribution {
  private Integer instructionMax;
  private Integer retrySleepSeconds;
  private Integer initialDelaySeconds;
  private Integer subsequentDelayMilliseconds;
}

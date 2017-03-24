package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

/**
 * Config POJO for distribition params
 *
 */
@Data
public class ActionDistribution {
  private Integer retrievalMax;
  private Integer distributionMax;
  private Integer retrySleepSeconds;
  private Integer delayMilliSeconds;
}

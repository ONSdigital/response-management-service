package uk.gov.ons.ctp.response.action.scheduled.plan;

import lombok.Data;

/**
 * info regarding the last action distribution to handlers
 *
 */
@Data
public class PlanExecutionInfo {

  private String lastRunTime;


}

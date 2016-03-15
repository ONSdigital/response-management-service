package uk.gov.ons.ctp.response.action.representation;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Domain model object for representation.
 */
@Data
public class ActionPlanJobDTO {
  private Integer actionPlanJobId;
  private Integer actionPlanId;
  private String createdBy;
  private String state;
  private Timestamp createdDatetime;
  private Timestamp updatedDateTime;
}

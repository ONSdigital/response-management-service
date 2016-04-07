package uk.gov.ons.ctp.response.action.representation;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * Domain model object for representation.
 */
@Data
public class ActionPlanJobDTO {
  private Integer actionPlanJobId;
  private Integer actionPlanId;
  @NotNull @Size(min=2, max=20)
  private String createdBy;
  private String state;
  private Timestamp createdDatetime;
  private Timestamp updatedDateTime;
}

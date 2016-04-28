package uk.gov.ons.ctp.response.action.representation;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model object for representation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ActionPlanJobDTO {  

  public enum ActionPlanJobState {
    SUBMITTED, STARTED, COMPLETED, FAILED;
  }

  private Integer actionPlanJobId;
  private Integer actionPlanId;
  @NotNull @Size(min=2, max=20)
  private String createdBy;
  private String state;
  private Timestamp createdDatetime;
  private Timestamp updatedDateTime;
}

package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import javax.validation.constraints.NotNull;

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
public class ActionDTO {

  public enum ActionState {
    SUBMITTED, PENDING, ACTIVE, COMPLETED, CANCEL_SUBMITTED, CANCEL_PENDING, CANCELLING, CANCELLED, ABORTED;
  }
  public enum ActionEvent {
    REQUEST_DISTRIBUTED, REQUEST_FAILED, REQUEST_ACCEPTED, REQUEST_COMPLETED,
    REQUEST_CANCELLED, CANCELLATION_DISTRIBUTED, CANCELLATION_FAILED, CANCELLATION_ACCEPTED, CANCELLATION_COMPLETED
  }

  private Integer actionId;

  @NotNull
  private Integer caseId;

  private Integer actionPlanId;

  private Integer actionRuleId;

  @NotNull
  private String actionTypeName;

  @NotNull
  private String createdBy;

  private Boolean manuallyCreated;

  private Integer priority = 3;

  private String situation;

  private ActionState state;

  private Date createdDateTime;

  private Date updatedDateTime;

}

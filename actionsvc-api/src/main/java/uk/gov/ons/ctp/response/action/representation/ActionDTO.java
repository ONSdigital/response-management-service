package uk.gov.ons.ctp.response.action.representation;

import java.math.BigInteger;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static uk.gov.ons.ctp.common.time.DateTimeUtil.DATE_FORMAT_IN_JSON;

/**
 * Domain model object for representation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ActionDTO {
  /**
   * enum for action state
   */
  public enum ActionState {
    SUBMITTED, PENDING, ACTIVE, COMPLETED, DECLINED, CANCEL_SUBMITTED, CANCEL_PENDING, CANCELLING, CANCELLED, ABORTED;
  }

  /**
   * enum for action event
   */
  public enum ActionEvent {
    REQUEST_DISTRIBUTED, REQUEST_FAILED, REQUEST_ACCEPTED, REQUEST_COMPLETED, REQUEST_DECLINED, REQUEST_COMPLETED_DEACTIVATE, REQUEST_COMPLETED_DISABLE,
    REQUEST_CANCELLED, CANCELLATION_DISTRIBUTED, CANCELLATION_FAILED, CANCELLATION_ACCEPTED, CANCELLATION_COMPLETED
  }

  private BigInteger actionId;

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

  @JsonFormat(pattern = DATE_FORMAT_IN_JSON)
  private Date createdDateTime;

  @JsonFormat(pattern = DATE_FORMAT_IN_JSON)
  private Date updatedDateTime;

}

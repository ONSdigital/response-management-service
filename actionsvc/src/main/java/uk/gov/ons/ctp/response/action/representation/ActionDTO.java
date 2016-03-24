package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionState;

/**
 * Domain model object for representation.
 */
@Data
public class ActionDTO {

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

package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Domain model object for representation.
 */
@Data
public class ActionDTO {

  private Integer actionId;

  @NotNull
  private Integer caseId;

  @NotNull
  private Integer actionPlanId;

  @NotNull
  private Integer actionRuleId;

  @NotNull
  private String actionTypeName;

  @NotNull
  private String createdBy;


  private Boolean manuallyCreated;

  @NotNull
  private String priority;

  private String situation;

  @NotNull
  private String state;

  private Date createdDateTime;

  private Date updatedDateTime;

}

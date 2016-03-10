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

  private Integer actionPlanId;

  @NotNull
  private Integer actionRuleId;

  private String actionTypeName;

  @NotNull
  private String createdBy;

  private Boolean manuallyCreated;

  private String priority;

  private String situation;

  private String state;

  private Date createdDatetime;

  private Date updatedDatetime;

}

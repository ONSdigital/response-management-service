package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import lombok.Data;

/**
 * Domain model object for representation.
 */
@Data
public class ActionDTO {

  private Integer actionId;

  private Integer caseId;

  private Integer actionPlanId;

  private Integer actionRuleId;

  private Integer actionTypeId;

  private String createdBy;

  private Boolean manuallyCreated;

  private String priority;

  private String situation;

  private String state;

  private Date createdDatetime;

  private Date updatedDatetime;


}

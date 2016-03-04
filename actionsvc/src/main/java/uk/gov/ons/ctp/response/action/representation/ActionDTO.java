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

  private String actionStatus;

  private String actionType;

  private String priority;

  private String situation;

  private Date createdDatetime;

  private String createdBy;

}

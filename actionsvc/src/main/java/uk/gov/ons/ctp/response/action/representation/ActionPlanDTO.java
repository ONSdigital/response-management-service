package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import lombok.Data;

/**
 * Domain model object for representation.
 */
@Data
public class ActionPlanDTO {


  private Integer actionPlanId;

  private Integer surveyId;

  private String name;

  private String description;


  private String createdBy;

  private Date createdDatetime;

  private Date lastGoodRunDatetime;

}

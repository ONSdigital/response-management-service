package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

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
public class ActionPlanDTO {

  private Integer actionPlanId;

  private Integer surveyId;

  private String name;

  private String description;

  private String createdBy;

  private Date createdDateTime;

  private Date lastGoodRunDateTime;

}

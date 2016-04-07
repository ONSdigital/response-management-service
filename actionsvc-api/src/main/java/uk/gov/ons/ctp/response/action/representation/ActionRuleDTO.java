package uk.gov.ons.ctp.response.action.representation;

import lombok.Data;

/**
 * Domain model object for representation.
 */
@Data
public class ActionRuleDTO {

  private Integer actionRuleId;
  private Integer actionPlanId;
  private Integer priority;
  private Integer surveyDateDaysOffset;

  private String actionTypeName;
  private String name;
  private String description;
}

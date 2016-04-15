package uk.gov.ons.ctp.response.action.representation;

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
public class ActionRuleDTO {

  private Integer actionRuleId;
  private Integer actionPlanId;
  private Integer priority;
  private Integer surveyDateDaysOffset;

  private String actionTypeName;
  private String name;
  private String description;
}

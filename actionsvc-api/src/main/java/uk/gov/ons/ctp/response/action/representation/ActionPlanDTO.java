package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Domain model object for representation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ActionPlanDTO {

  @NotNull
  private Integer actionPlanId;

  private Integer surveyId;

  private String name;

  private String description;

  private String createdBy;

  private Date lastRunDateTime;

}

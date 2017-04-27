package uk.gov.ons.ctp.response.action.representation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static uk.gov.ons.ctp.common.time.DateTimeUtil.DATE_FORMAT_IN_JSON;

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

  @JsonFormat(pattern = DATE_FORMAT_IN_JSON)
  private Date lastRunDateTime;

}

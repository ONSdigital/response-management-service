package uk.gov.ons.ctp.response.action.representation;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import java.sql.Timestamp;

/**
 * Domain model object for representation.
 */
@Data
public class ActionPlanJobDTO {
  private Integer actionPlanJobId;
  private Integer actionPlanId;
  @NotBlank
  private String createdBy;
  private String state;
  private Timestamp createdDatetime;
  private Timestamp updatedDateTime;
}

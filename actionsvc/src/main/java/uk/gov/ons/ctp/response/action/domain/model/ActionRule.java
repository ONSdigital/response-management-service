package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model object.
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "actionrule", schema = "action")
public class ActionRule implements Serializable {

  private static final long serialVersionUID = 4524689072566205066L;

  @Id
  @Column(name = "actionruleid")
  private Integer actionRuleId;

  @Column(name = "actionplanid")
  private Integer actionPlanId;

  private Integer priority;

  @Column(name = "surveydatedaysoffset")
  private Integer surveyDateDaysOffset;

  @ManyToOne
  @JoinColumn(name = "actiontypeid")
  private ActionType actionType;

  private String name;

  private String description;
}

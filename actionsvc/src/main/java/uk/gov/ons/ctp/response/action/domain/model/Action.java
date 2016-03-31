package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model object.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "action", schema = "action")
public class Action implements Serializable {

  private static final long serialVersionUID = 8539984354009320104L;

  public enum ActionState {
    ACTIVE, CANCELLED, CANCELSUBMITTED, COMPLETED, FAILED, PENDING, SUBMITTED;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "actionid")
  private Integer actionId;

  @Column(name = "caseid")
  private Integer caseId;

  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "actionruleid")
  private Integer actionRuleId;

  @Column(name = "actiontypeid")
  private Integer actionTypeId;

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "manuallycreated")
  private Boolean manuallyCreated;

  private Integer priority;

  private String situation;

  @Enumerated(EnumType.STRING)
  private ActionState state;

  @Column(name = "createddatetime")
  private Timestamp createdDateTime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;

}

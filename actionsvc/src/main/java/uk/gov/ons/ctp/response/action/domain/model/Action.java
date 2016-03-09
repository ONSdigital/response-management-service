package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "action", schema = "action")
public class Action implements Serializable {

  private static final long serialVersionUID = 8539984354009320104L;

  @Id
  @GeneratedValue
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

  @Column(name = "createdBy")
  private String createdBy;

  @Column(name = "manuallycreated")
  private Boolean manuallyCreated;

  private String priority;

  private String situation;

  private String state;

  @Column(name = "createddatetime")
  private Timestamp createdDatetime;

  private Timestamp updatedDatetime;

}

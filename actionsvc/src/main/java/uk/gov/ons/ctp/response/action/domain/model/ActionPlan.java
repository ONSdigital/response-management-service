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
@Table(name = "actionplan", schema = "caseframe")
public class ActionPlan implements Serializable {
  
  private static final long serialVersionUID = 3621028547635970347L;

  @Id
  @GeneratedValue
  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "surveyid")
  private Integer surveyId;

  private String name;

  private String description;


  @Column(name = "createdBy")
  private String createdBy;

  @Column(name = "createddatetime")
  private Timestamp createdDatetime;

  @Column(name = "lastgoodrundatetime")
  private Timestamp lastGoodRunDatetime;

}

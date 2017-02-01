package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "actionplan", schema = "action")
public class ActionPlan implements Serializable {

  private static final long serialVersionUID = 3621028547635970347L;

  @Id
  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "surveyid")
  private Integer surveyId;

  private String name;

  private String description;

  @Column(name = "createdby")
  private String createdBy;

  @Column(name = "lastrundatetime")
  private Timestamp lastRunDateTime;

}

package uk.gov.ons.ctp.response.action.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Domain model object.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "actionplanjob", schema = "action")
public class ActionPlanJob {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO, generator="actionplanjobseq_gen")
  @SequenceGenerator(name="actionplanjobseq_gen", sequenceName="action.actionplanjobseq")
  @Column(name = "actionplanjobid")
  private Integer actionPlanJobId;

  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "createdby")
  private String createdBy;

  private String state;

  @Column(name = "createddatetime")
  private Timestamp createdDatetime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;
}

package uk.gov.ons.ctp.response.action.domain.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;

/**
 * Domain model object.
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "actionplanjob", schema = "action")
public class ActionPlanJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "actionplanjobseq_gen")
  @GenericGenerator(
      name = "actionplanjobseq_gen",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
              @Parameter(name = "sequence_name", value = "action.actionplanjobseq"),
              @Parameter(name = "initial_value", value = "0"),
              @Parameter(name = "increment_size", value = "1")
      }
  )
  @Column(name = "actionplanjobid")
  private Integer actionPlanJobId;

  @Column(name = "actionplanid")
  private Integer actionPlanId;

  @Column(name = "createdby")
  private String createdBy;

  @Enumerated(EnumType.STRING)
  private ActionPlanJobDTO.ActionPlanJobState state;

  @Column(name = "createddatetime")
  private Timestamp createdDateTime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;
}

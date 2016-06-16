package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "case", schema = "action")
@IdClass(ActionCaseCompositeKey.class)
@NamedStoredProcedureQuery(name = "createactions", procedureName = "action.createactions", parameters = {
    @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_actionplanjobid", type = Integer.class),
    @StoredProcedureParameter(mode = ParameterMode.OUT, name = "success", type = Boolean.class) })
public class ActionCase implements Serializable {

  private static final long serialVersionUID = 7970373271889255844L;

  @Id
  @Column(name = "actionplanjobid")
  private Integer actionPlanJobId;

  @Id
  @Column(name = "caseid")
  private Integer caseId;

}

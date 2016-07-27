package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;

import javax.persistence.IdClass;

import lombok.Data;

/**
 * Class used as a composite key for action.case
 * Note the ids are duplicated in the ActionCAse class anyway
 * I guess JPA uses a hash of an instance of this class for its primary key work
 * but presents the ids individually in the ActionCase for everything else.
 *
 */
@IdClass(ActionCaseCompositeKey.class)
@Data
public class ActionCaseCompositeKey implements Serializable {
  private static final long serialVersionUID = -2090516162724563787L;
  private Integer actionPlanId;
  private Integer caseId;
}

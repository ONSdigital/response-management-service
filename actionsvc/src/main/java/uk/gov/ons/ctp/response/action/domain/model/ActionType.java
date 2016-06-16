package uk.gov.ons.ctp.response.action.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "actiontype", schema = "action")
public class ActionType implements Serializable {

  private static final long serialVersionUID = -581549382631976704L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "actiontypeid")
  private Integer actionTypeId;

  private String name;
  private String description;
  private String handler;

  @Column(name = "cancancel")
  private Boolean canCancel;

}

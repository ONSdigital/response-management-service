package uk.gov.ons.ctp.response.action.export.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain entity representing a Template.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "template", schema = "actionexport")
public class TemplateExpression {

  @Id
  private String name;
  private String content;
  @Column(name = "datemodified")
  private Date dateModified;
}

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
@Table(name = "survey", schema = "action")
public class Survey implements Serializable {

  @Id
  @Column(name = "surveyid")
  private Integer surveyId;

  @Column(name = "name")
  private String name;

  @Column(name = "surveystartdate")
  private Timestamp surveyStartDate;

  @Column(name = "surveyenddate")
  private Timestamp surveyEndDate;
}

package uk.gov.ons.ctp.response.report.domain.model;


import java.util.Date;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report")
public class Report {

  @Id
  @Column(name = "reportid")
  private Integer reportId;

  @Column(name = "reporttype")
  private String reportType;

  private String contents;

  @Column(name = "createddatetime")
  private Date createdDateTime;

  public Report(int reportId, String reportType, Date createdDateTime) {
    this.reportId = reportId;
    this.reportType = reportType;
    this.createdDateTime = createdDateTime;    
  }
}

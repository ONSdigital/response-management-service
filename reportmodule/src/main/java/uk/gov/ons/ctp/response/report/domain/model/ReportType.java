package uk.gov.ons.ctp.response.report.domain.model;


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
@Table(name = "reporttype")
public class ReportType {

  @Id
  @Column(name = "reporttypeid")
  private Integer reportTypeId;

  @Column(name = "reporttype")
  private String reportType;

  @Column(name = "orderid")
  private Integer orderId;
  
  @Column(name = "displayname")
  private String displayName;
  
}

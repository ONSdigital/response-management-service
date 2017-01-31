package uk.gov.ons.ctp.response.report.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSummary {

  private Integer reportId;
  
  private String reportType;
  
  private Date createdDateTime;

}

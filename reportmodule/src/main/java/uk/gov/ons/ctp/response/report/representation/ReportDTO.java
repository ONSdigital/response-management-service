package uk.gov.ons.ctp.response.report.representation;


import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ReportDTO {

  private Integer reportId;
  
  private String reportType;
  
  private String contents;
  
  private Date createdDateTime;
  
}

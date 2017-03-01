package uk.gov.ons.ctp.response.report.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.report.domain.model.Report;
import uk.gov.ons.ctp.response.report.domain.model.ReportSummary;
import uk.gov.ons.ctp.response.report.domain.model.ReportType;

public interface ReportService extends CTPService {

  /**
   * find all available report types
   *
   * @return List of report types
   */
  List<ReportType> findTypes();

  /**
   * Find reports by reportType.
   *
   * @param reportType String enum
   * @return Report list object or null
   */
  List<ReportSummary> getReportSummary(String reportType);

  /**
   * Find Report entity by reportId.
   *
   * @param reportId Integer
   * @return Report object or null
   */
  Report findByReportId(Integer reportId);

}

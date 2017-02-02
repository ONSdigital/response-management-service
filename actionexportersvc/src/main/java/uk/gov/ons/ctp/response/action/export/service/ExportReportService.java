package uk.gov.ons.ctp.response.action.export.service;

import uk.gov.ons.ctp.response.action.export.domain.ExportReport;

/**
 * Service responsible for dealing with ExportReports
 */
public interface ExportReportService {

  /**
   * Save an ExportReport
   *
   * @param exportReport the ExportReport to save.
   * @return the ExportReport saved.
   */
  ExportReport save(final ExportReport exportReport);

  /**
   * Create a report entry for files created since last report run.
   * 
   */
  boolean createReport();
}

package uk.gov.ons.ctp.response.action.export.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import uk.gov.ons.ctp.response.action.export.domain.ExportReport;
import uk.gov.ons.ctp.response.action.export.repository.ExportReportRepository;
import uk.gov.ons.ctp.response.action.export.service.ExportReportService;

/**
 * The implementation of FileRowCountService
 */
@Named
public class ExportReportServiceImpl implements ExportReportService {

  @Inject
  private ExportReportRepository exportReportRepo;

  @Override
  public ExportReport save(ExportReport exportReport) {
    return exportReportRepo.save(exportReport);
  }

  @Override
  public boolean createReport() {
    return exportReportRepo.createReport();
  }

}

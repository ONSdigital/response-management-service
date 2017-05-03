package uk.gov.ons.ctp.response.action.export.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.response.action.export.domain.ExportReport;
import uk.gov.ons.ctp.response.action.export.repository.ExportReportRepository;
import uk.gov.ons.ctp.response.action.export.service.ExportReportService;

/**
 * The implementation of FileRowCountService
 */
@Service
public class ExportReportServiceImpl implements ExportReportService {

  @Autowired
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

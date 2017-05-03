package uk.gov.ons.ctp.response.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.response.report.domain.model.Report;
import uk.gov.ons.ctp.response.report.domain.model.ReportSummary;
import uk.gov.ons.ctp.response.report.domain.model.ReportType;
import uk.gov.ons.ctp.response.report.domain.repository.ReportRepository;
import uk.gov.ons.ctp.response.report.domain.repository.ReportTypeRepository;
import uk.gov.ons.ctp.response.report.service.ReportService;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

  /**
   * Spring Data Repository for CSV Report entities.
   */
    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportTypeRepository reportTypeRepository;
    
    /**
     * find all available report types
     *
     * @return List of report types
     */
    public List<ReportType> findTypes() {
      List<ReportType> reportTypes = reportTypeRepository.findReportTypeByOrderByOrderId();
      return reportTypes;
    }

    /**
     * Find reportSummary by reportType.
     *
     * @param reportType String enum
     * @return Report list object or null
     */
    @Override
    public List<ReportSummary> getReportSummary(final String reportType) {
      log.debug("Entering findReportDatesByReportType with {}", reportType);
      return reportRepository.getReportSummary(reportType);
    }

    /**
     * Find Report entity by reportId.
     *
     * @param reportId Integer
     * @return Report object or null
     */
    @Override
    public Report findByReportId(final Integer reportId) {
      log.debug("Entering findByReportTypeAndReportDate with {}", reportId);
      return reportRepository.findOne(reportId);
    }

}

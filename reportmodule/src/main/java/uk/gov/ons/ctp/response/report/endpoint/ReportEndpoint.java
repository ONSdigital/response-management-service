package uk.gov.ons.ctp.response.report.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.report.ReportBeanMapper;
import uk.gov.ons.ctp.response.report.domain.model.Report;
import uk.gov.ons.ctp.response.report.domain.model.ReportSummary;
import uk.gov.ons.ctp.response.report.domain.model.ReportType;
import uk.gov.ons.ctp.response.report.representation.ReportDTO;
import uk.gov.ons.ctp.response.report.representation.ReportSummaryDTO;
import uk.gov.ons.ctp.response.report.service.ReportService;

/**
 * The REST endpoint controller for CaseSvc Reports
 */
@RestController
@RequestMapping(value = "/reports", produces = "application/json")
@Slf4j
public final class ReportEndpoint implements CTPEndpoint {

  public static final String ERRORMSG_REPORTNOTFOUND = "Report not found for";
  public static final String ERRORMSG_REPORTSNOTFOUND = "Reports not found for";

  @Autowired
  private ReportService reportService;
  
  private MapperFacade mapperFacade = new ReportBeanMapper ();

  /**
   * the GET endpoint to find all available report types
   *
   * @return List of report types
   * @throws CTPException something went wrong
   */
  @RequestMapping(value = "/types", method = RequestMethod.GET)
  public List<ReportType> findReportTypes() throws CTPException {
    log.info("Finding Report Types");
    List<ReportType> reportTypes = reportService.findTypes();

    if (reportTypes == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Report types not found");
    }
    
    return reportTypes;
  }

  /**
   * the GET endpoint to find list of report dates by reporttype
   *
   * @param reportType to find by
   * @return list of report dates by reportType
   * @throws CTPException something went wrong
   */
  @RequestMapping(value = "/types/{reportType}", method = RequestMethod.GET)
  public List<ReportSummaryDTO> findReportDatesByReportType(@PathVariable("reportType") final String reportType) throws CTPException {
    log.info("Entering findReportDatesByReportType with {}", reportType);

    List<ReportSummary> reports = reportService.getReportSummary(reportType);
    List<ReportSummaryDTO> reportList = mapperFacade.mapAsList(reports, ReportSummaryDTO.class);

    if (reportList == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
          String.format("%s report type %s", ERRORMSG_REPORTSNOTFOUND, reportType));
    }

    return reportList;
  }

  /**
   * the GET endpoint to find report by reporttype and reportdate
   *
   * @param reportId to find by
   * @return the report found
   * @throws CTPException something went wrong
   */
  @RequestMapping(value = "/{reportId}", method = RequestMethod.GET)
  public ReportDTO findReportByReportId(@PathVariable("reportId") final int reportId) throws CTPException {
    log.info("Entering findReportByReportId with {}", reportId);

    Report report = reportService.findByReportId(reportId);
    ReportDTO reportDTO = mapperFacade.map(report, ReportDTO.class);
    
    if (report == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
          String.format("%s report type %s", ERRORMSG_REPORTNOTFOUND, reportId));
    }
    return reportDTO;
  }

}

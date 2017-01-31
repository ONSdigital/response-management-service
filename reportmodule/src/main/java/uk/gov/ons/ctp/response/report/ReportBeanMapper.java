package uk.gov.ons.ctp.response.report;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import uk.gov.ons.ctp.response.report.domain.model.ReportSummary;
import uk.gov.ons.ctp.response.report.domain.model.ReportType;
import uk.gov.ons.ctp.response.report.representation.ReportSummaryDTO;
import uk.gov.ons.ctp.response.report.representation.ReportTypeDTO;

/**
 * The bean mapper that maps to/from DTOs and JPA entity types.
 *
 */
public class ReportBeanMapper extends ConfigurableMapper {

  /**
   * Setup the mapper for all of our beans. Only fields having non  identical names need
   * mapping if we also use byDefault() following.
   * @param factory the factory to which we add our mappings
   */
  protected final void configure(final MapperFactory factory) {

    factory
    .classMap(ReportSummary.class, ReportSummaryDTO.class)
    .byDefault()
    .register();

    factory
    .classMap(ReportType.class, ReportTypeDTO.class)
    .byDefault()
    .register();

  }
}

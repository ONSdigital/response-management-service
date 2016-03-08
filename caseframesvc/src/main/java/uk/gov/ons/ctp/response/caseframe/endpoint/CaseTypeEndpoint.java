package uk.gov.ons.ctp.response.caseframe.endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.caseframe.domain.model.CaseType;
import uk.gov.ons.ctp.response.caseframe.representation.CaseTypeDTO;
import uk.gov.ons.ctp.response.caseframe.service.CaseTypeService;

/**
 * The REST endpoint controller for CaseType
 */
@Path("/casetypes")
@Produces({ "application/json" })
@Slf4j
public final class CaseTypeEndpoint implements CTPEndpoint {

  @Inject
  private CaseTypeService caseTypeService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * the GET endpoint to retrieve all casetypes
   * @return the list of case types
   */
  @GET
  @Path("/")
  public List<CaseTypeDTO> findCaseTypes() {
    log.debug("Entering findCaseTypes...");
    List<CaseType> caseTypes = caseTypeService.findCaseTypes();
    List<CaseTypeDTO> caseTypeDTOs = mapperFacade.mapAsList(caseTypes, CaseTypeDTO.class);
    return CollectionUtils.isEmpty(caseTypeDTOs) ? null : caseTypeDTOs;
  }

  /**
   * the GET endpoint to find a casetype by id
   * @param caseTypeId to find by
   * @return the casetype or null if not found
   * @throws CTPException something went wrong
   */
  @GET
  @Path("/{casetypeid}")
  public CaseTypeDTO findCaseTypeByCaseTypeId(@PathParam("casetypeid") final Integer caseTypeId) throws CTPException {
    log.debug("Entering findCaseTypeByCaseTypeId with {}", caseTypeId);
    CaseType caseType = caseTypeService.findCaseTypeByCaseTypeId(caseTypeId);
    if (caseType == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "CaseType not found for id %s", caseTypeId);
    }
    return mapperFacade.map(caseType, CaseTypeDTO.class);
  }

}
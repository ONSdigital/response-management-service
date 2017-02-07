package uk.gov.ons.ctp.response.action.export.endpoint;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMapping;
import uk.gov.ons.ctp.response.action.export.representation.TemplateMappingDTO;
import uk.gov.ons.ctp.response.action.export.service.TemplateMappingService;

/**
 * The REST endpoint controller for TemplateMappings.
 */
@Path("/templatemappings")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class TemplateMappingEndpoint {
  @Inject
  private TemplateMappingService templateMappingService;

  @Inject
  private MapperFacade mapperFacade;

  @Context
  private UriInfo uriInfo;

  /**
   * To retrieve all TemplateMappings
   *
   * @return a list of TemplateMappings
   */
  @GET
  public List<TemplateMappingDTO> findAllTemplateMappings() {
    log.debug("Entering findAllTemplateMappings ...");
    List<TemplateMapping> templateMappings = templateMappingService
        .retrieveAllTemplateMappings();
    List<TemplateMappingDTO> results = mapperFacade.mapAsList(templateMappings,
        TemplateMappingDTO.class);
    return CollectionUtils.isEmpty(results) ? null : results;
  }

  /**
   * To retrieve a specific TemplateMapping
   *
   * @param actionType for the specific TemplateMapping to retrieve
   * @return the specific TemplateMapping
   * @throws CTPException if no TemplateMapping found
   */
  @GET
  @Path("/{actionType}")
  public TemplateMappingDTO findTemplateMapping(
      @PathParam("actionType") final String actionType) throws CTPException {
    log.debug("Entering findTemplateMapping with {}", actionType);
    TemplateMapping result = templateMappingService.retrieveTemplateMappingByActionType(actionType);
    return mapperFacade.map(result, TemplateMappingDTO.class);
  }

  /**
   * To store TemplateMappings
   *
   * @param fileContents the TemplateMapping content
   * @return 201 if created
   * @throws CTPException if the TemplateMapping can't be stored
   */
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response storeTemplateMapping(@FormDataParam("file") InputStream fileContents) throws CTPException {
    log.debug("Entering storeTemplateMapping");
    List<TemplateMapping> mappings = templateMappingService.storeTemplateMappings(fileContents);

    UriBuilder ub = uriInfo.getAbsolutePathBuilder();
    URI templateMappingUri = ub.build();
    List<TemplateMappingDTO> results = mapperFacade.mapAsList(mappings,
        TemplateMappingDTO.class);
    return Response.created(templateMappingUri).entity(results).build();
  }
}

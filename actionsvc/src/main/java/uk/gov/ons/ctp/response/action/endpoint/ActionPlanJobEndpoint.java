package uk.gov.ons.ctp.response.action.endpoint;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The REST endpoint controller for ActionPlanJobs.
 */
@Path("/actionplans")
@Produces({ MediaType.APPLICATION_JSON})
@Slf4j
public class ActionPlanJobEndpoint implements CTPEndpoint {

  @Inject
  private ActionPlanJobService actionPlanJobService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * This method returns the associated action plan job for the specified action plan job id.
   *
   * @param actionPlanJobId This is the action plan job id
   * @return ActionPlanJobDTO This returns the associated action plan job for the specified action plan job id.
   * @throws CTPException if no action plan job found for the specified action plan job id.
   */
  @GET
  @Path("/jobs/{actionplanjobid}")
  public final ActionPlanJobDTO findActionPlanJobById(@PathParam("actionplanjobid") final Integer actionPlanJobId)
      throws CTPException {
    log.debug("Entering findActionPlanJobById with {}", actionPlanJobId);
    ActionPlanJob actionPlanJob = actionPlanJobService.findActionPlanJob(actionPlanJobId);
    if (actionPlanJob == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "ActionPlanJob not found for id %s", actionPlanJobId);
    }
    return mapperFacade.map(actionPlanJob, ActionPlanJobDTO.class);
  }
}

package uk.gov.ons.ctp.response.action.endpoint;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.util.CollectionUtils;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The REST endpoint controller for ActionPlanJobs.
 */
@Path("/actionplans")
@Consumes({ MediaType.APPLICATION_JSON})
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

  /**
   * Returns all action plan jobs for the given action plan id.
   * @param actionPlanId the given action plan id.
   * @return Returns all action plan jobs for the given action plan id.
   * @throws CTPException
   */
  @GET
  @Path("/{actionplanid}/jobs")
  public final List<ActionPlanJobDTO> findAllActionPlanJobsByActionPlanId(@PathParam("actionplanid") final Integer
      actionPlanId) throws CTPException {
    log.debug("Entering findAllActionPlanJobsByActionPlanId with {}", actionPlanId);
    List<ActionPlanJob> actionPlanJobs = actionPlanJobService.findActionPlanJobsForActionPlan(actionPlanId);
    List<ActionPlanJobDTO> actionPlanJobDTOs = mapperFacade.mapAsList(actionPlanJobs, ActionPlanJobDTO.class);
    return CollectionUtils.isEmpty(actionPlanJobDTOs) ? null : actionPlanJobDTOs;
  }

  @POST
  @Path("/{actionplanid}/jobs")
  public final ActionPlanJobDTO executeActionPlan(@PathParam("actionplanid") final Integer actionPlanId,
      final ActionPlanJobDTO requestObject) throws CTPException {
    log.debug("Entering executeActionPlan with {}", actionPlanId);

    if (requestObject == null) {
      throw new CTPException(CTPException.Fault.VALIDATION_FAILED, "Provided json is incorrect.");
    }

    ActionPlanJob actionPlanJob = actionPlanJobService.executeActionPlan(actionPlanId, requestObject);
    if (actionPlanJob == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "ActionPlan not found for id %s", actionPlanId);
    }
    return mapperFacade.map(actionPlanJob, ActionPlanJobDTO.class);
  }
}

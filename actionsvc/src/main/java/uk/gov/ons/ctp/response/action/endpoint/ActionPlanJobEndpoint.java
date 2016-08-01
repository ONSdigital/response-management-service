package uk.gov.ons.ctp.response.action.endpoint;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

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
    Optional<ActionPlanJob> actionPlanJob = actionPlanJobService.findActionPlanJob(actionPlanJobId);
    return mapperFacade.map(actionPlanJob.orElseThrow(() -> new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
            "ActionPlanJob not found for id %d", actionPlanJobId)), ActionPlanJobDTO.class);
  }

  /**
   * Returns all action plan jobs for the given action plan id.
   * @param actionPlanId the given action plan id.
   * @return Returns all action plan jobs for the given action plan id.
   * @throws CTPException summats went wrong
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

  /**
   * To create a new Action Plan Job having received an action plan id and some json
   * @param actionPlanId the given action plan id.
   * @param actionPlanJobDTO the ActionPlanJobDTO representation of the provided json
   * @return the created ActionPlanJobDTO
   * @throws CTPException summats went wrong
   */
  @POST
  @Path("/{actionplanid}/jobs")
  public final ActionPlanJobDTO executeActionPlan(@PathParam("actionplanid") final Integer actionPlanId,
      final @Valid ActionPlanJobDTO actionPlanJobDTO) throws CTPException {
    log.debug("Entering executeActionPlan with {}", actionPlanId);

    if (actionPlanJobDTO == null) {
      throw new CTPException(CTPException.Fault.VALIDATION_FAILED, "Provided json is incorrect.");
    }

    ActionPlanJob job = mapperFacade.map(actionPlanJobDTO, ActionPlanJob.class);
    job.setActionPlanId(actionPlanId);
    Optional<ActionPlanJob> actionPlanJob = actionPlanJobService.createAndExecuteActionPlanJob(job);
    return mapperFacade.map(actionPlanJob.orElseThrow(() -> new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
            "ActionPlan not found for id %s", actionPlanId)), ActionPlanJobDTO.class);
  }
}

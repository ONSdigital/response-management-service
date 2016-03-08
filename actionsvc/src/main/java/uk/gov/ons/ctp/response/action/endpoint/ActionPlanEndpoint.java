package uk.gov.ons.ctp.response.action.endpoint;

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
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;


/**
 * The REST endpoint controller for ActionPlans.
 */
@Path("/actionplans")
@Produces({"application/json"})
@Slf4j
public class ActionPlanEndpoint implements CTPEndpoint {

  @Inject
  private ActionPlanService actionPlanService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * This method returns all action plans.
   * @return List<ActionPlanDTO> This returns all action plans.
   */
  @GET
  @Path("/")
  public final List<ActionPlanDTO> findActionPlans() {
    log.debug("Entering findActionPlans...");
    List<ActionPlan> actionPlans = actionPlanService.findActionPlans();
    List<ActionPlanDTO> actionPlanDTOs = mapperFacade.mapAsList(actionPlans, ActionPlanDTO.class);
    return CollectionUtils.isEmpty(actionPlanDTOs) ? null : actionPlanDTOs;
  }

  /**
   * This method returns the associated action plan for the specified action plan id.
   * @param actionPlanId This is the action plan id
   * @return ActionPlanDTO This returns the associated action plan for the specified action plan id.
   * @throws CTPException if no action plan found for the specified action plan id.
   */
  @GET
  @Path("/{actionplanid}")
  public final ActionPlanDTO findActionPlanByActionPlanId(@PathParam("actionplanid") final Integer actionPlanId)
      throws CTPException {
    log.debug("Entering findActionPlanByActionPlanId with {}", actionPlanId);
    ActionPlan actionPlan = actionPlanService.findActionPlan(actionPlanId);
    if (actionPlan == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "ActionPlan not found for id %s", actionPlanId);
    }
    return mapperFacade.map(actionPlan, ActionPlanDTO.class);
  }
}
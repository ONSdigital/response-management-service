package uk.gov.ons.ctp.response.action.endpoint;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionRule;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.representation.ActionRuleDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;

/**
 * The REST endpoint controller for ActionPlans.
 */
@Path("/actionplans")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Slf4j
public class ActionPlanEndpoint implements CTPEndpoint {

  @Inject
  private ActionPlanService actionPlanService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * This method returns all action plans.
   *
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
   *
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

  /**
   * This method returns the associated action plan after it has been updated. Note that only the description and
   * the lastGoodRunDatetime can be updated.
   *
   * @param actionPlanId This is the action plan id
   * @param requestObject The object created by ActionPlanDTOMessageBodyReader from the json found in the request body
   * @return ActionPlanDTO This returns the updated action plan.
   * @throws CTPException if the json provided is incorrect or if the action plan id does not exist.
   */
  @PUT
  @Path("/{actionplanid}")
  public final ActionPlanDTO updateActionPlanByActionPlanId(@PathParam("actionplanid") final Integer actionPlanId,
      final ActionPlanDTO requestObject) throws CTPException {
    log.debug("UpdateActionPlanByActionPlanId with actionplanid {} - actionPlan {}", actionPlanId, requestObject);
    if (requestObject == null) {
      throw new CTPException(CTPException.Fault.VALIDATION_FAILED, "Provided json is incorrect.");
    }

    ActionPlan actionPlan = actionPlanService.updateActionPlan(actionPlanId,
        mapperFacade.map(requestObject, ActionPlan.class));
    if (actionPlan == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "ActionPlan not found for id %s", actionPlanId);
    } else {
      return mapperFacade.map(actionPlan, ActionPlanDTO.class);
    }
  }

  /**
   * This method creates a new action plan.
   * @param requestObject the action plan to be created
   * @return a not implemented response for 2016.
   */
  @POST
  @Path("/")
  public final Response createActionPlan(final ActionPlanDTO requestObject) {
    return Response.status(Response.Status.NOT_IMPLEMENTED).build();
  }

  /**
   * Returns all action rules for the given action plan id.
   * @param actionPlanId the action plan id
   * @return Returns all action rules for the given action plan id.
   * @throws CTPException
   */
  @GET
  @Path("/{actionplanid}/rules")
  public final List<ActionRuleDTO> returnActionRulesForActionPlanId(@PathParam("actionplanid") final Integer actionPlanId)
      throws CTPException {
    log.debug("Entering returnActionRulesForActionPlanId with {}", actionPlanId);
    ActionPlan actionPlan = actionPlanService.findActionPlan(actionPlanId);
    if (actionPlan == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "ActionPlan not found for id %s", actionPlanId);
    }

    List<ActionRule> actionRules = actionPlanService.findActionRulesForActionPlan(actionPlanId);
    List<ActionRuleDTO> actionRuleDTOs = mapperFacade.mapAsList(actionRules, ActionRuleDTO.class);
    return CollectionUtils.isEmpty(actionRuleDTOs) ? null : actionRuleDTOs;
  }

}

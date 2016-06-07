package uk.gov.ons.ctp.response.action.endpoint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * The REST endpoint controller for Actions.
 */
@Path("/actions")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@Slf4j
public final class ActionEndpoint implements CTPEndpoint {

  @Inject
  private ActionService actionService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * GET all Actions optionally filtered by ActionType and or state
   *
   * @param actionType Optional filter by ActionType
   * @param actionState Optional filter by Action state
   * @return List<ActionDTO> Actions for the specified filters
   */
  @GET
  @Path("/")
  public List<ActionDTO> findActions(@QueryParam("actiontype") final String actionType,
      @QueryParam("state") final ActionDTO.ActionState actionState) {

    List<Action> actions = null;

    if (actionType != null) {
      if (actionState != null) {
        log.debug("Entering findActionsByTypeAndState with {} {}", actionType, actionState);
        actions = actionService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(actionType, actionState);
      } else {
        log.debug("Entering findActionsByType with {}", actionType);
        actions = actionService.findActionsByType(actionType);
      }
    } else {
      if (actionState != null) {
        log.debug("Entering findActionsByState with {}", actionState);
        actions = actionService.findActionsByState(actionState);
      } else {
        actions = new ArrayList<Action>();
      }
    }

    List<ActionDTO> actionDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionDTOs) ? null : actionDTOs;
  }

  /**
   * POST Create an Action.
   *
   * @param actionDTO Incoming ActionDTO with details to validate and from which
   *          to create Action
   * @return ActionDTO Created Action
   * @throws CTPException on failure to create Action
   */
  @POST
  @Path("/")
  public ActionDTO createAction(final @Valid ActionDTO actionDTO) throws CTPException {
    log.debug("Entering createAction with Action {}", actionDTO);
    Action action = actionService.createAction(mapperFacade.map(actionDTO, Action.class));
    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
  }

  /**
   * GET the Action for the specified action id.
   *
   * @param actionId Action Id of requested Action
   * @return ActionDTO Returns the associated Action for the specified action
   * @throws CTPException if no associated Action found for the specified action
   *           Id.
   */
  @GET
  @Path("/{actionid}")
  public ActionDTO findActionByActionId(@PathParam("actionid") final Integer actionId) throws CTPException {
    log.debug("Entering findActionByActionId with {}", actionId);
    Action action = actionService.findActionByActionId(actionId);
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not found for id %s", actionId);
    }
    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
  }

  /**
   * PUT to update the specified Action.
   *
   * @param actionId Action Id of the Action to update
   * @param actionDTO Incoming ActionDTO with details to update
   * @return ActionDTO Returns the updated Action details
   * @throws CTPException if update operation fails
   */
  @PUT
  @Path("/{actionid}")
  public ActionDTO updateAction(@PathParam("actionid") final int actionId, final ActionDTO actionDTO)
      throws CTPException {
    log.debug("Updating Action with {} {}", actionId, actionDTO);
    actionDTO.setActionId(actionId);
    Action action = actionService.updateAction(mapperFacade.map(actionDTO, Action.class));
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not updated for id %s", actionId);
    }
    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
  }

  /**
   * PUT to cancel all the Actions for a specified caseId.
   *
   * @param caseId Case Id of the actions to cancel
   * @return List<ActionDTO> Returns a list of cancelled Actions
   * @throws CTPException if update operation fails
   */
  @PUT
  @Path("/case/{caseid}/cancel")
  public List<ActionDTO> cancelActions(@PathParam("caseid") final int caseId)
      throws CTPException {
    log.debug("Cancelling Actions for {}", caseId);
    List<Action> actions = actionService.cancelActions(caseId);

    List<ActionDTO> results = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(results) ? null : results;
  }

  /**
   * GET Actions for the specified case Id.
   *
   * @param caseId caseID to which Actions apply
   * @return List<ActionDTO> Returns the associated actions for the specified
   *         case id.
   */
  @GET
  @Path("/case/{caseid}")
  public List<ActionDTO> findActionsByCaseId(@PathParam("caseid") final Integer caseId) {
    log.debug("Entering findActionsByCaseId...");
    List<Action> actions = actionService.findActionsByCaseId(caseId);
    List<ActionDTO> actionDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionDTOs) ? null : actionDTOs;
  }

  /**
   * Allow feedback otherwise sent via JMS to be sent via endpoint
   * @param actionId the action
   * @param actionFeedback the feedback
   * @return the modified action
   * @throws CTPException oops
   */
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @PUT
  @Path("/{actionid}/feedback")
  public ActionDTO feedbackAction(@PathParam("actionid") final int actionId, final ActionFeedback actionFeedback)
      throws CTPException {
    log.debug("Feedback for Action {}", actionId);
    actionFeedback.setActionId(BigInteger.valueOf(actionId));
    Action action = actionService.feedBackAction(actionFeedback);
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not updated for id %s", actionId);
    }

    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
  }
}

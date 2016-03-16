package uk.gov.ons.ctp.response.action.endpoint;

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
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * The REST endpoint controller for Actions.
 */
@Path("/actions")
@Produces({ "application/json" })
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
   * @param state Optional filter by Action state
   * @return List<ActionDTO> Actions for the specified filters
   */
  @GET
  @Path("/")
  public List<ActionDTO> findActions(@QueryParam("actiontype") final String actionType,
      @QueryParam("state") final String state) {

    List<Action> actions = null;
    
    if (actionType != null) {
      if (state != null) {
        log.debug("Entering findActionsByTypeAndState with {} {}", actionType, state);
        actions = actionService.findActionsByTypeAndState(actionType, state);
      } else {
        log.debug("Entering findActionsByType with {}", actionType);
        actions = actionService.findActionsByType(actionType);
      }
    } else {
        if (state != null) {
          log.debug("Entering findActionsByState with {}", state);
          actions = actionService.findActionsByState(state);         
        } else {
          actions = new ArrayList<Action>();
        }
      }

    List<ActionDTO> actionDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionDTOs) ? null : actionDTOs;
  }

  /**
   * POST Create an Action. CaseId, actionTypeName and createdBy body parameters
   * are mandatory.
   *
   * @param actionDTO Incoming ActionDTO with details to validate and from which
   *          to create Action
   * @return ActionDTO Created Action
   * @throws CTPException on failure to create Action
   */
  @POST
  @Consumes({ MediaType.APPLICATION_JSON })
  @Path("/")
  public ActionDTO createAction(@Valid final ActionDTO actionDTO) throws CTPException {
    log.debug("Entering createAction ...");
    // TODO add call to service
    Action action = new Action();
    if (action == null) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Action not created");
    }
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
   * @param actionId Action Id of the Action to Update
   * @return ActionDTO Returns the updated Action details
   * @throws CTPException if update operation fails
   */
  @PUT
  @Path("/{actionid}")
  public ActionDTO updateAction(@PathParam("actionId") final int actionId) throws CTPException {
    log.debug("Updating Action with {}", actionId);
    // TODO add call to service
    Action action = new Action();
    if (action == null) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Action not updated for if %s", actionId);
    }
    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
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
}

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
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * The REST endpoint controller for Actions.
 */
@Path("/actions")
@Produces({ "application/json" })
@Slf4j
public class ActionEndpoint implements CTPEndpoint {

  @Inject
  private ActionService actionService;

  @Inject
  private MapperFacade mapperFacade;

  /**
   * This method returns the associated action for the specified action id.
   * @param actionId This is the action id
   * @return ActionDTO This returns the associated action for the specified action id.
   * @throws CTPException if no associated action found for the specified action id.
   */
  @GET
  @Path("/{actionid}")
  public final ActionDTO findActionByActionId(@PathParam("actionid") final Integer actionId) throws CTPException {
    log.debug("Entering findActionByActionId with {}", actionId);
    Action action = actionService.findActionByActionId(actionId);
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not found for id %s", actionId);
    }
    ActionDTO result = mapperFacade.map(action, ActionDTO.class);
    return result;
  }

  /**
   * This method returns the associated actions for the specified case id.
   * @param caseId This is the case id
   * @return List<ActionDTO> This returns the associated actions for the specified case id.
   */
  @GET
  @Path("/case/{caseid}")
  public final List<ActionDTO> findActionsByCaseId(@PathParam("caseid") final Integer caseId) {
    log.debug("Entering findActionsByCaseId...");
    List<Action> actions = actionService.findActionsByCaseId(caseId);
    List<ActionDTO> actionDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionDTOs) ? null : actionDTOs;
  }
}

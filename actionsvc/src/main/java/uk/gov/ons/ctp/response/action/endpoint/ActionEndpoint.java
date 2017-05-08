package uk.gov.ons.ctp.response.action.endpoint;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.ctp.common.endpoint.CTPEndpoint;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.error.InvalidRequestException;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionCaseService;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * The REST endpoint controller for Actions.
 */
@RestController
@RequestMapping(value = "/actions", produces = "application/json")
@Slf4j
public final class ActionEndpoint implements CTPEndpoint {

  @Autowired
  private ActionService actionService;

  @Autowired
  private ActionCaseService actionCaseService;

  @Qualifier("actionBeanMapper")
  @Autowired
  private MapperFacade mapperFacade;

  /**
   * GET all Actions optionally filtered by ActionType and or state
   *
   * @param actionType Optional filter by ActionType
   * @param actionState Optional filter by Action state
   * @return List<ActionDTO> Actions for the specified filters
   */
  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<?> findActions(@RequestParam(value = "actiontype", required = false) final String actionType,
                                       @RequestParam(value = "state", required = false) final ActionDTO.ActionState actionState) {
    List<Action> actions = null;

    if (actionType != null) {
      if (actionState != null) {
        log.info("Entering findActionsByTypeAndState with {} {}", actionType, actionState);
        actions = actionService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(actionType, actionState);
      } else {
        log.info("Entering findActionsByType with {}", actionType);
        actions = actionService.findActionsByType(actionType);
      }
    } else {
      if (actionState != null) {
        log.info("Entering findActionsByState with {}", actionState);
        actions = actionService.findActionsByState(actionState);
      } else {
        log.info("Entering findActionsByState");
        actions = new ArrayList<Action>();
      }
    }

    List<ActionDTO> actionsDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionsDTOs) ?
            ResponseEntity.noContent().build() : ResponseEntity.ok(actionsDTOs);
  }

  /**
   * POST Create an Action.
   *
   * @param actionDTO Incoming ActionDTO with details to validate and from which
   *          to create Action
   * @return ActionDTO Created Action
   * @throws CTPException on failure to create Action
   */
  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  public ResponseEntity<?> createAction(final @RequestBody @Valid ActionDTO actionDTO, BindingResult bindingResult)
          throws CTPException {
    log.info("Entering createAction with Action {}", actionDTO);
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException("Binding errors for create action: ", bindingResult);
    }
    Action action = actionService.createAction(mapperFacade.map(actionDTO, Action.class));
    return ResponseEntity.created(URI.create("TODO")).body(mapperFacade.map(action, ActionDTO.class));
  }

  /**
   * GET the Action for the specified action id.
   *
   * @param actionId Action Id of requested Action
   * @return ActionDTO Returns the associated Action for the specified action
   * @throws CTPException if no associated Action found for the specified action
   *           Id.
   */
  @RequestMapping(value = "/{actionid}", method = RequestMethod.GET)
  public ActionDTO findActionByActionId(@PathVariable("actionid") final BigInteger actionId) throws CTPException {
    log.info("Entering findActionByActionId with {}", actionId);
    Action action = actionService.findActionByActionId(actionId);
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not found for id %s", actionId);
    }
    return mapperFacade.map(action, ActionDTO.class);
  }

  /**
   * PUT to update the specified Action.
   *
   * @param actionId Action Id of the Action to update
   * @param actionDTO Incoming ActionDTO with details to update
   * @return ActionDTO Returns the updated Action details
   * @throws CTPException if update operation fails
   */
  @RequestMapping(value = "/{actionid}", method = RequestMethod.PUT, consumes = "application/json")
  public ActionDTO updateAction(@PathVariable("actionid") final BigInteger actionId,
                                @RequestBody final ActionDTO actionDTO, BindingResult bindingResult)
      throws CTPException {
    log.info("Updating Action with {} {}", actionId, actionDTO);
    if (bindingResult.hasErrors()) {
      throw new InvalidRequestException("Binding errors for update action: ", bindingResult);
    }

    actionDTO.setActionId(actionId);
    Action action = actionService.updateAction(mapperFacade.map(actionDTO, Action.class));
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not updated for id %s", actionId);
    }
    return mapperFacade.map(action, ActionDTO.class);
  }

  /**
   * PUT to cancel all the Actions for a specified caseId.
   *
   * @param caseId Case Id of the actions to cancel
   * @return List<ActionDTO> Returns a list of cancelled Actions
   * @throws CTPException if update operation fails
   */
  @RequestMapping(value = "/case/{caseid}/cancel", method = RequestMethod.PUT, consumes = "application/json")
  public ResponseEntity<?> cancelActions(@PathVariable("caseid") final int caseId)
      throws CTPException {
    log.info("Cancelling Actions for {}", caseId);
  
    ActionCase caze = actionCaseService.findActionCase(caseId);
    if (caze == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Case not found for caseId %s", caseId);
    }
    
    List<Action> actions = actionService.cancelActions(caseId);
    List<ActionDTO> results = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(results) ?
            ResponseEntity.noContent().build() : ResponseEntity.ok(results);
  }

  /**
   * GET Actions for the specified case Id.
   *
   * @param caseId caseID to which Actions apply
   * @return List<ActionDTO> Returns the associated actions for the specified
   *         case id.
   */
  @RequestMapping(value = "/case/{caseid}", method = RequestMethod.GET)
  public ResponseEntity<?> findActionsByCaseId(@PathVariable("caseid") final Integer caseId) {
    log.info("Entering findActionsByCaseId...");
    List<Action> actions = actionService.findActionsByCaseId(caseId);
    List<ActionDTO> actionDTOs = mapperFacade.mapAsList(actions, ActionDTO.class);
    return CollectionUtils.isEmpty(actionDTOs) ?
            ResponseEntity.noContent().build() : ResponseEntity.ok(actionDTOs);
  }

  /**
   * Allow feedback otherwise sent via JMS to be sent via endpoint
   * @param actionId the action
   * @param actionFeedback the feedback
   * @return the modified action
   * @throws CTPException oops
   */
  @RequestMapping(value = "/{actionid}/feedback", method = RequestMethod.PUT, consumes = {"application/xml", "application/json"})
  public ActionDTO feedbackAction(@PathVariable("actionid") final int actionId, final ActionFeedback actionFeedback)
      throws CTPException {
    log.info("Feedback for Action {}", actionId);
    actionFeedback.setActionId(BigInteger.valueOf(actionId));
    Action action = actionService.feedBackAction(actionFeedback);
    if (action == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND, "Action not found for id %s", actionId);
    }
    return mapperFacade.map(action, ActionDTO.class);
  }
}

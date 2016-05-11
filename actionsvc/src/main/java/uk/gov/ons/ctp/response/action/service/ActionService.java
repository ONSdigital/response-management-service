package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;

/**
 * The Action Service interface defines all business behaviours for operations
 * on the Action entity model.
 */
public interface ActionService extends CTPService {


  /**
   * Find Actions filtered by ActionType and state ordered by created date time
   * descending
   *
   * @param actionTypeName Action type name by which to filter
   * @param state State by which to filter
   * @return List<Action> List of Actions or empty List
   */
  List<Action> findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(String actionTypeName,
      ActionDTO.ActionState state);

  /**
   * Find Actions filtered by ActionType.
   *
   * @param actionTypeName Action type name by which to filter
   * @return List<Action> List of Actions or empty List
   */
  List<Action> findActionsByType(String actionTypeName);

  /**
   * Find Actions filtered by state.
   *
   * @param state State by which to filter
   * @return List<Action> List of Actions or empty List
   */
  List<Action> findActionsByState(ActionDTO.ActionState state);

  /**
   * Find Action entity by specified action id.
   *
   * @param actionId This is the action id
   * @return Action Returns the action for the specified action id.
   */
  Action findActionByActionId(Integer actionId);

  /**
   * Find all actions for the specified Case Id.
   *
   * @param caseId This is the case id
   * @return List<Action> Returns all actions for the specified Case Id.
   */
  List<Action> findActionsByCaseId(Integer caseId);

	/**
	 * Cancel all the actions for a given caseId.
	 *
	 * @param caseId
	 *            Integer caseId for all the Actions to be cancelled
	 * @param categoryName the case event category name that lead to the case closure
	 *
	 * @return List<Action> Returns list of all actions for the case that were cancelled. Not all actions are cancellable!
	 */
	List<Action> cancelActions(Integer caseId);
	
	/**
	 * Create an action.
	 *
	 * @param action
	 *            Action to be created
	 *
	 * @return Action Returns created Action.
	 */
	Action createAction(Action action);

  /**
   * Update an action.
   *
   * @param action Action with update information
   * @return Action Returns updated Action.
   */
  Action updateAction(Action action);

  
  /**
   * Accept the feedback for an action  
   * @param actionFeedback the feedback
   * @return the updated action
   */
  Action feedBackAction(ActionFeedback actionFeedback);
}

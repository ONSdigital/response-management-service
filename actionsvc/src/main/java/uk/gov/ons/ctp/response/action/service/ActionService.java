package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.Action;

/**
 * The Action Service interface defines all business behaviours for operations
 * on the Action entity model.
 */
public interface ActionService extends CTPService {
  
  /**
   * Find Actions filtered by ActionType and. or state.
   * @param actionType Action type name by which to filter
   * @param state State by which to filter
   * @return List<Action> List of Actions or empty List 
   */
  List<Action> findActionsByTypeAndState(String actionTypeName, String state);

  /**
   * Find Action entity by specified action id.
   * @param actionId This is the action id
   * @return Action Returns the action for the specified action id.
   */
  Action findActionByActionId(Integer actionId);

  /**
   * Find all actions for the specified Case Id.
   * @param caseId This is the case id
   * @return List<Action> Returns all actions for the specified Case Id.
   */
  List<Action> findActionsByCaseId(Integer caseId);

}

package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.Action;

/**
 * Created by Martin.Humphrey on 16/2/16.
 */
public interface ActionService extends CTPService {

  /**
   * This method returns the action for the specified action id.
   * @param actionId This is the action id
   * @return Action This returns the action for the specified action id.
   */
  Action findActionByActionId(Integer actionId);

  /**
   * This method returns all actions for the specified case id.
   * @param caseId This is the case id
   * @return List<Action> This returns all actions for the specified case id.
   */
  List<Action> findActionsByCaseId(Integer caseId);

}

package uk.gov.ons.ctp.response.action.export.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;

/**
 * Service responsible for dealing with ActionRequests
 */
public interface ActionRequestService {
  /**
   * To retrieve all ActionRequests
   *
   * @return a list of ActionRequests
   */
  List<ActionRequestInstruction> retrieveAllActionRequests();

  /**
   * To retrieve a given ActionRequest
   *
   * @param actionId the ActionRequest actionId to be retrieved
   * @return the given ActionRequest
   */
  ActionRequestInstruction retrieveActionRequest(BigInteger actionId);

  /**
   * Save an ActionRequest
   *
   * @param actionRequest the ActionRequest to save.
   * @return the ActionRequest saved.
   */
  ActionRequestInstruction save(final ActionRequestInstruction actionRequest);

  /**
   * Retrieve all ActionRequests not sent for an actionType.
   *
   * @param actionType actionType for which to retrieve ActionRequests.
   * @return List ActionRequests not sent to external services previously for
   *         actionType.
   */
  List<ActionRequestInstruction> findByDateSentIsNullAndActionType(String actionType);

  /**
   * Return a list of distinct actionTypes in collection
   *
   * @return a list of actionTypes.
   */
  List<String> retrieveActionTypes();

  /**
   * Update ActionRequest date sent for batch of actionIds.
   *
   * @param actionIds List of ActionRequest actionIds to update.
   * @param dateSent to set on each ActionRequest.
   * @return int of affected rows
   */
  int updateDateSentByActionId(Set<BigInteger> actionIds, Timestamp dateSent);

  /**
   * Retrieve actionIds where response required is true for batch of actionIds.
   *
   * @param actionIds ActionRequest actionIds to check for response required.
   * @return actionIds of those where response required.
   */
  List<BigInteger> retrieveResponseRequiredByActionId(Set<BigInteger> actionIds);
}

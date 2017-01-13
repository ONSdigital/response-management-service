package uk.gov.ons.ctp.response.action.export.service;

import java.math.BigInteger;
import java.util.List;

import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;

/**
 * Service responsible for dealing with ActionRequests stored in MongoDB
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
   * Retrieve all action export requests not done for an actionType.
   * 
   * @return List ActionRequests not sent to external services
   *         previously for actionType.
   */
  List<ActionRequestInstruction> findByDateSentIsNullAndActionType(String actionType);

  /**
   * Return a list of distinct actionTypes in collection
   *
   * @return a list of actionTypes.
   */
  List<String> retrieveActionTypes();
}

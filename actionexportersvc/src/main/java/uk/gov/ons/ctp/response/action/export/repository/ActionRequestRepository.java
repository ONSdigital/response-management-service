package uk.gov.ons.ctp.response.action.export.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;

/**
 * JPA repository for ActionRequest entities
 *
 */
@Repository
public interface ActionRequestRepository extends JpaRepository<ActionRequestInstruction, BigInteger> {

  /**
   * Retrieve all action export requests not done for an actionType.
   *
   * @param actionType for which to return action requests.
   * @return List ActionRequests not sent to external services previously for
   *         actionType.
   */
  List<ActionRequestInstruction> findByDateSentIsNullAndActionType(String actionType);

  /**
   * Retrieve a list of actionTypes
   *
   * @return List of distinct actionTypes
   */
  @Query("SELECT DISTINCT(r.actionType) FROM ActionRequestInstruction r")
  List<String> findAllActionType();

  /**
   * Update action request date sent for List of actionIds.
   *
   * @param actionIds List of ActionRequest actionIds to update
   * @param dateSent to set on each ActionRequest
   * @return int of affected rows
   */
  @Modifying
  @Transactional
  @Query("UPDATE ActionRequestInstruction r SET r.dateSent = :dateSent WHERE r.actionId IN :actionIds")
  int updateDateSentByActionId(@Param("actionIds") Set<BigInteger> actionIds, @Param("dateSent") Timestamp dateSent);

  /**
   * Retrieve actionIds where response required is true for List of actionIds.
   *
   * @param actionIds List of ActionRequest actionIds to check for response
   *          required.
   * @return actionIds of those where response required.
   */
  @Query("SELECT r.actionId FROM ActionRequestInstruction r WHERE r.responseRequired = TRUE AND r.actionId IN :actionIds")
  List<BigInteger> retrieveResponseRequiredByActionId(@Param("actionIds") Set<BigInteger> actionIds);

}

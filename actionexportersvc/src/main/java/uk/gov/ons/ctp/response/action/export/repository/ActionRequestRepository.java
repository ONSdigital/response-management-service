package uk.gov.ons.ctp.response.action.export.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
   * @return List ActionRequests not sent to external services
   *         previously for actionType.
   */
  List<ActionRequestInstruction> findByDateSentIsNullAndActionType(String actionType);

  /**
   * Retrieve a list of actionTypes
   *
   * @return List of distinct actionTypes
   */
  @Query("SELECT DISTINCT(r.actionType) FROM ActionRequestInstruction r")
  List<String> findAllActionType();

}

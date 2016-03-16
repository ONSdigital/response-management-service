package uk.gov.ons.ctp.response.action.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.domain.model.Action;

/**
 * JPA Data Repository.
 */
@Repository
public interface ActionRepository extends JpaRepository<Action, Integer> {

  /**
   * Return all actions for the specified case id.
   *
   * @param caseId This is the case id
   * @return List<Action> This returns all actions for the specified case id.
   */
  List<Action> findByCaseId(Integer caseId);

  /**
   * Return all actions for the specified actionTypeName and state.
   *
   * @param actionTypeName ActionTypeName filter criteria
   * @param state State of Action
   * @return List<Action> returns all actions for actionTypeName and state
   */
  List<Action> findByActionTypeNameAndState(String actionTypeName, String state);

  /**
   * Return all actions for the specified actionTypeName.
   *
   * @param actionTypeName ActionTypeName filter criteria
   * @return List<Action> returns all actions for actionTypeName
   */
  List<Action> findByActionTypeName(String actionTypeName);

  /**
   * Return all actions for the specified state.
   *
   * @param state State filter criteria
   * @return List<Action> returns all actions for state
   */
  List<Action> findByState(String state);

}

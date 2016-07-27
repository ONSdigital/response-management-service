package uk.gov.ons.ctp.response.action.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.domain.model.ActionCase;

/**
 * JPA Data Repository for ActionCase which is backed by action.case table
 */
@Repository
public interface ActionCaseRepository extends JpaRepository<ActionCase, Integer> {

  /**
   * trigger creation of actions from the population of the action.case and action.actionjobplan tables
   * @param actionplanjobid the id of the action plan job
   * @return true if successful
   */
  @Procedure(name = "createactions")
  boolean createActions(@Param("p_actionplanjobid") Integer actionplanjobid);
  
  /**
   * find cases (by virtue open) for actionplanid 
   * @param actionPlanId the action plan
   * @return the list of (open) cases assoc with that plan
   */
  List<ActionCase> findByActionPlanId(Integer actionPlanId);
  
}

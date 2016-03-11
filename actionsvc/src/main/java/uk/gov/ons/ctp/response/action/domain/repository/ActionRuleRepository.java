package uk.gov.ons.ctp.response.action.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.ctp.response.action.domain.model.ActionRule;

import java.util.List;

/**
 * JPA Data Repository.
 */
@Repository
public interface ActionRuleRepository extends JpaRepository<ActionRule, Integer> {
  /**
   * Returns all action rules for the given action plan id
   * @param actionPlanId the given action plan id
   * @return Returns all action rules for the given action plan id.
   */
  List<ActionRule> findByActionPlanId(Integer actionPlanId);
}

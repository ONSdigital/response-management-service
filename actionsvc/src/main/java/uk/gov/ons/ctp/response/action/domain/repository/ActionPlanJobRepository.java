package uk.gov.ons.ctp.response.action.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;

import java.util.List;

/**
 * JPA Data Repository.
 */
@Repository
public interface ActionPlanJobRepository extends JpaRepository<ActionPlanJob, Integer> {
  /**
   * Gte the actionplanjobs for an action plan by id
   * @param actionPlanId the plan id
   * @return the jobs
   */
  List<ActionPlanJob> findByActionPlanId(Integer actionPlanId);
}

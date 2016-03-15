package uk.gov.ons.ctp.response.action.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;

/**
 * JPA Data Repository.
 */
@Repository
public interface ActionPlanJobRepository extends JpaRepository<ActionPlanJob, Integer> {
}

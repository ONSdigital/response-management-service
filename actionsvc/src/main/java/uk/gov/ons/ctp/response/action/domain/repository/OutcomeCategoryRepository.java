package uk.gov.ons.ctp.response.action.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.domain.model.OutcomeCategory;
import uk.gov.ons.ctp.response.action.domain.model.OutcomeHandlerId;

/**
 * JPA Data Repository.
 */
@Repository
public interface OutcomeCategoryRepository extends JpaRepository<OutcomeCategory, OutcomeHandlerId> {
 
}

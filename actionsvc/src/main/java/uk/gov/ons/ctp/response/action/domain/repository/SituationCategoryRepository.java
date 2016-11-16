package uk.gov.ons.ctp.response.action.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.domain.model.SituationCategory;

/**
 * JPA Data Repository.
 */
@Repository
public interface SituationCategoryRepository extends JpaRepository<SituationCategory, String> {
 
}

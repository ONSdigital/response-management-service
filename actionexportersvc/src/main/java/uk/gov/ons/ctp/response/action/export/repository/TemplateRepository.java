package uk.gov.ons.ctp.response.action.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.export.domain.TemplateExpression;

/**
 * JPA repository for Template entities
 */
@Repository
public interface TemplateRepository extends JpaRepository<TemplateExpression, String> {
}

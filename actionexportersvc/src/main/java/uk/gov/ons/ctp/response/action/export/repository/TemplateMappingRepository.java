package uk.gov.ons.ctp.response.action.export.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.export.domain.TemplateMappingDocument;

/**
 * JPA repository for TemplateMapping entities
 */
@Repository
public interface TemplateMappingRepository extends JpaRepository<TemplateMappingDocument, String> {

  /**
   * Retrieve a list of actionTypes
   *
   * @return List of distinct actionTypes
   */
  @Query("SELECT DISTINCT(t.actionType) FROM TemplateMappingDocument t")
  List<String> findAllActionType();
}

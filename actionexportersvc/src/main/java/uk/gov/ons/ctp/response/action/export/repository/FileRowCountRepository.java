package uk.gov.ons.ctp.response.action.export.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.export.domain.FileRowCount;

/**
 * JPA repository for FileRowCount entities
 */
@Repository
public interface FileRowCountRepository extends JpaRepository<FileRowCount, String> {

}

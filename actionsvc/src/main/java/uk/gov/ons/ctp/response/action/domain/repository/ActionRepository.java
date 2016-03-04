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
     * This method returns all actions for the specified case id.
     * @param caseId This is the case id
     * @return List<Action> This returns all actions for the specified case id.
     */
    List<Action> findByCaseId(Integer caseId);

}

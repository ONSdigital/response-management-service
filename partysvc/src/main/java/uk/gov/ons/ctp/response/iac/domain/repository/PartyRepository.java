package uk.gov.ons.ctp.response.iac.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import uk.gov.ons.ctp.response.party.domain.model.Party;

/**
 * JPA Data Repository needed to persist IAC records
 */
@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {

}

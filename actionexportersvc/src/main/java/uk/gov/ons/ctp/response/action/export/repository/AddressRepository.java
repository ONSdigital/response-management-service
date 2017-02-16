package uk.gov.ons.ctp.response.action.export.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import uk.gov.ons.ctp.response.action.export.domain.Address;

/**
 * JPA repository for Address entities
 *
 */
@Repository
public interface AddressRepository extends BaseRepository<Address, BigInteger> {

  /**
   * Check repository for uprn existence
   * 
   * @param uprn to check for existence
   * @return boolean whether exists
   */
  @Query(value = "select exists(select 1 from actionexporter.address where uprn=:p_uprn)", nativeQuery = true)
  boolean tupleExists(@Param("p_uprn") BigInteger uprn);
}

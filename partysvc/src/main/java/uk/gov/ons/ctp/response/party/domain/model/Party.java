package uk.gov.ons.ctp.response.party.domain.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "party", schema = "party")
public class Party {

  @Id
  @Column(name = "id")
  private String partyId;
  
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "partytypeid")
  private PartyType partyType;

  @Version
  @Column(name = "optlockversion")
  private int optLockVersion;

  @Column(name = "effectivestartdatetime")
  private Timestamp effectiveStartDateTime;

  @Column(name = "effectiveenddatetime")
  private Timestamp effectiveEndDateTime;
  
  @Column(name = "createddatetime")
  private Timestamp createdDateTime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;
  
  @OneToMany(mappedBy = "partyid", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<PartyAttribute> attributes;
}

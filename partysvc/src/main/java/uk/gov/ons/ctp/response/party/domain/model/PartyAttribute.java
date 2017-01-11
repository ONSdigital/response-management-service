package uk.gov.ons.ctp.response.party.domain.model;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistable Domain model object for the IAC DB IAC records 
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "attribute", schema = "party")
public class PartyAttribute {

  @Id
  @Column(name = "id")
  private String partyAttributeId;

  @Column(name = "attributevalue")
  private String value;

  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "attributeTypeId")
  private PartyAttributeType attributeType;
  
  @Version
  @Column(name = "optlockversion")
  private int optLockVersion;

  @Column(name = "createddatetime")
  private Timestamp createdDateTime;

  @Column(name = "updateddatetime")
  private Timestamp updatedDateTime;

}

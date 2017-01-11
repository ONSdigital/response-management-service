package uk.gov.ons.ctp.response.party.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "partytype", schema = "party")
public class PartyType {

  @Id
  @Column(name = "id")
  private String partyTypeId;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "description")
  private String description;

}

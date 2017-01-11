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
@Table(name = "partyvaluetype", schema = "party")
public class PartyValueType {

  @Id
  @Column(name = "id")
  private String valueTypeId;
  
  @Column(name = "name")
  private String name;
  
  @Column(name = "description")
  private String description;

}

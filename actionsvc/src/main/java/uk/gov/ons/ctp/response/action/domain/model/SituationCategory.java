package uk.gov.ons.ctp.response.action.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "situationcategory", schema = "action")
public class SituationCategory {

  @Id
  private String situation;

  @Column(name = "eventcategory")
  private String eventCategory;

}

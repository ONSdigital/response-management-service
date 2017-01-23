package uk.gov.ons.ctp.response.action.export.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(name = "actionexportseq_gen", sequenceName = "actionexporter.contactidseq")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contact", schema = "actionexporter")
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actionexportseq_gen")
  @Column(name = "contactid")
  private Integer contactId;

  private String title;

  private String forename;

  private String surname;

  @Column(name = "phonenumber")
  private String phoneNumber;

  @Column(name = "emailaddress")
  private String emailAddress;

}

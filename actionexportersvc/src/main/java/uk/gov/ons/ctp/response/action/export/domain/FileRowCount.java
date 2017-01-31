package uk.gov.ons.ctp.response.action.export.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain entity representing details of a SFTP transfer of actionRequests.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "filerowcount", schema = "actionexporter")
public class FileRowCount {

  @Id
  private String filename;

  private int rowcount;

  @Column(name = "datesent")
  private Timestamp dateSent;

  @Column(name = "sendresult")
  private boolean sendResult;

  private boolean reported;

}

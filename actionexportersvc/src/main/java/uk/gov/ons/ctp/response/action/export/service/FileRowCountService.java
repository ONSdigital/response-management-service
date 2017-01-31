package uk.gov.ons.ctp.response.action.export.service;

import uk.gov.ons.ctp.response.action.export.domain.FileRowCount;

/**
 * Service responsible for dealing with FileRowCounts
 */
public interface FileRowCountService {

  /**
   * Save a FileRowCount
   *
   * @param fileRowCount the FileRowCount to save.
   * @return the FileRowCount saved.
   */
  FileRowCount save(final FileRowCount fileRowCount);

}

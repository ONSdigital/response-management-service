package uk.gov.ons.ctp.response.action.message;

import java.io.File;

/**
 * Interface for CSV Ingester
 *
 */
public interface CsvIngester {

  /**
   * take the file and ingest the contents as CSV formatted entries - each row
   * will result in an action request or action cancel instruction being sent to
   * a handler, BUT the action details will not be recorded in the action schema
   * as is usually the case. These instructions are unrecorded and require no
   * feedback from the handlers.
   *
   * @param csvFile the file to ingest
   */
  void ingest(File csvFile);

}

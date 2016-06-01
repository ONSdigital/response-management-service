package uk.gov.ons.ctp.response.action.message;

import java.io.File;

public interface CsvIngester {

  void ingest(File csvFile);

}
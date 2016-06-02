package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

/**
 * App config POJO for csv ingest params
 *
 */
@Data
public class CsvIngest {
  private String directory;
  private String filePattern;
  private Integer pollMilliseconds;
}

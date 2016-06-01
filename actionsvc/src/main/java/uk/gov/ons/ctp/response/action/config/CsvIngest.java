package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

@Data
public class CsvIngest {
  private String directory;
  private String filePattern;
  private Integer pollMilliseconds;
}

package uk.gov.ons.ctp.response.action.config;

import lombok.Data;

@Data
public class CaseFrameSvc {
  private String scheme;
  private String host;
  private String port;
  private String caseByCaseGetPath;
  private String questionnairesByCaseGetPath;
  private String caseEventsByCaseGetPath;
  private String caseEventsByCasePostPath;
  private String addressByUprnGetPath;
}

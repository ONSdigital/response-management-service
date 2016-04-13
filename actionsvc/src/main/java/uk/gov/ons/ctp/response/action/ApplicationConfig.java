package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;

import lombok.Data;

@Named
@Data
public class ApplicationConfig {

  @Value("${caseframesvc.scheme}")
  private String caseFrameSvcScheme;

  @Value("${caseframesvc.host}")
  private String caseFrameSvcHost;

  @Value("${caseframesvc.port}")
  private String caseFrameSvcPort;
  
  @Value("${caseframesvc.caseByCaseGetPath}")
  private String caseFrameSvcCaseGetPath;
  
  @Value("${caseframesvc.questionnairesByCaseGetPath}")
  private String caseFrameSvcQuestionnairesByCaseGetPath;
  
  @Value("${caseframesvc.caseEventsByCaseGetPath}")
  private String caseFrameSvcCaseEventsByCaseGetPath;
  
  @Value("${caseframesvc.caseEventsByCasePostPath}")
  private String caseFrameSvcCaseEventsByCasePostPath;
  
  @Value("${caseframesvc.addressByUprnGetPath}")
  private String caseFrameSvcAddressByUprnGetPath;
  
}

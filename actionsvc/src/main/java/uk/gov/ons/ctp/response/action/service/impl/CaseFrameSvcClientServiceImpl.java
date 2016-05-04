package uk.gov.ons.ctp.response.action.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.service.CaseFrameSvcClientService;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

@Slf4j
public class CaseFrameSvcClientServiceImpl implements CaseFrameSvcClientService {
  @Inject
  private AppConfig appConfig;
  
  @Inject
  private RestClient caseFrameClient;
  

  @Override
  public AddressDTO getAddress(final Integer uprn) {
    AddressDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvc().getAddressByUprnGetPath(),
        AddressDTO.class, uprn);
    return caseDTO;
  }


  @Override
  public QuestionnaireDTO getQuestionnaire(final Integer caseId) {
    List<QuestionnaireDTO> questionnaireDTOs = caseFrameClient.getResources(
        appConfig.getCaseFrameSvc().getQuestionnairesByCaseGetPath(),
        QuestionnaireDTO[].class, caseId);
    if (questionnaireDTOs.size() == 0) {
      throw new RuntimeException("Failed to find questionnaire for case " + caseId);
    }
    return questionnaireDTOs.get(0);
  }


  @Override
  public CaseDTO getCase(final Integer caseId) {
    CaseDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvc().getCaseByCaseGetPath(),
        CaseDTO.class, caseId);
    return caseDTO;
  }


  @Override
  public List<CaseEventDTO> getCaseEvents(final Integer caseId) {
    List<CaseEventDTO> caseEventDTOs = caseFrameClient.getResources(
        appConfig.getCaseFrameSvc().getCaseEventsByCaseGetPath(),
        CaseEventDTO[].class, caseId);
    return caseEventDTOs;
  }
  
  @Override
  public CaseEventDTO createNewCaseEvent(final Action action, String actionCategory) {
    log.debug("posting caseEvent for actionId {} to caseframesvc for category {} ", action.getActionId(), actionCategory);
    CaseEventDTO caseEventDTO = new CaseEventDTO();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory(actionCategory);
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setDescription(action.getActionType().getDescription());
    caseEventDTO.setSubCategory(action.getActionType().getName()); 

    CaseEventDTO returnedCaseEventDTO = caseFrameClient.postResource(appConfig.getCaseFrameSvc().getCaseEventsByCasePostPath(), caseEventDTO,
        CaseEventDTO.class,
        action.getCaseId());
    return returnedCaseEventDTO;
  }

}

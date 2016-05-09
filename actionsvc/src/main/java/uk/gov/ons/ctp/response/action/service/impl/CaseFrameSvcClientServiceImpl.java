package uk.gov.ons.ctp.response.action.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.service.CaseFrameSvcClientService;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CategoryDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

@Slf4j
@Named
public class CaseFrameSvcClientServiceImpl implements CaseFrameSvcClientService {
  @Inject
  private AppConfig appConfig;

  @Inject
  private RestClient caseFrameClient;

  @Override
  public AddressDTO getAddress(final Long uprn) {
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
  public CaseEventDTO createNewCaseEvent(final Action action, CategoryDTO.CategoryName actionCategory) {
    log.debug("posting caseEvent for actionId {} to caseframesvc for category {} ", action.getActionId(),
        actionCategory);
    CaseEventDTO caseEventDTO = new CaseEventDTO();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory(actionCategory.getLabel());
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setDescription(action.getActionType().getDescription());
    caseEventDTO.setSubCategory(action.getActionType().getName());

    CaseEventDTO returnedCaseEventDTO = caseFrameClient.postResource(
        appConfig.getCaseFrameSvc().getCaseEventsByCasePostPath(), caseEventDTO,
        CaseEventDTO.class,
        action.getCaseId());
    return returnedCaseEventDTO;
  }

  @Override
  public List<Integer> getOpenCasesForActionPlan(Integer actionPlanId) {
    MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();
    queryParamMap.put("status", Arrays.asList(CaseDTO.CaseState.INIT.name(), CaseDTO.CaseState.RESPONDED.name()));
    List<Integer> openCasesForPlan = caseFrameClient.getResources(
        appConfig.getCaseFrameSvc().getCaseByStatusAndActionPlanPath(), Integer[].class, null, queryParamMap,
        actionPlanId);
    return openCasesForPlan;
  }
}

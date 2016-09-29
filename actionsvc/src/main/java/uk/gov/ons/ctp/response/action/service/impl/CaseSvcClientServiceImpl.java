package uk.gov.ons.ctp.response.action.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.service.CaseSvcClientService;
import uk.gov.ons.ctp.response.casesvc.representation.AddressDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;
import uk.gov.ons.ctp.response.casesvc.representation.QuestionnaireDTO;

/**
 * Impl of the service that centralizes all REST calls to the Case service
 *
 */
@Slf4j
@Named
public class CaseSvcClientServiceImpl implements CaseSvcClientService {
  @Inject
  private AppConfig appConfig;

  @Inject
  private RestClient caseSvcClient;

  @Override
  public AddressDTO getAddress(final Long uprn) {
    AddressDTO caseDTO = caseSvcClient.getResource(appConfig.getCaseSvc().getAddressByUprnGetPath(),
        AddressDTO.class, uprn);
    return caseDTO;
  }

  @Override
  public QuestionnaireDTO getQuestionnaire(final Integer caseId) {
    List<QuestionnaireDTO> questionnaireDTOs = caseSvcClient.getResources(
        appConfig.getCaseSvc().getQuestionnairesByCaseGetPath(),
        QuestionnaireDTO[].class, caseId);
    if (questionnaireDTOs.size() == 0) {
      throw new RuntimeException("Failed to find questionnaire for case " + caseId);
    }
    return questionnaireDTOs.get(0);
  }

  @Override
  public CaseDTO getCase(final Integer caseId) {
    CaseDTO caseDTO = caseSvcClient.getResource(appConfig.getCaseSvc().getCaseByCaseGetPath(),
        CaseDTO.class, caseId);
    return caseDTO;
  }

  @Override
  public List<CaseEventDTO> getCaseEvents(final Integer caseId) {
    List<CaseEventDTO> caseEventDTOs = caseSvcClient.getResources(
        appConfig.getCaseSvc().getCaseEventsByCaseGetPath(),
        CaseEventDTO[].class, caseId);
    return caseEventDTOs;
  }

  @Override
  public CaseEventDTO createNewCaseEvent(final Action action, CategoryDTO.CategoryName actionCategory) {
    log.debug("posting caseEvent for actionId {} to casesvc for category {} ", action.getActionId(),
        actionCategory);
    CaseEventDTO caseEventDTO = new CaseEventDTO();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory(actionCategory.getLabel());
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setSubCategory(action.getActionType().getName());

    if (!StringUtils.isEmpty(action.getSituation())) {
      caseEventDTO.setDescription(String.format("%s (%s)",
          action.getActionType().getDescription(), action.getSituation()));
    } else {
      caseEventDTO.setDescription(action.getActionType().getDescription());
    }

    CaseEventDTO returnedCaseEventDTO = caseSvcClient.postResource(
        appConfig.getCaseSvc().getCaseEventsByCasePostPath(), caseEventDTO,
        CaseEventDTO.class,
        action.getCaseId());
    return returnedCaseEventDTO;
  }

  @Override
  @Deprecated
  public List<Integer> getOpenCasesForActionPlan(Integer actionPlanId) {
    MultiValueMap<String, String> queryParamMap = new LinkedMultiValueMap<>();
    queryParamMap.put("state", Arrays.asList(CaseDTO.CaseState.ACTIVE.name(), CaseDTO.CaseState.RESPONDED.name()));
    List<Integer> openCasesForPlan = caseSvcClient.getResources(
        appConfig.getCaseSvc().getCaseByStatusAndActionPlanPath(), Integer[].class, null, queryParamMap,
        actionPlanId);
    return openCasesForPlan;
  }
}

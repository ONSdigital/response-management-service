package uk.gov.ons.ctp.response.action.scheduled;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.ActionService;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

@Named
@Slf4j
public class ActionDistributor {


  private final RestTemplate restTemplate;

  @Value("${caseframesvc.casesurl}")
  private String caseFrameSvcCasesUrl;

  @Inject
  private InstructionPublisher instructionPublisher;

  @Inject
  private MapperFacade mapperFacade;

  @Inject
  private ActionService actionService;
  
  public ActionDistributor() {
    super();
    restTemplate = new RestTemplate();
  }

  @Scheduled(fixedRate = 5000)
  public void wakeUp() {
    log.debug("ActionDistributor is in the house!");

    List<ActionType> actionTypes = actionService.findActionTypes();
    for (ActionType actionType : actionTypes) {
      List<Action> actions = actionService.findActionsForDistribution(actionType.getName(), ActionState.SUBMITTED);
      List<ActionRequest> actionRequests = new ArrayList<ActionRequest> ();

      for (Action action : actions) {
        try {
          // create the request, filling in details by GETs from caseframesvc
          ActionRequest actionRequest = createActionRequest(action);
          // advise caseframesvc to create a corresponding caseevent for our action
          postNewCaseEvent(action);
          
          // update our actions state in db 
          updateActionStatusToPending(action);
          
          // add the request to the list to be published to downstream handler
          actionRequests.add(actionRequest);
        } catch (Exception e) {
          // do what?!
        }
      }
      // send the list of requests for this action type to the handler
      instructionPublisher.sendRequests(actionType.getHandler(), actionRequests);
    }
  }

  private void updateActionStatusToPending(Action action) {
    action.setState(ActionState.PENDING); 
    actionService.updateAction(action);
  }

  private void postNewCaseEvent(Action action) throws Exception {
    log.debug("posting caseEvent for actionId {} to caseframesvc for creation", action.getActionId());
    CaseEventDTO caseEventDTO = new CaseEventDTO ();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory("?");
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setDescription(action.getActionType().getDescription());  //?
    caseEventDTO.setSubCategory("?");
    // POST to /cases/{casedId}/events
    
  }
  
  private <T> T getResource(Class<T> clazz, String url, Map<String, String> headerParams, MultiValueMap<String, String> queryParams, Object ... pathParams) throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    if (headerParams != null) {
      for (Map.Entry<String, String> me : headerParams.entrySet()) {
        headers.set(me.getKey(), me.getValue());
      }
    }

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(String.format(url, pathParams)).queryParams(queryParams);
    HttpEntity<?> httpEntity = new HttpEntity<>(headers);
    ResponseEntity<T> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, httpEntity, clazz);
    if (response.getStatusCode().equals(HttpStatus.OK)) {
      throw new RestClientException("Expected status 200 but got " + response.getStatusCode().value());
    }
    return response.getBody();
  }
  
  private ActionRequest createActionRequest(Action action) throws Exception {
    log.debug("constructing ActionRequest to publish to downstream handler");
    ActionRequest actionRequest = new ActionRequest();
    // now call caseframe for the following
    CaseDTO caseDTO = getCase(action.getCaseId());
    QuestionnaireDTO questionnaireDTO = getQuestionnaire(action.getCaseId());
    AddressDTO addressDTO = getAddress(caseDTO.getUprn());
    List<CaseEventDTO> caseEvents = getCaseEvents(action.getCaseId());

    // populate the request
    actionRequest.setActionId(BigInteger.valueOf(action.getActionId()));
    actionRequest.setActionType(action.getActionType().getName());
    actionRequest.setCaseId(BigInteger.valueOf(action.getCaseId()));
    actionRequest.setContactName(""); // TODO - will be avail in data 2017+
    ActionEvent actionEvent = new ActionEvent();
    for (CaseEventDTO caseEventDTO : caseEvents) {
      actionEvent.getEvents().add(formatCaseEvent(caseEventDTO));
    }
    actionRequest.setIac(questionnaireDTO.getIac());
    actionRequest.setPriority(Priority.fromValue(ActionPriority.valueOf(action.getPriority()).getName()));
    actionRequest.setQuestionnaireId(BigInteger.valueOf(questionnaireDTO.getQuestionnaireId()));
    actionRequest.setUprn(BigInteger.valueOf(caseDTO.getUprn()));

    ActionAddress actionAddress = mapperFacade.map(addressDTO, ActionAddress.class);
    actionRequest.setAddress(actionAddress);

    return actionRequest;
  }

  private AddressDTO getAddress(Integer uprn) throws Exception {
    // TODO call caseframsvc restfully asking for the address for a uprn
    return new AddressDTO();
  }

  private QuestionnaireDTO getQuestionnaire(Integer caseId) throws Exception {
    // TODO call caseframsvc restfully asking for all questionnaires for case
    // then return the questionnaire with the latest dispatch date time
    return new QuestionnaireDTO();
  }

  private CaseDTO getCase(Integer caseId) throws Exception {
    CaseDTO caseDTO = getResource(CaseDTO.class, "", null, null, caseId); 

    return caseDTO;
  }

  private String formatCaseEvent(CaseEventDTO caseEventDTO) {
    return String.format(
        "%s : %s : %s : %s",
        caseEventDTO.getCategory(),
        caseEventDTO.getSubCategory(),
        caseEventDTO.getCreatedBy(),
        caseEventDTO.getDescription());
  }

  /**
   * "/cases/{caseId}/events"
   * 
   * @param caseId
   */
  private List<CaseEventDTO> getCaseEvents(Integer caseId) {
    // TODO call caseframsvc restfully
    return new ArrayList<CaseEventDTO>();
  }
}

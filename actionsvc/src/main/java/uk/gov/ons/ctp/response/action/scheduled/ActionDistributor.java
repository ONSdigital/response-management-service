package uk.gov.ons.ctp.response.action.scheduled;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionState;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
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
  
//  private void doSomeShit() {
//    HttpHeaders headers = new HttpHeaders();
//    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//
//    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//            .queryParam("msisdn", msisdn)
//            .queryParam("email", email)
//            .queryParam("clientVersion", clientVersion)
//            .queryParam("clientType", clientType)
//            .queryParam("issuerName", issuerName)
//            .queryParam("applicationName", applicationName);
//
//    HttpEntity<?> entity = new HttpEntity<>(headers);
//
//    HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
//  }
  
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
    CaseDTO caseDTO = restTemplate.getForObject(caseFrameSvcCasesUrl, CaseDTO.class, caseId); 

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

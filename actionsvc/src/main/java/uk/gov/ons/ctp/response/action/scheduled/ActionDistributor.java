package uk.gov.ons.ctp.response.action.scheduled;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.ApplicationConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionTypeRepository;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

/**
 * This class has a scheduled task wakeUp(), which looks for Actions in
 * SUBMITTED state to send to downstream handlers. On each wake cycle it fetches
 * the first n actions of each type, by createddatatime, and attempts to enrich
 * them with case,questionnaire,address and caseevent details all fetched in
 * individual calls to the caseframe service through its RESTful endpoints.
 * 
 * It then updates its own action table to change the action state to PENDING,
 * posts a new CaseEvent to the CaseFrameService, and constructs an outbound
 * ActionRequest instance. That instance is added to the list of request objects
 * that will sent out as a batch inside an ActionInstruction to the
 * SpringIntegration @Publisher once the N actions for the individual type have
 * all been processed.
 * 
 */
@Named
@Slf4j
public class ActionDistributor {

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private RestClient caseFrameClient;

  @Inject
  private ApplicationConfig appConfig;

  @Inject
  private InstructionPublisher instructionPublisher;

  @Inject
  private MapperFacade mapperFacade;

  @Inject
  private ActionRepository actionRepo;

  @Inject
  private ActionTypeRepository actionTypeRepo;

  private List<ActionType> actionTypes;

  // single TransactionTemplate shared amongst all methods in this instance
  private final TransactionTemplate transactionTemplate;

  @Inject
  public ActionDistributor(PlatformTransactionManager transactionManager) {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }

  @Scheduled(initialDelay = 10 * 1000, fixedDelay = 5000) // 30 * 60 * 1000)
  public void wakeUp() {
    log.debug("ActionDistributor awoken from slumber");

    List<ActionType> actionTypes = getActionTypes();

    for (ActionType actionType : actionTypes) {
      List<Action> actions = actionRepo
          .findFirst100ByActionTypeNameAndStateOrderByCreatedDateTimeAsc(actionType.getName(), ActionState.SUBMITTED);

      // container for outbound requests for this action type
      List<ActionRequest> actionRequests = new ArrayList<ActionRequest>();

      log.debug("Dealing with actionType {}", actionType.getName());
      for (Action action : actions) {
        try {
          ActionRequest actionRequest = processAction(action);

          // add the request to the list to be published to downstream handler
          if (actionRequest != null) {
            log.debug("Adding actionRequest");
            actionRequests.add(actionRequest);
          }
        } catch (Exception e) {
          log.debug("Rolledback?");
        }
      }

      log.debug("Done with actionType {}", actionType.getName());
      if (actionRequests.size() > 0) {
        // send the list of requests for this action type to the handler
        instructionPublisher.sendRequests(actionType.getHandler(), actionRequests);
      }
    }

  }

  private ActionRequest processAction(final Action action) {
    return transactionTemplate.execute(new TransactionCallback<ActionRequest>() {
      // the code in this method executes in a transactional context
      public ActionRequest doInTransaction(TransactionStatus status) {
        ActionRequest actionRequest = null;
        log.debug("Preparing action {} for distribution", action.getActionId());

        // update our actions state in db
        updateActionStatusToPending(action);
        // create the request, filling in details by GETs from caseframesvc
        actionRequest = createActionRequest(action);

        // advise caseframesvc to create a corresponding caseevent for our
        // action
        postNewCaseEvent(action);

        return actionRequest;
      }
    });
  }

  private void updateActionStatusToPending(Action action) {
    action.setState(ActionState.PENDING);
    action.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
    action = actionRepo.saveAndFlush(action);
  }

  private void postNewCaseEvent(Action action) {
    log.debug("posting caseEvent for actionId {} to caseframesvc for creation", action.getActionId());
    CaseEventDTO caseEventDTO = new CaseEventDTO();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory("ActionCreated");
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setDescription(action.getActionType().getDescription());
    caseEventDTO.setSubCategory(null); // TODO - will be avail in data 2017+

    caseFrameClient.postResource(appConfig.getCaseFrameSvcCaseEventsByCasePostPath(), caseEventDTO, CaseEventDTO.class,
        action.getCaseId());
  }

  private ActionRequest createActionRequest(Action action) {
    log.debug("constructing ActionRequest to publish to downstream handler for action id {} and case id {}",
        action.getActionId(), action.getCaseId());
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
    actionRequest.setContactName(null); // TODO - will be avail in data 2017+
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

  private AddressDTO getAddress(Integer uprn) {
    AddressDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvcAddressByUprnGetPath(),
        AddressDTO.class, uprn);
    return caseDTO;
  }

  private QuestionnaireDTO getQuestionnaire(Integer caseId) {
    List<QuestionnaireDTO> questionnaireDTOs = caseFrameClient.getResources(
        appConfig.getCaseFrameSvcQuestionnairesByCaseGetPath(),
        QuestionnaireDTO[].class, caseId);
    return (questionnaireDTOs.size() > 0) ? questionnaireDTOs.get(0) : null;
  }

  private CaseDTO getCase(Integer caseId) throws RestClientException {
    CaseDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvcCaseGetPath(),
        CaseDTO.class, caseId);
    return caseDTO;
  }

  private List<CaseEventDTO> getCaseEvents(Integer caseId) {
    List<CaseEventDTO> caseEventDTOs = caseFrameClient.getResources(appConfig.getCaseFrameSvcCaseEventsByCaseGetPath(),
        CaseEventDTO[].class, caseId);
    return caseEventDTOs;
  }

  private List<ActionType> getActionTypes() {
    if (this.actionTypes == null) {
      this.actionTypes = actionTypeRepo.findAll();
    }
    return this.actionTypes;
  }

  private String formatCaseEvent(CaseEventDTO caseEventDTO) {
    return String.format(
        "%s : %s : %s : %s",
        caseEventDTO.getCategory(),
        caseEventDTO.getSubCategory(),
        caseEventDTO.getCreatedBy(),
        caseEventDTO.getDescription());
  }

}
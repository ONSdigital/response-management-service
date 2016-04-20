package uk.gov.ons.ctp.response.action.scheduled.impl;

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
import uk.gov.ons.ctp.response.action.config.AppConfig;
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
import uk.gov.ons.ctp.response.action.scheduled.ActionDistributor;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

/**
 * This is the 'service' class that distributes actions to downstream services
 * ie those services outside of response management It has a number of injected
 * beans, including a RestClient, Repositories and the InstructionPublisher
 *
 * It cannot use the normal serviceimpl @Transaction pattern, as that will
 * rollback on a runtime exception (desired) but will then rethrow that
 * exception all the way up the stack. If we try and catch that exception, the
 * rollback does not happen. So - see the TransactionTemplate usage - that
 * allows both rollback and for us to catch the runtime exception and handle it.
 *
 * This class has a scheduled task wakeUp(), which looks for Actions in
 * SUBMITTED state to send to downstream handlers. On each wake cycle, it fetches
 * the first n actions of each type, by createddatatime, and attempts to enrich
 * them with case, questionnaire, address and caseevent details all fetched in
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
public class ActionDistributorImpl implements ActionDistributor {
  // TODO - parameterize from external config
  public static final long DEV_DELAY_INITIAL = 10L * 1000L;
  public static final long DEV_DELAY_INTER = 10L * 1000L;
  public static final long PROD_DELAY_INITIAL = 10L * 1000L;
  public static final long PROD_DELAY_INTER = 30L * 60L * 1000L;

  @Inject
  private RestClient caseFrameClient;

  @Inject
  private AppConfig appConfig;

  @Inject
  private InstructionPublisher instructionPublisher;

  @Inject
  private MapperFacade mapperFacade;

  @Inject
  private ActionRepository actionRepo;

  @Inject
  private ActionTypeRepository actionTypeRepo;

  // single TransactionTemplate shared amongst all methods in this instance
  private final TransactionTemplate transactionTemplate;

  /**
   * Constructor into which the Spring PlatformTransactionManager is injected
   *
   * @param transactionManager provided by Spring
   */
  @Inject
  public ActionDistributorImpl(final PlatformTransactionManager transactionManager) {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * uk.gov.ons.ctp.response.action.scheduled.impl.ActionDistributor#wakeUp()
   */
  @Override
  @Scheduled(initialDelay = PROD_DELAY_INITIAL, fixedDelay = PROD_DELAY_INTER)
  public final void wakeUp() {
    log.debug("ActionDistributor awoken from slumber");

    try {
      // TODO PB These could be cached as they will not change very often.
      List<ActionType> actionTypes = actionTypeRepo.findAll();

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
              log.debug("Adding actionRequest to outbound list");
              actionRequests.add(actionRequest);
            }
          } catch (Exception e) {
            // db changes rolled back
            log.error(
                "Exception thrown processing action {}. Processing will be retried at next scheduled distribution",
                action.getActionId());
          }
        }

        log.debug("Done with actionType {}", actionType.getName());
        if (actionRequests.size() > 0) {
          // send the list of requests for this action type to the handler
          instructionPublisher.sendRequests(actionType.getHandler(), actionRequests);
        }
      }
    } catch (Exception e) {
      // something went wrong retrievnig action types or actions
      log.error("Failed to access local database for action types or actions");
      // we will be back after a short snoooze
    }
    log.debug("ActionDistributor going back to sleep");
  }

  /**
   * Deal with a single action - the transaction boundary is here. The
   * processing requires numerous calls to caseframe service and to write to our
   * own action table. The rollback most likely to be triggered by either
   * failing to find the caseframe service, or if it sends back an http error
   * status code.
   *
   * @param action the action to deal with
   * @return The resulting ActionRequest that will be added to the outbound
   *         ActionInstruction
   */
  private ActionRequest processAction(final Action action) {
    return transactionTemplate.execute(new TransactionCallback<ActionRequest>() {
      // the code in this method executes in a transactional context
      public ActionRequest doInTransaction(final TransactionStatus status) {
        ActionRequest actionRequest = null;
        log.debug("Preparing action {} for distribution", action.getActionId());

        // update our actions state in db
        updateActionStatusToPending(action);
        // create the request, filling in details by GETs from caseframesvc
        actionRequest = prepareActionRequest(action);

        // advise caseframesvc to create a corresponding caseevent for our
        // action
        postNewCaseEvent(action);

        return actionRequest;
      }
    });
  }

  /**
   * Change the action status in db to indicate we have sent this action
   * downstream
   *
   * @param action the action to change and persist
   */
  private void updateActionStatusToPending(final Action action) {
    action.setState(ActionState.PENDING);
    action.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
    actionRepo.saveAndFlush(action);
  }

  /**
   * Create and post to caseframe service a new CaseEvent
   *
   * @param action the action for which we need the event
   */
  private void postNewCaseEvent(final Action action) {
    log.debug("posting caseEvent for actionId {} to caseframesvc for creation", action.getActionId());
    CaseEventDTO caseEventDTO = new CaseEventDTO();
    caseEventDTO.setCaseId(action.getCaseId());
    caseEventDTO.setCategory("ActionCreated");
    caseEventDTO.setCreatedBy(action.getCreatedBy());
    caseEventDTO.setCreatedDateTime(new Date());
    caseEventDTO.setDescription(action.getActionType().getDescription());
    caseEventDTO.setSubCategory(null); // TODO - will be avail in data 2017+

    caseFrameClient.postResource(appConfig.getCaseFrameSvc().getCaseEventsByCasePostPath(), caseEventDTO,
        CaseEventDTO.class,
        action.getCaseId());
  }

  /**
   * Take an action and using it, fetch further info from caseframe service in a
   * number of rest calls, in order to create the ActionRequest
   *
   * @param action It all starts wih the Action
   * @return The ActionRequest created from the Action and the other info from
   *         CaseFrameSvc
   */
  private ActionRequest prepareActionRequest(final Action action) {
    log.debug("constructing ActionRequest to publish to downstream handler for action id {} and case id {}",
        action.getActionId(), action.getCaseId());
    // now call caseframe for the following
    CaseDTO caseDTO = getCase(action.getCaseId());
    QuestionnaireDTO questionnaireDTO = getQuestionnaire(action.getCaseId());
    AddressDTO addressDTO = getAddress(caseDTO.getUprn());
    List<CaseEventDTO> caseEventDTOs = getCaseEvents(action.getCaseId());

    return createActionRequest(action, caseDTO, questionnaireDTO, addressDTO, caseEventDTOs);
  }

  /**
   * Given the business objects passed, create, populate and return an
   * ActionRequest
   *
   * @param action the persistent Action obj from the db
   * @param caseDTO the Case representation from the CaseFrameSvc
   * @param questionnaireDTO the Questionnaire representation from the
   *          CaseFrameSvc
   * @param addressDTO the Address representation from the CaseFrameSvc
   * @param caseEventDTOs the list of CaseEvent represenations from the
   *          CaseFrameSvc
   * @return the shiney new Action Request
   */
  private ActionRequest createActionRequest(final Action action, final CaseDTO caseDTO,
      final QuestionnaireDTO questionnaireDTO,
      final AddressDTO addressDTO, final List<CaseEventDTO> caseEventDTOs) {
    ActionRequest actionRequest = new ActionRequest();
    // populate the request
    actionRequest.setActionId(BigInteger.valueOf(action.getActionId()));
    actionRequest.setActionType(action.getActionType().getName());
    actionRequest.setCaseId(BigInteger.valueOf(action.getCaseId()));
    actionRequest.setContactName(null); // TODO - will be avail in data 2017+
    ActionEvent actionEvent = new ActionEvent();
    for (CaseEventDTO caseEventDTO : caseEventDTOs) {
      actionEvent.getEvents().add(formatCaseEvent(caseEventDTO));
    }
    actionRequest.setEvents(actionEvent);
    actionRequest.setIac(questionnaireDTO.getIac());
    actionRequest.setPriority(Priority.fromValue(ActionPriority.valueOf(action.getPriority()).getName()));
    actionRequest.setQuestionnaireId(BigInteger.valueOf(questionnaireDTO.getQuestionnaireId()));
    actionRequest.setUprn(BigInteger.valueOf(caseDTO.getUprn()));

    ActionAddress actionAddress = mapperFacade.map(addressDTO, ActionAddress.class);
    actionRequest.setAddress(actionAddress);
    return actionRequest;
  }

  /**
   * Call CaseFrameSvc using REST to get the Address MAY throw a
   * RuntimeException if the call fails
   *
   * @param uprn identifies the Address to fetch
   * @return the Address we fetched
   */
  private AddressDTO getAddress(final Integer uprn) {
    AddressDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvc().getAddressByUprnGetPath(),
        AddressDTO.class, uprn);
    return caseDTO;
  }

  /**
   * Call CaseFrameSvc using REST to get the Questionnaire MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseId used to find the questionnaire
   * @return the Questionnaire we fetched
   */
  private QuestionnaireDTO getQuestionnaire(final Integer caseId) {
    List<QuestionnaireDTO> questionnaireDTOs = caseFrameClient.getResources(
        appConfig.getCaseFrameSvc().getQuestionnairesByCaseGetPath(),
        QuestionnaireDTO[].class, caseId);
    return (questionnaireDTOs.size() > 0) ? questionnaireDTOs.get(0) : null;
  }

  /**
   * Call CaseFrameSvc using REST to get the Case details MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseId identifies the Case to fetch
   * @return the Case we fetched
   * @throws RestClientException when we cannot connect or the service call errors
   */
  private CaseDTO getCase(final Integer caseId) throws RestClientException {
    CaseDTO caseDTO = caseFrameClient.getResource(appConfig.getCaseFrameSvc().getCaseByCaseGetPath(),
        CaseDTO.class, caseId);
    return caseDTO;
  }

  /**
   * Call CaseFrameSvc using REST to get the CaseEvents for the Case MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseId identifies the Case to fetch events for
   * @return the CaseEvents we found for the case
   * @throws RestClientException when we cannot connect or the service call errors
   */
  private List<CaseEventDTO> getCaseEvents(final Integer caseId) {
    List<CaseEventDTO> caseEventDTOs = caseFrameClient.getResources(
        appConfig.getCaseFrameSvc().getCaseEventsByCaseGetPath(),
        CaseEventDTO[].class, caseId);
    return caseEventDTOs;
  }

  /**
   * Formats a CaseEvent as a string that can added to the ActionRequest
   *
   * @param caseEventDTO the DTO to be formatted
   * @return the pretty one liner
   */
  private String formatCaseEvent(final CaseEventDTO caseEventDTO) {
    return String.format(
        "%s : %s : %s : %s",
        caseEventDTO.getCategory(),
        caseEventDTO.getSubCategory(),
        caseEventDTO.getCreatedBy(),
        caseEventDTO.getDescription());
  }

}

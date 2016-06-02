package uk.gov.ons.ctp.response.action.scheduled;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.state.StateTransitionException;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionTypeRepository;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.CaseFrameSvcClientService;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CategoryDTO;
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
 * This class has a self scheduled method wakeUp(), which looks for Actions in
 * SUBMITTED state to send to downstream handlers. On each wake cycle, it
 * fetches the first n actions of each type, by createddatatime, and attempts to
 * enrich them with case, questionnaire, address and caseevent details all
 * fetched in individual calls to the caseframe service through its RESTful
 * endpoints.
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
public class ActionDistributorImpl {

  private static final long MILLISECONDS = 1000L;

  @Inject
  private AppConfig appConfig;

  @Inject
  private StateTransitionManager<ActionState, ActionDTO.ActionEvent> actionSvcStateTransitionManager;

  @Inject
  private InstructionPublisher instructionPublisher;

  @Inject
  private MapperFacade mapperFacade;

  @Inject
  private ActionRepository actionRepo;

  @Inject
  private CaseFrameSvcClientService caseFrameSvcClientService;

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

  /**
   * wake up on schedule and check for submitted actions, enrich and distribute
   * them to spring integration channels
   * @return the info for the health endpoint regarding the distribution just performed
   */
  public final DistributionInfo distribute() {
    log.debug("ActionDistributor awoken from slumber");
    DistributionInfo distInfo = new DistributionInfo();
    DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    distInfo.setLastRunTime(dateTimeInstance.format(Calendar.getInstance().getTime()));

    try {
      List<ActionType> actionTypes = actionTypeRepo.findAll();

      for (ActionType actionType : actionTypes) {

        Pageable pageable = new PageRequest(0, appConfig.getActionDistribution().getInstructionMax(), new Sort(
            new Sort.Order(Direction.ASC, "createdDateTime")));

        List<Action> actions = actionRepo
            .findByActionTypeNameAndStateIn(actionType.getName(),
                Arrays.asList(ActionState.SUBMITTED, ActionState.CANCEL_SUBMITTED), pageable);

        // container for outbound requests for this action type
        List<ActionRequest> actionRequests = new ArrayList<>();
        List<ActionCancel> actionCancels = new ArrayList<>();

        log.debug("Dealing with actionType {}", actionType.getName());
        StringBuilder sbRequests = new StringBuilder("Action ids for Requests : ");
        StringBuilder sbCancels = new StringBuilder("Action ids for Cancels : ");
        for (Action action : actions) {
          try {
            if (action.getState().equals(ActionDTO.ActionState.SUBMITTED)) {
              actionRequests.add(processActionRequest(action));
              sbRequests.append(action.getActionId()).append(",");
            } else if (action.getState().equals(ActionDTO.ActionState.CANCEL_SUBMITTED)) {
              actionCancels.add(processActionCancel(action));
              sbCancels.append(action.getActionId()).append(",");
            }
          } catch (Exception e) {
            // db changes rolled back
            log.error(
                "Exception thrown processing action {}. Processing will be retried at next scheduled distribution",
                action.getActionId());
          }
        }
        distInfo.addInstructionCount(
            new InstructionCount(actionType.getName(), DistributionInfo.Instruction.REQUEST, actionRequests.size()));
        distInfo.addInstructionCount(new InstructionCount(actionType.getName(),
            DistributionInfo.Instruction.CANCEL_REQUEST, actionCancels.size()));

        boolean published = false;
        if (actionRequests.size() > 0 || actionCancels.size() > 0) {
          do {
            try {
              // send the list of requests for this action type to the handler
              log.debug("Publishing instruction");
              instructionPublisher.sendInstructions(actionType.getHandler(), actionRequests, actionCancels);
              published = true;
            } catch (Exception e) {
              // broker not there ? sleep then retry
              log.error(sbRequests.toString());
              log.error(sbCancels.toString());
              log.error("Problem sending action instruction for preceeding ids to handler {} due to {}", actionType, e);
              Thread.sleep(appConfig.getActionDistribution().getRetrySleepSeconds() * MILLISECONDS);
            }
          } while (!published);
        }

        log.debug("Actions processed for actionType {}", actionType.getName());
      }
    } catch (Exception e) {
      // something went wrong retrieving action types or actions
      log.error("Failed to process actions because {}", e);
      // we will be back after a short snooze
    }
    log.debug("ActionDistributor going back to sleep");
    return distInfo;
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
  private ActionRequest processActionRequest(final Action action) {
    return transactionTemplate.execute(new TransactionCallback<ActionRequest>() {
      // the code in this method executes in a transactional context
      public ActionRequest doInTransaction(final TransactionStatus status) {
        ActionRequest actionRequest = null;
        // update our actions state in db
        transitionAction(action, ActionDTO.ActionEvent.REQUEST_DISTRIBUTED);
        // create the request, filling in details by GETs from caseframesvc
        actionRequest = prepareActionRequest(action);
        // advise caseframesvc to create a corresponding caseevent for our
        // action
        caseFrameSvcClientService.createNewCaseEvent(action, CategoryDTO.CategoryName.ACTION_CREATED);
        return actionRequest;
      }
    });
  }

  /**
   * Deal with a single action cancel- the transaction boundary is here
   *
   * @param action the action to deal with
   * @return The resulting ActionCancel that will be added to the outbound
   *         ActionInstruction
   */
  private ActionCancel processActionCancel(final Action action) {
    return transactionTemplate.execute(new TransactionCallback<ActionCancel>() {
      // the code in this method executes in a transactional context
      public ActionCancel doInTransaction(final TransactionStatus status) {
        ActionCancel actionCancel = null;
        log.debug("Preparing action {} for distribution", action.getActionId());

        // update our actions state in db
        transitionAction(action, ActionDTO.ActionEvent.CANCELLATION_DISTRIBUTED);
        // create the request, filling in details by GETs from caseframesvc
        actionCancel = prepareActionCancel(action);
        // advise caseframesvc to create a corresponding caseevent for our
        // action
        caseFrameSvcClientService.createNewCaseEvent(action, CategoryDTO.CategoryName.ACTION_CANCELLATION_CREATED);
        return actionCancel;
      }
    });
  }

  /**
   * Change the action status in db to indicate we have sent this action
   * downstream, and clear previous situation (in the scenario where the action
   * has prev. failed)
   *
   * @param action the action to change and persist
   * @param event the event to transition the action with
   * @return the transitioned action
   */
  private Action transitionAction(final Action action, final ActionDTO.ActionEvent event) {
    Action updatedAction = null;
    try {
      ActionDTO.ActionState nextState = actionSvcStateTransitionManager.transition(action.getState(), event);
      action.setState(nextState);
      action.setSituation(null);
      action.setUpdatedDateTime(new Timestamp(System.currentTimeMillis()));
      updatedAction = actionRepo.saveAndFlush(action);
    } catch (StateTransitionException ste) {
      throw new RuntimeException(ste);
    }
    return updatedAction;
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
    CaseDTO caseDTO = caseFrameSvcClientService.getCase(action.getCaseId());
    QuestionnaireDTO questionnaireDTO = caseFrameSvcClientService.getQuestionnaire(action.getCaseId());
    AddressDTO addressDTO = caseFrameSvcClientService.getAddress(caseDTO.getUprn());
    List<CaseEventDTO> caseEventDTOs = caseFrameSvcClientService.getCaseEvents(action.getCaseId());

    return createActionRequest(action, caseDTO, questionnaireDTO, addressDTO, caseEventDTOs);
  }

  /**
   * Take an action and using it, fetch further info from caseframe service in a
   * number of rest calls, in order to create the ActionRequest
   *
   * @param action It all starts wih the Action
   * @return The ActionRequest created from the Action and the other info from
   *         CaseFrameSvc
   */
  private ActionCancel prepareActionCancel(final Action action) {
    log.debug("constructing ActionCancel to publish to downstream handler for action id {} and case id {}",
        action.getActionId(), action.getCaseId());
    ActionCancel actionCancel = new ActionCancel();
    actionCancel.setActionId(BigInteger.valueOf(action.getActionId()));
    actionCancel.setReason("Action cancelled by Response Management");
    return actionCancel;
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
    caseEventDTOs.forEach((caseEventDTO) -> actionEvent.getEvents().add(formatCaseEvent(caseEventDTO)));
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

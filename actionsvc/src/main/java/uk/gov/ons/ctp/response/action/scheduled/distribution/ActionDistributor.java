package uk.gov.ons.ctp.response.action.scheduled.distribution;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.state.StateTransitionException;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.ActionSvcApplication;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionTypeRepository;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionAddress;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionContact;
import uk.gov.ons.ctp.response.action.message.instruction.ActionEvent;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.CaseSvcClientService;
import uk.gov.ons.ctp.response.casesvc.representation.AddressDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseGroupDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseTypeDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;
import uk.gov.ons.ctp.response.casesvc.representation.ContactDTO;

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
 * fetched in individual calls to the Case service through its RESTful
 * endpoints.
 *
 * It then updates its own action table to change the action state to PENDING,
 * posts a new CaseEvent to the Case Service, and constructs an outbound
 * ActionRequest instance. That instance is added to the list of request objects
 * that will sent out as a batch inside an ActionInstruction to the
 * SpringIntegration @Publisher once the N actions for the individual type have
 * all been processed.
 *
 */
@Named
@Slf4j
public class ActionDistributor {

  private static final String ACTION_DISTRIBUTOR_SPAN = "actionDistributor";

  private static final long IMPOSSIBLE_ACTION_ID = 999999999999L;

  private static final long MILLISECONDS = 1000L;

  @Inject
  private Tracer tracer;

  @Inject
  private HazelcastInstance hazelcastInstance;

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
  private ActionPlanRepository actionPlanRepo;

  @Inject
  private CaseSvcClientService caseSvcClientService;

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
  public ActionDistributor(final PlatformTransactionManager transactionManager) {
    this.transactionTemplate = new TransactionTemplate(transactionManager);
  }

  /**
   * wake up on schedule and check for submitted actions, enrich and distribute
   * them to spring integration channels
   *
   * @return the info for the health endpoint regarding the distribution just
   *         performed
   */
  @SuppressWarnings("unchecked")
  public final DistributionInfo distribute() {
    Span distribSpan = tracer.createSpan(ACTION_DISTRIBUTOR_SPAN);
    log.info("ActionDistributor is in the house");
    DistributionInfo distInfo = new DistributionInfo();

    IMap<Object, Object> actionMap = hazelcastInstance.getMap(ActionSvcApplication.ACTION_DISTRIBUTION_MAP);
    String localUUID = hazelcastInstance.getLocalEndpoint().getUuid();
    log.debug("Hazel name is {}", localUUID);
    try {
      actionTypeRepo.findAll().forEach(actionType -> {
        // container for outbound requests for this action type
        List<ActionRequest> actionRequests = new ArrayList<>();
        List<ActionCancel> actionCancels = new ArrayList<>();

        log.debug("Dealing with actionType {}", actionType.getName());

        List<BigInteger> excludedActions = actionMap.values().stream()
            .flatMap(o -> ((List<BigInteger>) o).stream())
            .collect(Collectors.toList());
        log.debug("Excluding actions {}", excludedActions);
        List<Action> actions = retrieveActions(actionType, excludedActions);
        log.debug("Dealing with actions {}",
            actions.stream()
                .map(a -> a.getActionId().toString())
                .collect(Collectors.joining(",")));

        actionMap.put(localUUID,
            actions.stream()
                .map(a -> a.getActionId())
                .collect(Collectors.toList()));

        actions.forEach(action -> {
          try {
            if (action.getState().equals(ActionDTO.ActionState.SUBMITTED)) {
              actionRequests.add(processActionRequest(action));
            } else if (action.getState().equals(ActionDTO.ActionState.CANCEL_SUBMITTED)) {
              actionCancels.add(processActionCancel(action));
            }
            log.debug("dealt with action {}", action.getActionId());
          } catch (Exception e) {
            // db changes rolled back
            log.error(
                "Exception thrown processing action {}. Processing will be retried at next scheduled distribution",
                action.getActionId());
          }
        });

        distInfo.getInstructionCounts().add(new InstructionCount(actionType.getName(),
            DistributionInfo.Instruction.REQUEST, actionRequests.size()));
        distInfo.getInstructionCounts().add(new InstructionCount(actionType.getName(),
            DistributionInfo.Instruction.CANCEL_REQUEST, actionCancels.size()));

        publishActions(actionType, actionRequests, actionCancels);

        actionMap.remove(localUUID);
        log.debug("Actions processed for actionType {}", actionType.getName());
      });
    } catch (Exception e) {
      // something went wrong retrieving action types or actions
      log.error("Failed to process actions because {}", e);
      // we will be back after a short snooze
    }
    log.info("ActionDistributor going back to sleep");
    tracer.close(distribSpan);
    return distInfo;
  }

  /**
   * publish actions using the inject publisher - try and try and try ...
   *
   * @param actionRequests requests
   * @param actionType the type
   * @param actionCancels cancels
   * @throws InterruptedException our pause was interrupted
   */
  private void publishActions(ActionType actionType, List<ActionRequest> actionRequests,
      List<ActionCancel> actionCancels) {
    boolean published = false;
    if (actionRequests.size() > 0 || actionCancels.size() > 0) {
      do {
        try {
          // send the list of requests for this action type to the
          // handler
          log.debug("Publishing instruction");
          instructionPublisher.sendInstructions(actionType.getHandler(), actionRequests, actionCancels);
          published = true;
        } catch (Exception e) {
          // broker not there ? sleep then retry
          log.warn("Failed to send requests {}", actionRequests.stream().map(a -> a.getActionId().toString())
              .collect(Collectors.joining(",")));
          log.warn("Failed to send cancels {}", actionCancels.stream().map(a -> a.getActionId().toString())
              .collect(Collectors.joining(",")));
          log.warn("Problem sending action instruction for preceeding ids to handler {} due to {}",
              actionType, e);
          log.warn("ActionDistibution will sleep and retry publish");
          try {
            Thread.sleep(appConfig.getActionDistribution().getRetrySleepSeconds() * MILLISECONDS);
          } catch (InterruptedException ie) {
            log.warn("Retry sleep was interrupted");
          }
        }
      } while (!published);
    }
  }

  /**
   * Get the oldest page of submitted actions by type
   *
   * @param actionType the type
   * @param excludedActionIds the list of action ids to exclude
   * @return list of actions
   */
  private List<Action> retrieveActions(ActionType actionType, List<BigInteger> excludedActionIds) {
    Pageable pageable = new PageRequest(0, appConfig.getActionDistribution().getInstructionMax(), new Sort(
        new Sort.Order(Direction.ASC, "updatedDateTime")));
    excludedActionIds.add(BigInteger.valueOf(IMPOSSIBLE_ACTION_ID));
    List<Action> actions = actionRepo
        .findByActionTypeNameAndStateInAndActionIdNotIn(actionType.getName(),
            Arrays.asList(ActionState.SUBMITTED, ActionState.CANCEL_SUBMITTED), excludedActionIds, pageable);
    log.debug("RETRIEVED action ids {}", actions.stream().map(a -> a.getActionId().toString())
        .collect(Collectors.joining(",")));
    return actions;
  }

  /**
   * Deal with a single action - the transaction boundary is here. The
   * processing requires numerous calls to Case service and to write to our own
   * action table. The rollback most likely to be triggered by either failing to
   * find the Case service, or if it sends back an http error status code.
   *
   * @param action the action to deal with
   * @return The resulting ActionRequest that will be added to the outbound
   *         ActionInstruction
   */
  private ActionRequest processActionRequest(final Action action) {
    log.info("processing action REQUEST actionid {} caseid {} actionplanid {}", action.getActionId(),
        action.getCaseId(), action.getActionPlanId());
    return transactionTemplate.execute(new TransactionCallback<ActionRequest>() {
      // the code in this method executes in a transactional context
      public ActionRequest doInTransaction(final TransactionStatus status) {
        ActionRequest actionRequest = null;
        // update our actions state in db
        transitionAction(action, ActionDTO.ActionEvent.REQUEST_DISTRIBUTED);
        // create the request, filling in details by GETs from casesvc
        actionRequest = prepareActionRequest(action);
        // advise casesvc to create a corresponding caseevent for our action
        caseSvcClientService.createNewCaseEvent(action, CategoryDTO.CategoryType.ACTION_CREATED);
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
    log.info("processing action REQUEST actionid {} caseid {} actionplanid {}", action.getActionId(),
        action.getCaseId(), action.getActionPlanId());
    return transactionTemplate.execute(new TransactionCallback<ActionCancel>() {
      // the code in this method executes in a transactional context
      public ActionCancel doInTransaction(final TransactionStatus status) {
        ActionCancel actionCancel = null;
        log.debug("Preparing action {} for distribution", action.getActionId());

        // update our actions state in db
        transitionAction(action, ActionDTO.ActionEvent.CANCELLATION_DISTRIBUTED);
        // create the request, filling in details by GETs from casesvc
        actionCancel = prepareActionCancel(action);
        // advise casesvc to create a corresponding caseevent for our action
        caseSvcClientService.createNewCaseEvent(action, CategoryDTO.CategoryType.ACTION_CANCELLATION_CREATED);
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
      action.setUpdatedDateTime(DateTimeUtil.nowUTC());
      updatedAction = actionRepo.saveAndFlush(action);
    } catch (StateTransitionException ste) {
      throw new RuntimeException(ste);
    }
    return updatedAction;
  }

  /**
   * Take an action and using it, fetch further info from Case service in a
   * number of rest calls, in order to create the ActionRequest
   *
   * @param action It all starts with the Action
   * @return The ActionRequest created from the Action and the other info from
   *         CaseSvc
   */
  private ActionRequest prepareActionRequest(final Action action) {
    log.debug("constructing ActionRequest to publish to downstream handler for action id {} and case id {}",
        action.getActionId(), action.getCaseId());
    // now call caseSvc for the following
    ActionPlan actionPlan = (action.getActionPlanId() == null) ? null : actionPlanRepo.findOne(action.getActionPlanId());
    CaseDTO caseDTO = caseSvcClientService.getCase(action.getCaseId());
    CaseTypeDTO caseTypeDTO = caseSvcClientService.getCaseType(caseDTO.getCaseTypeId());
    CaseGroupDTO caseGroupDTO = caseSvcClientService.getCaseGroup(caseDTO.getCaseGroupId());
    AddressDTO addressDTO = caseSvcClientService.getAddress(caseGroupDTO.getUprn());
    List<CaseEventDTO> caseEventDTOs = caseSvcClientService.getCaseEvents(action.getCaseId());

    return createActionRequest(action, actionPlan, caseDTO, caseTypeDTO, addressDTO, caseEventDTOs);
  }

  /**
   * Take an action and using it, fetch further info from Case service in a
   * number of rest calls, in order to create the ActionRequest
   *
   * @param action It all starts wih the Action
   * @return The ActionRequest created from the Action and the other info from
   *         CaseSvc
   */
  private ActionCancel prepareActionCancel(final Action action) {
    log.debug("constructing ActionCancel to publish to downstream handler for action id {} and case id {}",
        action.getActionId(), action.getCaseId());
    ActionCancel actionCancel = new ActionCancel();
    actionCancel.setActionId(action.getActionId());
    actionCancel.setResponseRequired(true);
    actionCancel.setReason("Action cancelled by Response Management");
    return actionCancel;
  }

  /**
   * Given the business objects passed, create, populate and return an
   * ActionRequest
   *
   * @param action the persistent Action obj from the db
   * @param actionPlan the persistent ActionPlan obj from the db
   * @param caseDTO the Case representation from the CaseSvc
   * @param questionnaireDTO the Questionnaire representation from the CaseSvc
   * @param addressDTO the Address representation from the CaseSvc
   * @param caseEventDTOs the list of CaseEvent represenations from the CaseSvc
   * @return the shiney new Action Request
   */
  private ActionRequest createActionRequest(final Action action, final ActionPlan actionPlan, final CaseDTO caseDTO, final CaseTypeDTO caseTypeDTO,
      final AddressDTO addressDTO, final List<CaseEventDTO> caseEventDTOs) {
    ActionRequest actionRequest = new ActionRequest();
    // populate the request
    actionRequest.setActionId(action.getActionId());
    actionRequest.setActionPlan((actionPlan==null)?null:actionPlan.getName());
    actionRequest.setActionType(action.getActionType().getName());
    actionRequest.setQuestionSet(caseTypeDTO.getQuestionSet());
    actionRequest.setResponseRequired(true);
    actionRequest.setCaseId(BigInteger.valueOf(action.getCaseId()));
    ContactDTO contactDTO = caseDTO.getContact();
    if (contactDTO != null) {
      ActionContact actionContact = new ActionContact();
      actionContact.setTitle(contactDTO.getTitle());
      actionContact.setForename(contactDTO.getForename());
      actionContact.setSurname(contactDTO.getSurname());
      actionContact.setPhoneNumber(contactDTO.getPhoneNumber());
      actionContact.setEmailAddress(contactDTO.getEmailAddress());
      actionRequest.setContact(actionContact);
    }
    ActionEvent actionEvent = new ActionEvent();
    caseEventDTOs.forEach((caseEventDTO) -> actionEvent.getEvents().add(formatCaseEvent(caseEventDTO)));
    actionRequest.setEvents(actionEvent);
    actionRequest.setIac(caseDTO.getIac());
    actionRequest.setPriority(Priority.fromValue(ActionPriority.valueOf(action.getPriority()).getName()));
    actionRequest.setCaseRef(caseDTO.getCaseRef());

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
    return String.format("%s : %s : %s : %s", caseEventDTO.getCategory(), caseEventDTO.getSubCategory(),
        caseEventDTO.getCreatedBy(), caseEventDTO.getDescription());
  }

}

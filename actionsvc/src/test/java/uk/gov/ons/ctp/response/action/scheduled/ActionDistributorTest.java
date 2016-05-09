package uk.gov.ons.ctp.response.action.scheduled;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestClientException;

import ma.glasnost.orika.MapperFacade;
import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.response.action.config.ActionDistribution;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.config.CaseFrameSvc;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionTypeRepository;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.CaseFrameSvcClientService;
import uk.gov.ons.ctp.response.caseframe.representation.AddressDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CategoryDTO;
import uk.gov.ons.ctp.response.caseframe.representation.QuestionnaireDTO;

//@RunWith(MockitoJUnitRunner.class)
public class ActionDistributorTest {

  @Mock
  AppConfig appConfig;

  @Mock
  InstructionPublisher instructionPublisher;

  @Mock
  StateTransitionManager<ActionState, uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent> actionSvcStateTransitionManager;

  @Mock
  MapperFacade mapperFacade;

  @Mock
  CaseFrameSvcClientService caseFrameSvcClientService;

  @Mock
  ActionRepository actionRepo;

  @Mock
  ActionTypeRepository actionTypeRepo;

  @Mock
  TransactionTemplate transactionTemplate;

  @Mock
  PlatformTransactionManager platformTransactionManager;

  @InjectMocks
  ActionDistributorImpl actionDistributor;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Test that when we fail at first hurdle to load ActionTypes we do not go on
   * to call anything else In reality the wakeup mathod would then be called
   * again after a sleep interval by spring but we cannot test that here
   * 
   * @throws Exception
   */
  //@Test
  public void testFailGetActionType() throws Exception {
    // set up dummy data
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();

    // wire up mock responses
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);

    Mockito.when(actionTypeRepo.findAll()).thenThrow(new RuntimeException("Database access failed"));

    // let it roll
    actionDistributor.wakeUp();

    // assert the right calls were made
    verify(actionTypeRepo).findAll();
    verify(actionRepo, times(0)).findByActionTypeNameAndStateIn(eq("HouseholdInitialContact"),
        anyListOf(ActionState.class), any(Pageable.class));
    verify(actionRepo, times(0)).findByActionTypeNameAndStateIn(eq("HouseholdUploadIAC"),
        anyListOf(ActionState.class), any(Pageable.class));

    verify(caseFrameSvcClientService, times(0)).getQuestionnaire(eq(1));
    verify(caseFrameSvcClientService, times(0)).getQuestionnaire(eq(2));
    verify(caseFrameSvcClientService, times(0)).getQuestionnaire(eq(3));
    verify(caseFrameSvcClientService, times(0)).getQuestionnaire(eq(4));

    verify(caseFrameSvcClientService, times(0)).getCase(eq(3));
    verify(caseFrameSvcClientService, times(0)).getCase(eq(4));

    verify(caseFrameSvcClientService, times(0)).getAddress(eq(1234L));

    verify(caseFrameSvcClientService, times(0)).getCaseEvents(eq(3));
    verify(caseFrameSvcClientService, times(0)).getCaseEvents(eq(4));

    verify(caseFrameSvcClientService, times(0)).createNewCaseEvent(any(Action.class), eq(CategoryDTO.CategoryName.ACTION_CREATED));

    verify(instructionPublisher, times(0)).sendInstructions(eq("Printer"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
    verify(instructionPublisher, times(0)).sendInstructions(eq("HHSurvey"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
  }

  /**
   * Test that when we momentarily fail to call caseframesvc to GET two cases we
   * carry on trying and succesfully deal with the actions/cases we can retrieve
   * 
   * @throws Exception
   */
 // @Test
  public void testFailCaseGet() throws Exception {
    // set up dummy data
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();
    ActionDistribution actionDistribution = new ActionDistribution();

    List<ActionType> actionTypes = FixtureHelper.loadClassFixtures(ActionType[].class);

    List<Action> actionsHHIC = FixtureHelper.loadClassFixtures(Action[].class, "HouseholdInitialContact");
    List<Action> actionsHHIACLOAD = FixtureHelper.loadClassFixtures(Action[].class, "HouseholdUploadIAC");

    List<QuestionnaireDTO> questionnaireDTOs = FixtureHelper.loadClassFixtures(QuestionnaireDTO[].class);

    List<CaseDTO> caseDTOs = FixtureHelper.loadClassFixtures(CaseDTO[].class);

    List<AddressDTO> addressDTOsUprn1234 = FixtureHelper.loadClassFixtures(AddressDTO[].class, "uprn1234");

    List<CaseEventDTO> caseEventDTOs = FixtureHelper.loadClassFixtures(CaseEventDTO[].class);

    List<CaseEventDTO> caseEventDTOsPost = FixtureHelper.loadClassFixtures(CaseEventDTO[].class, "post");

    // wire up mock responses
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.SUBMITTED, ActionDTO.ActionEvent.REQUEST_DISTRIBUTED)).thenReturn(ActionState.PENDING);
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);
    Mockito.when(appConfig.getActionDistribution()).thenReturn(actionDistribution);
    Mockito.when(actionTypeRepo.findAll()).thenReturn(actionTypes);
    Mockito
        .when(actionRepo.findByActionTypeNameAndStateIn(eq("HouseholdInitialContact"), anyListOf(ActionState.class), any(Pageable.class)))
        .thenReturn(actionsHHIC);
    Mockito.when(
        actionRepo.findByActionTypeNameAndStateIn(eq("HouseholdUploadIAC"), anyListOf(ActionState.class), any(Pageable.class)))
        .thenReturn(actionsHHIACLOAD);

    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(1)))
        .thenThrow(new RestClientException("CaseFrameService Temporarily Unavailable"));
    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(2)))
        .thenThrow(new RestClientException("CaseFrameService Temporarily Unavailable"));
    Mockito.when(caseFrameSvcClientService.getCase(eq(3))).thenReturn(caseDTOs.get(2));
    Mockito.when(caseFrameSvcClientService.getCase(eq(4))).thenReturn(caseDTOs.get(3));

    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(3))).thenReturn(questionnaireDTOs.get(2));
    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(4))).thenReturn(questionnaireDTOs.get(3));

    Mockito.when(caseFrameSvcClientService.getAddress(eq(1234L)))
        .thenReturn(addressDTOsUprn1234.get(0));

    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(3)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(2)}));
    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(4)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(3)}));

    Mockito.when(caseFrameSvcClientService.createNewCaseEvent(any(Action.class), eq(CategoryDTO.CategoryName.ACTION_CREATED)))
        .thenReturn(caseEventDTOsPost.get(2));

    // let it roll
    actionDistributor.wakeUp();

    // assert the right calls were made
    verify(actionTypeRepo).findAll();  
    verify(actionRepo).findByActionTypeNameAndStateIn(eq("HouseholdInitialContact"),
        anyListOf(ActionState.class), any(Pageable.class));
    verify(actionRepo).findByActionTypeNameAndStateIn(eq("HouseholdUploadIAC"),
        anyListOf(ActionState.class), any(Pageable.class));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(1));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(2));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(3));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(4));

    verify(caseFrameSvcClientService).getCase(eq(3));
    verify(caseFrameSvcClientService).getCase(eq(4));

    verify(caseFrameSvcClientService, times(2)).getAddress(eq(1234L));

    verify(caseFrameSvcClientService).getCaseEvents(eq(3));
    verify(caseFrameSvcClientService).getCaseEvents(eq(4));

    verify(caseFrameSvcClientService, times(2)).createNewCaseEvent(any(Action.class), eq(CategoryDTO.CategoryName.ACTION_CREATED));

    verify(instructionPublisher, times(0)).sendInstructions(eq("Printer"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
    verify(instructionPublisher, times(1)).sendInstructions(eq("HHSurvey"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
  }

  /**
   * Test BlueSky scenario - two action types, four cases etc resulting in two calls to publish
   * @throws Exception
   */
  //@Test
  public void testBlueSky() throws Exception {
    // set up dummy data
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();
    ActionDistribution actionDistribution = new ActionDistribution();

    List<ActionType> actionTypes = FixtureHelper.loadClassFixtures(ActionType[].class);

    List<Action> actionsHHIC = FixtureHelper.loadClassFixtures(Action[].class, "HouseholdInitialContact");
    List<Action> actionsHHIACLOAD = FixtureHelper.loadClassFixtures(Action[].class, "HouseholdUploadIAC");

    List<QuestionnaireDTO> questionnaireDTOs = FixtureHelper.loadClassFixtures(QuestionnaireDTO[].class);

    List<CaseDTO> caseDTOs = FixtureHelper.loadClassFixtures(CaseDTO[].class);

    List<AddressDTO> addressDTOsUprn1234 = FixtureHelper.loadClassFixtures(AddressDTO[].class, "uprn1234");

    List<CaseEventDTO> caseEventDTOs = FixtureHelper.loadClassFixtures(CaseEventDTO[].class);

    List<CaseEventDTO> caseEventDTOsPost = FixtureHelper.loadClassFixtures(CaseEventDTO[].class, "post");

    // wire up mock responses
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.SUBMITTED, ActionDTO.ActionEvent.REQUEST_DISTRIBUTED)).thenReturn(ActionState.PENDING);

    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);
    Mockito.when(appConfig.getActionDistribution()).thenReturn(actionDistribution);
    Mockito.when(actionTypeRepo.findAll()).thenReturn(actionTypes);
    Mockito
        .when(actionRepo.findByActionTypeNameAndStateIn(eq("HouseholdInitialContact"), anyListOf(ActionState.class), any(Pageable.class)))
        .thenReturn(actionsHHIC);
    Mockito.when(
        actionRepo.findByActionTypeNameAndStateIn(eq("HouseholdUploadIAC"), anyListOf(ActionState.class), any(Pageable.class)))
        .thenReturn(actionsHHIACLOAD);

    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(1)))
        .thenReturn(questionnaireDTOs.get(0));
    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(2)))
        .thenReturn(questionnaireDTOs.get(1));
    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(3)))
        .thenReturn(questionnaireDTOs.get(2));
    Mockito.when(caseFrameSvcClientService.getQuestionnaire(eq(4)))
        .thenReturn(questionnaireDTOs.get(3));

    Mockito.when(caseFrameSvcClientService.getCase(eq(1))).thenReturn(caseDTOs.get(0));
    Mockito.when(caseFrameSvcClientService.getCase(eq(2))).thenReturn(caseDTOs.get(1));
    Mockito.when(caseFrameSvcClientService.getCase(eq(3))).thenReturn(caseDTOs.get(2));
    Mockito.when(caseFrameSvcClientService.getCase(eq(4))).thenReturn(caseDTOs.get(3));

    Mockito.when(caseFrameSvcClientService.getAddress(eq(1234L)))
        .thenReturn(addressDTOsUprn1234.get(0));

    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(1)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(0)}));
    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(2)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(1)}));
    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(3)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(2)}));
    Mockito.when(caseFrameSvcClientService.getCaseEvents(eq(4)))
        .thenReturn(Arrays.asList(new CaseEventDTO[] {caseEventDTOs.get(3)}));

    Mockito.when(caseFrameSvcClientService.createNewCaseEvent(any(Action.class), eq(CategoryDTO.CategoryName.ACTION_CREATED)))
        .thenReturn(caseEventDTOsPost.get(2));


    // let it roll
    actionDistributor.wakeUp();

    // assert the right calls were made
    verify(actionTypeRepo).findAll();
    verify(actionRepo).findByActionTypeNameAndStateIn(eq("HouseholdInitialContact"),
        anyListOf(ActionState.class), any(Pageable.class));
    verify(actionRepo).findByActionTypeNameAndStateIn(eq("HouseholdUploadIAC"),
        anyListOf(ActionState.class), any(Pageable.class));

    verify(caseFrameSvcClientService).getQuestionnaire(eq(1));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(2));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(3));
    verify(caseFrameSvcClientService).getQuestionnaire(eq(4));

    verify(caseFrameSvcClientService).getCase(eq(1));
    verify(caseFrameSvcClientService).getCase(eq(2));
    verify(caseFrameSvcClientService).getCase(eq(3));
    verify(caseFrameSvcClientService).getCase(eq(4));

    verify(caseFrameSvcClientService, times(4)).getAddress(eq(1234L));

    verify(caseFrameSvcClientService).getCaseEvents(eq(1));
    verify(caseFrameSvcClientService).getCaseEvents(eq(2));
    verify(caseFrameSvcClientService).getCaseEvents(eq(3));
    verify(caseFrameSvcClientService).getCaseEvents(eq(4));

    verify(caseFrameSvcClientService, times(4)).createNewCaseEvent(any(Action.class), eq(CategoryDTO.CategoryName.ACTION_CREATED));

    verify(instructionPublisher, times(1)).sendInstructions(eq("Printer"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
    verify(instructionPublisher, times(1)).sendInstructions(eq("HHSurvey"), anyListOf(ActionRequest.class), anyListOf(ActionCancel.class));
  }
}

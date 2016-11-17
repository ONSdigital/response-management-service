package uk.gov.ons.ctp.response.action.service.impl;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.ctp.common.FixtureHelper;
import uk.gov.ons.ctp.common.state.StateTransitionException;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.SituationCategory;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.SituationCategoryRepository;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionEvent;
import uk.gov.ons.ctp.response.action.service.CaseSvcClientService;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;

/**
 * A test of the case frame service client service
 */
@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceImplTest {

  @Mock
  private CaseSvcClientService caseSvcClientService;

  @Mock
  private ActionRepository actionRepo;

  @Mock
  private SituationCategoryRepository situationCategoryRepository;

  @Mock
  private StateTransitionManager<ActionState, ActionEvent> actionSvcStateTransitionManager;

  @Mock
  private AppConfig appConfig;

  @InjectMocks
  private FeedbackServiceImpl feedbackService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * Yep - another test
   */
  @Test
  public void testFeedbackAccepted() throws Exception {
    List<ActionFeedback> actionFeedbacks = FixtureHelper.loadClassFixtures(ActionFeedback[].class);
    List<Action> actions = FixtureHelper.loadClassFixtures(Action[].class);

    Mockito.when(actionRepo.getOne(BigInteger.valueOf(1))).thenReturn(actions.get(0));
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.PENDING, ActionEvent.REQUEST_ACCEPTED))
        .thenReturn(ActionState.ACTIVE);

    // Call method
    feedbackService.acceptFeedback(actionFeedbacks.get(0));

    // Verify calls made
    verify(actionRepo, times(1)).saveAndFlush(any(Action.class));
    verify(caseSvcClientService, times(0)).createNewCaseEvent(any(Action.class), any(CategoryDTO.CategoryType.class));
  }

  /**
   * Yep - another test
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testFeedbackAcceptedInvalidState() throws Exception {
    List<ActionFeedback> actionFeedbacks = FixtureHelper.loadClassFixtures(ActionFeedback[].class);
    List<Action> actions = FixtureHelper.loadClassFixtures(Action[].class);

    Mockito.when(actionRepo.getOne(BigInteger.valueOf(2))).thenReturn(actions.get(1));
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.SUBMITTED, ActionEvent.REQUEST_FAILED))
        .thenThrow(StateTransitionException.class);

    // Call method
    try {
      feedbackService.acceptFeedback(actionFeedbacks.get(1));
      fail();
    } catch (RuntimeException rte) {
      // Verify calls NOT made
      verify(actionRepo, times(0)).saveAndFlush(any(Action.class));
      verify(caseSvcClientService, times(0)).createNewCaseEvent(any(Action.class), any(CategoryDTO.CategoryType.class));
    }
  }

  /**
   * Yep - another test
   */
  @Test
  public void testFeedbackBlankSituationActionCompleted() throws Exception {
    List<ActionFeedback> actionFeedbacks = FixtureHelper.loadClassFixtures(ActionFeedback[].class);
    List<Action> actions = FixtureHelper.loadClassFixtures(Action[].class);
    List<SituationCategory> situationCats = FixtureHelper.loadClassFixtures(SituationCategory[].class);

    Mockito.when(actionRepo.getOne(BigInteger.valueOf(1))).thenReturn(actions.get(2));
    Mockito.when(situationCategoryRepository.findOne(actionFeedbacks.get(2).getSituation()))
        .thenReturn(situationCats.get(0));
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.ACTIVE, ActionEvent.REQUEST_COMPLETED))
        .thenReturn(ActionState.COMPLETED);

    // Call method
    feedbackService.acceptFeedback(actionFeedbacks.get(2));

    // Verify calls made
    verify(actionRepo, times(1)).saveAndFlush(any(Action.class));
    verify(caseSvcClientService, times(1)).createNewCaseEvent(actions.get(2), CategoryDTO.CategoryType.ACTION_COMPLETED);
  }
  
  /**
   * Yep - another test
   */
  @Test
  public void testFeedbackDerelictSituationActionCompleted() throws Exception {
    List<ActionFeedback> actionFeedbacks = FixtureHelper.loadClassFixtures(ActionFeedback[].class);
    List<Action> actions = FixtureHelper.loadClassFixtures(Action[].class);
    List<SituationCategory> situationCats = FixtureHelper.loadClassFixtures(SituationCategory[].class);

    Mockito.when(actionRepo.getOne(BigInteger.valueOf(1))).thenReturn(actions.get(2));
    Mockito.when(situationCategoryRepository.findOne(actionFeedbacks.get(3).getSituation()))
        .thenReturn(situationCats.get(0));
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.ACTIVE, ActionEvent.REQUEST_COMPLETED))
        .thenReturn(ActionState.COMPLETED);

    // Call method
    feedbackService.acceptFeedback(actionFeedbacks.get(3));

    // Verify calls made
    verify(actionRepo, times(1)).saveAndFlush(any(Action.class));
    verify(caseSvcClientService, times(1)).createNewCaseEvent(actions.get(2), CategoryDTO.CategoryType.ACTION_COMPLETED_DISABLED);
  }
  
  /**
   * Yep - another test
   */
  @Test
  public void testFeedbackConfusedSituationActionCompleted() throws Exception {
    List<ActionFeedback> actionFeedbacks = FixtureHelper.loadClassFixtures(ActionFeedback[].class);
    List<Action> actions = FixtureHelper.loadClassFixtures(Action[].class);
    List<SituationCategory> situationCats = FixtureHelper.loadClassFixtures(SituationCategory[].class);

    Mockito.when(actionRepo.getOne(BigInteger.valueOf(1))).thenReturn(actions.get(2));
    Mockito.when(situationCategoryRepository.findOne(actionFeedbacks.get(4).getSituation()))
        .thenReturn(situationCats.get(1));
    Mockito.when(actionSvcStateTransitionManager.transition(ActionState.ACTIVE, ActionEvent.REQUEST_COMPLETED))
        .thenReturn(ActionState.COMPLETED);

    // Call method
    feedbackService.acceptFeedback(actionFeedbacks.get(4));

    // Verify calls made
    verify(actionRepo, times(1)).saveAndFlush(any(Action.class));
    verify(caseSvcClientService, times(1)).createNewCaseEvent(actions.get(2), CategoryDTO.CategoryType.ACTION_COMPLETED_DEACTIVATED);
  }
}

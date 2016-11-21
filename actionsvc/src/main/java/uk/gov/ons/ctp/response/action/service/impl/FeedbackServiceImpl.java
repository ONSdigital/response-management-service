package uk.gov.ons.ctp.response.action.service.impl;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.SituationCategory;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.domain.repository.SituationCategoryRepository;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.CaseSvcClientService;
import uk.gov.ons.ctp.response.action.service.FeedbackService;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;

/**
 * Accept feedback from handlers
 */
@Slf4j
@Named
public class FeedbackServiceImpl implements FeedbackService {

  private static final BigInteger CSV_GENERATED_ID_BOUNDARY = BigInteger.valueOf(1_000_000_000L);

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private CaseSvcClientService caseSvcClientService;

  @Inject
  private ActionRepository actionRepo;

  @Inject
  private SituationCategoryRepository situationCategoryRepository;

  @Inject
  private StateTransitionManager<ActionState, ActionDTO.ActionEvent> actionSvcStateTransitionManager;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public void acceptFeedback(ActionFeedback feedback) {
    BigInteger actionId = feedback.getActionId();

    if (actionId.compareTo(CSV_GENERATED_ID_BOUNDARY) == -1) {
      Action action = actionRepo.getOne(actionId);
      ActionDTO.ActionEvent outcomeEvent = ActionDTO.ActionEvent.valueOf(feedback.getOutcome().name());

      if (outcomeEvent != null) {
        ActionDTO.ActionState nextState = actionSvcStateTransitionManager.transition(
            action.getState(),
            outcomeEvent);
        String situation = feedback.getSituation();

        if (nextState.equals(ActionDTO.ActionState.COMPLETED)) {
          CategoryDTO.CategoryType category = CategoryDTO.CategoryType.ACTION_COMPLETED;
          if (!StringUtils.isBlank(feedback.getSituation())) {
            SituationCategory situationCategory = situationCategoryRepository.findOne(feedback.getSituation());
            if (situationCategory != null) {
              category = CategoryDTO.CategoryType.valueOf(situationCategory.getEventCategory());
              caseSvcClientService.createNewCaseEvent(action, category);
              updateAction(action, nextState, situation);
            } else {
              log.error("Feedback Service unable to decipher the situation {} from feedback - ignoring this feedback",
                  feedback.getSituation());
            }
          } else {
            caseSvcClientService.createNewCaseEvent(action, category);
            updateAction(action, nextState, situation);
          }
        } else {
          updateAction(action, nextState, situation);
        }
      } else {
        log.error("Feedback Service unable to decipher the outcome {} from feedback - ignoring this feedback",
            feedback.getOutcome());
      }
    }

  }

  /**
   * Update the action
   * @param action the action to update
   * @param nextState the state to transition to
   * @param situation the situation provided by the feedback
   */
  private void updateAction(Action action, ActionDTO.ActionState nextState, String situation) {
    action.setSituation(situation);
    action.setState(nextState);
    action.setUpdatedDateTime(DateTimeUtil.nowUTC());
    actionRepo.saveAndFlush(action);

  }

}

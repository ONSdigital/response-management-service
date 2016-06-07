package uk.gov.ons.ctp.response.action.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.state.StateTransitionException;
import uk.gov.ons.ctp.common.state.StateTransitionManager;
import uk.gov.ons.ctp.common.time.DateTimeUtil;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionDTO.ActionState;
import uk.gov.ons.ctp.response.action.service.CaseFrameSvcClientService;
import uk.gov.ons.ctp.response.action.service.FeedbackService;
import uk.gov.ons.ctp.response.caseframe.representation.CategoryDTO;

/**
 * Accept feedback from handlers
 */
@Slf4j
@Named
public class FeedbackServiceImpl implements FeedbackService {

  private static final BigInteger CSV_GENERATED_ID_BOUNDARY = BigInteger.valueOf(1_000_000_000L);

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private CaseFrameSvcClientService caseFrameSvcClientService;

  @Inject
  private ActionRepository actionRepo;

  @Inject
  private StateTransitionManager<ActionState, ActionDTO.ActionEvent> actionSvcStateTransitionManager;

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public void acceptFeedback(ActionFeedback feedback) {
    BigInteger actionId = feedback.getActionId();

    // TODO won't need this check when instruction xsd has responseRequired bool
    // here now so that for 2016 we could use CSV ingest for all handlers if necessary
    if (actionId.compareTo(CSV_GENERATED_ID_BOUNDARY) == -1) {
      Action action = actionRepo.getOne(actionId.intValue());
      ActionDTO.ActionEvent outcomeEvent = ActionDTO.ActionEvent.valueOf(feedback.getOutcome().name());

      if (outcomeEvent != null) {
        try {
          ActionDTO.ActionState nextState = actionSvcStateTransitionManager.transition(
              action.getState(),
              outcomeEvent);
          action.setSituation(feedback.getSituation());
          action.setState(nextState);
          action.setUpdatedDateTime(DateTimeUtil.nowUTC());
          actionRepo.saveAndFlush(action);

          if (nextState.equals(ActionDTO.ActionState.COMPLETED)) {
            caseFrameSvcClientService.createNewCaseEvent(action, CategoryDTO.CategoryName.ACTION_COMPLETED);
          }
        } catch (StateTransitionException ste) {
          throw new RuntimeException(ste);
        }
      } else {
        log.error("Feedback Service unable to decipher the outcome {} from feedback - ignoring this feedback",
            feedback.getOutcome());
      }
    }

  }

}

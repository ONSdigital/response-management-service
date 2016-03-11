package uk.gov.ons.ctp.response.action.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;

/**
 * Implementation
 */
@Named
@Slf4j
public class ActionPlanServiceImpl implements ActionPlanService {

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private ActionPlanRepository actionPlanRepo;

  /**
   * Implementation
   * @return
   */
  @Override
  public final List<ActionPlan> findActionPlans() {
    log.debug("Entering findActionPlans");
    return actionPlanRepo.findAll();
  }

  /**
   * Implementation
   * @param actionPlanId This is the action plan id
   * @return
   */
  @Override
  public final ActionPlan findActionPlan(final Integer actionPlanId) {
    log.debug("Entering findActionPlan with {}", actionPlanId);
    return actionPlanRepo.findOne(actionPlanId);
  }

  /**
   * Implementation
   * @param actionPlanId This is the action plan id of the action plan to be updated
   * @param actionPlanDTO This is the action plan dto containing the potentially new description and lastGoodRunDatetime
   * @return
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  public final ActionPlan updateActionPlan(final Integer actionPlanId, final ActionPlanDTO actionPlanDTO) {
    log.debug("Entering updateActionPlan with {}", actionPlanId);
    ActionPlan actionPlan = actionPlanRepo.findOne(actionPlanId);
    if (actionPlan != null) {
      boolean needsUpdate = false;

      String newDescription = actionPlanDTO.getDescription();
      log.debug("newDescription = {}", newDescription);
      if (newDescription != null) {
        needsUpdate = true;
        actionPlan.setDescription(newDescription);
      }

      Date newLastGoodRunDatetime = actionPlanDTO.getLastGoodRunDatetime();
      log.debug("newLastGoodRunDatetime = {}", newLastGoodRunDatetime);
      if (newLastGoodRunDatetime != null) {
        needsUpdate = true;
        actionPlan.setLastGoodRunDatetime(new Timestamp(newLastGoodRunDatetime.getTime()));
      }

      if (needsUpdate) {
        log.debug("about to update the action plan with id {}", actionPlanId);
        actionPlan = actionPlanRepo.save(actionPlan);
      }
    }
    return actionPlan;
  }

}

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
import uk.gov.ons.ctp.response.action.domain.model.ActionRule;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRuleRepository;
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

  @Inject
  private ActionRuleRepository actionRuleRepository;

  @Override
  public List<ActionPlan> findActionPlans() {
    log.debug("Entering findActionPlans");
    return actionPlanRepo.findAll();
  }

  @Override
  public ActionPlan findActionPlan(final Integer actionPlanId) {
    log.debug("Entering findActionPlan with {}", actionPlanId);
    return actionPlanRepo.findOne(actionPlanId);
  }


  @Override
  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  public ActionPlan updateActionPlan(final Integer actionPlanId, final ActionPlan actionPlan) {
    log.debug("Entering updateActionPlan with {}", actionPlanId);
    ActionPlan existingActionPlan = actionPlanRepo.findOne(actionPlanId);
    if (existingActionPlan != null) {
      boolean needsUpdate = false;

      String newDescription = actionPlan.getDescription();
      log.debug("newDescription = {}", newDescription);
      if (newDescription != null) {
        needsUpdate = true;
        existingActionPlan.setDescription(newDescription);
      }

      Date newLastRunDateTime = actionPlan.getLastRunDateTime();
      log.debug("newLastRunDatetime = {}", newLastRunDateTime);
      if (newLastRunDateTime != null) {
        needsUpdate = true;
        existingActionPlan.setLastRunDateTime(new Timestamp(newLastRunDateTime.getTime()));
      }

      if (needsUpdate) {
        log.debug("about to update the action plan with id {}", actionPlanId);
        existingActionPlan = actionPlanRepo.save(existingActionPlan);
      }
    }
    return existingActionPlan;
  }

  @Override
  public List<ActionRule> findActionRulesForActionPlan(final Integer actionPlanId) {
    return actionRuleRepository.findByActionPlanId(actionPlanId);
  }

}

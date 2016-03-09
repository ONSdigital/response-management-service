package uk.gov.ons.ctp.response.action.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;

/**
 * An implementation of the AddressService using JPA Repository class(es)
 * The business logic for the application should reside here.
 */
@Named
@Slf4j
public class ActionPlanServiceImpl implements ActionPlanService {

  @Inject
  private ActionPlanRepository actionPlanRepo;

  @Override
  public final List<ActionPlan> findActionPlans() {
    log.debug("Entering findActionPlans");
    return actionPlanRepo.findAll();
  }

  @Override
  public final ActionPlan findActionPlan(final Integer actionPlanId) {
    log.debug("Entering findActionPlan with {}", actionPlanId);
    return actionPlanRepo.findOne(actionPlanId);
  }

}

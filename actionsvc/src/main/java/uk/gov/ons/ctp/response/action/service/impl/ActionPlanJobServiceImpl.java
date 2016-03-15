package uk.gov.ons.ctp.response.action.service.impl;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Implementation
 */
@Named
@Slf4j
public class ActionPlanJobServiceImpl implements ActionPlanJobService {

  private static final int TRANSACTION_TIMEOUT = 30;

  @Inject
  private ActionPlanJobRepository actionPlanJobRepo;

  @Override public ActionPlanJob findActionPlanJob(Integer actionPlanJobId) {
    log.debug("Entering findActionPlanJob with {}", actionPlanJobId);
    return actionPlanJobRepo.findOne(actionPlanJobId);
  }
}

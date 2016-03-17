package uk.gov.ons.ctp.response.action.service.impl;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanJobRepository;
import uk.gov.ons.ctp.response.action.domain.repository.ActionPlanRepository;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation
 */
@Named
@Slf4j
public class ActionPlanJobServiceImpl implements ActionPlanJobService {

  private static final int TRANSACTION_TIMEOUT = 30;
  private static final String SUBMITTED = "SUBMITTED";

  @Inject
  private ActionPlanRepository actionPlanRepo;

  @Inject
  private ActionPlanJobRepository actionPlanJobRepo;

  @Inject
  private MapperFacade mapperFacade;

  @Override
  public final ActionPlanJob findActionPlanJob(final Integer actionPlanJobId) {
    log.debug("Entering findActionPlanJob with {}", actionPlanJobId);
    return actionPlanJobRepo.findOne(actionPlanJobId);
  }

  @Override
  public final List<ActionPlanJob> findActionPlanJobsForActionPlan(final Integer actionPlanId) {
    return actionPlanJobRepo.findByActionPlanId(actionPlanId);
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false, timeout = TRANSACTION_TIMEOUT)
  @Override
  public final ActionPlanJob executeActionPlan(final Integer actionPlanId, final ActionPlanJob actionPlanJob) {
    ActionPlan actionPlan = actionPlanRepo.findOne(actionPlanId);
    if (actionPlan != null) {
      actionPlanJob.setActionPlanId(actionPlanId);
      actionPlanJob.setState(SUBMITTED);
      java.util.Date nowDate = new java.util.Date();
      Timestamp now = new Timestamp(nowDate.getTime());
      actionPlanJob.setCreatedDatetime(now);
      actionPlanJob.setUpdatedDateTime(now);
      return actionPlanJobRepo.save(actionPlanJob);
    }
    return null;
  }
}

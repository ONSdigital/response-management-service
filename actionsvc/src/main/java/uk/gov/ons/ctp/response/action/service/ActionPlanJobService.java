package uk.gov.ons.ctp.response.action.service;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;

/**
 * The service for ActionPlanJobs
 */
public interface ActionPlanJobService extends CTPService {
  /**
   * This method returns the action plan job for the specified action plan job id.
   * @param actionPlanJobId This is the action plan job id
   * @return ActionPlanJob This returns the associated action plan job.
   */
  ActionPlanJob findActionPlanJob(Integer actionPlanJobId);
}

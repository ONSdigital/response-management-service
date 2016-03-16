package uk.gov.ons.ctp.response.action.service;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;

import java.util.List;

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

  /**
   * Returns all action plan jobs for the given action plan id.
   * @param actionPlanId This is the action plan id
   * @return Returns all action plan jobs for the given action plan id.
   */
  List<ActionPlanJob> findActionPlanJobsForActionPlan(Integer actionPlanId);

  /**
   * Create an action plan job given an action plan id and an actionPlanJobDTO
   * @param actionPlanId This is the action plan id
   * @param actionPlanJobDTO This is the actionPlanJobDTO for the action plan job to be created
   * @return ActionPlanJob This returns the newly created action plan job.
   */
  ActionPlanJob executeActionPlan(Integer actionPlanId, ActionPlanJobDTO actionPlanJobDTO);
}

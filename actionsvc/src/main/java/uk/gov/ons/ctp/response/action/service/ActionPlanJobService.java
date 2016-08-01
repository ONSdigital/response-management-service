package uk.gov.ons.ctp.response.action.service;

import java.util.List;
import java.util.Optional;

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
  Optional<ActionPlanJob> findActionPlanJob(Integer actionPlanJobId);

  /**
   * Returns all action plan jobs for the given action plan id.
   * @param actionPlanId This is the action plan id
   * @return Returns all action plan jobs for the given action plan id.
   */
  List<ActionPlanJob> findActionPlanJobsForActionPlan(Integer actionPlanId);

  /**
   * Will be called by the endpoint when a manual execution of an action plan is requested
   * Create an action plan job and execute it
   * @param actionPlanJob This is the actionPlanJob for the action plan job to be created
   * @return ActionPlanJob This returns the newly created action plan job.
   */
  Optional<ActionPlanJob> createAndExecuteActionPlanJob(ActionPlanJob actionPlanJob);

  /**
   * Will be called by the endpoint when a manual execution of an action plan is requested
   * @return list of action plan jobs.
   */
  List<ActionPlanJob>createAndExecuteAllActionPlanJobs();

}

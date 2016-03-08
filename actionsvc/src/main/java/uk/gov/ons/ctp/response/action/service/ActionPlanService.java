package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;

/**
 * Created by Martin.Humphrey on 17/2/2016.
 */
public interface ActionPlanService extends CTPService {

  /**
   * This method returns all action plans.
   * @return List<ActionPlan> This returns all action plans.
   */
  List<ActionPlan> findActionPlans();

  /**
   * This method returns the action plan for the specified action plan id.
   * @param actionPlanId This is the action plan id
   * @return ActionPlan This returns the associated action plan.
   */
  ActionPlan findActionPlan(Integer actionPlanId);


}
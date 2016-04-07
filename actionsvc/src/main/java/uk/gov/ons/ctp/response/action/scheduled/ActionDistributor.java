package uk.gov.ons.ctp.response.action.scheduled;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionState;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;
import uk.gov.ons.ctp.response.action.service.ActionService;

@Named
@Slf4j
public class ActionDistributor {

  @Inject
  private ActionService actionService;
  
  @Scheduled(fixedRate=5000)
  public void work() {
      log.debug("ActionDistributor is in the house!");
      
      List<ActionType> actionTypes = actionService.findActionTypes();
      for (ActionType actionType:actionTypes) {
        List<Action> actions = actionService.findActionsForDistribution(actionType.getName(), ActionState.SUBMITTED);
        ActionInstruction actionInstruction = new ActionInstruction();
        ActionRequests actionRequests = new ActionRequests();
        actionInstruction.setActionRequests(actionRequests);

        for (Action action:actions) {
          ActionRequest actionRequest = new ActionRequest();
          actionRequest.setActionId(BigInteger.valueOf(action.getActionId()));
          actionRequest.setActionType(actionType.getName());
          actionRequest.setCaseId(BigInteger.valueOf(action.getCaseId()));
          actionRequest.setContactName("TODO"); // TODO
          actionRequest.setEvents(null); //TODO
          actionRequest.setIac(""); //TODO
          actionRequest.setPriority(Priority.fromValue(ActionPriority.valueOf(action.getPriority()).getName()));
//          actionRequest.setQuestionnaireId(action.);
        }
        
      }
  }
  
}

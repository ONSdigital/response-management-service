package uk.gov.ons.ctp.response.action.message;

import javax.inject.Named;

import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.handler.annotation.Header;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;

@Named
public class InstructionService {

  @Publisher(channel="instruction.outbound")
  public ActionInstruction sendRequest(@Header("HANDLER") String handler, String actionType) {
    ActionInstruction instruction = new ActionInstruction();
    ActionRequest request = new ActionRequest();
    
    request.setActionType(actionType);
    ActionRequests requests = new ActionRequests();
    requests.getActionRequests().add(request);
    instruction.setActionRequests(requests);
    return instruction;
  }
  
}

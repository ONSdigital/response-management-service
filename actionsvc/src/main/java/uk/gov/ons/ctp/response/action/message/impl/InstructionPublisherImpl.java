package uk.gov.ons.ctp.response.action.message.impl;

import java.util.List;

import javax.inject.Named;

import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.handler.annotation.Header;

import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;

@Named
public class InstructionPublisherImpl implements InstructionPublisher {

  @Override
  @Publisher(channel="instructionOutbound")
  public ActionInstruction sendRequests(@Header("HANDLER") String handler, List<ActionRequest> actionRequests) {
    ActionInstruction instruction = new ActionInstruction();
    
    ActionRequests requests = new ActionRequests();
    requests.getActionRequests().addAll(actionRequests);
    instruction.setActionRequests(requests);
    return instruction;
  }
  
}

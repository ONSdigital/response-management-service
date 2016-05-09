package uk.gov.ons.ctp.response.action.message.impl;

import java.util.List;

import javax.inject.Named;

import org.springframework.integration.annotation.Publisher;
import org.springframework.messaging.handler.annotation.Header;

import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancels;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequests;

/**
 * This class is used to publish a list of action request objects to the
 * downstream handlers The sendRequests method is called by the
 * ActionDistibutor, and on return the ActionInstruction object constructed is
 * sent to the instructionOutbound SpringIntegration channel (see the
 * integration-context.xml for details of the channel and the outbound flow)
 *
 */
@Named
public class InstructionPublisherImpl implements InstructionPublisher {


  @Override
  @Publisher(channel = "instructionOutbound")
  public ActionInstruction sendInstructions(@Header("HANDLER") String handler, List<ActionRequest> actionRequests, List<ActionCancel> actionCancels) {
    ActionInstruction instruction = new ActionInstruction();

    ActionRequests requests = new ActionRequests();
    requests.getActionRequests().addAll(actionRequests);
    instruction.setActionRequests(requests);

    ActionCancels cancels = new ActionCancels();
    cancels.getActionCancels().addAll(actionCancels);
    instruction.setActionCancels(cancels);

    return instruction;
  }

}

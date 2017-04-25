package uk.gov.ons.ctp.response.action.message.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.MessageEndpoint;
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
@MessageEndpoint
public class InstructionPublisherImpl implements InstructionPublisher {

  @Qualifier("actionInstructionRabbitTemplate")
  @Autowired
  private RabbitTemplate rabbitTemplate;

  private static final String ACTION = "Action.";
  private static final String BINDING = ".binding";

  public void sendInstructions(@Header("HANDLER") String handler, List<ActionRequest> actionRequests,
      List<ActionCancel> actionCancels) {
    ActionInstruction instruction = new ActionInstruction();

    if (actionRequests != null) {
      ActionRequests requests = new ActionRequests();
      requests.getActionRequests().addAll(actionRequests);
      instruction.setActionRequests(requests);
    }

    if (actionCancels != null) {
      ActionCancels cancels = new ActionCancels();
      cancels.getActionCancels().addAll(actionCancels);
      instruction.setActionCancels(cancels);
    }

    String routingKey = String.format("%s%s%s", ACTION, handler, BINDING);
    rabbitTemplate.convertAndSend(routingKey, instruction);
  }
}

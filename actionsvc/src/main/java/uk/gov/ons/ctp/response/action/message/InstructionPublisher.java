package uk.gov.ons.ctp.response.action.message;

import java.util.List;

import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

/**
 * Interface for the publishing of ActionRequests to downstream handlers
 * @author centos
 *
 */
public interface InstructionPublisher {
  /**
   * The implementation will be responsible for publishing ActionRequests to the
   * SpringIntegration outbound flow
   * 
   * @param handler the handler that the outbound flow should send to - taken
   *          directly from the Actions ActionType
   * @param actionRequests the requests to publish
   * @return the outbound representation of the instruction generated from the
   *         xsd - contains the action requests, and will be serialized into xml
   *         by jaxb
   */
  ActionInstruction sendInstructions(String handler, List<ActionRequest> actionRequests, List<ActionCancel> actionCancels);
}
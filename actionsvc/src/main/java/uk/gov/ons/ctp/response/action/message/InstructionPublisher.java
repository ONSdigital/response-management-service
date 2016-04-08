package uk.gov.ons.ctp.response.action.message;

import java.util.List;

import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

public interface InstructionPublisher {
  ActionInstruction sendRequests(String handler, List<ActionRequest> actionRequests);
}
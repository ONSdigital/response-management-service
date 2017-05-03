package uk.gov.ons.ctp.response.action.export.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import uk.gov.ons.ctp.response.action.export.message.ActionExportReceiver;
import uk.gov.ons.ctp.response.action.export.service.ActionExportService;
import uk.gov.ons.ctp.response.action.message.instruction.ActionInstruction;

/**
 * Service implementation responsible for receipt of action export instructions.
 * See Spring Integration flow for details of InstructionTransformed inbound
 * queue.
 *
 */
@MessageEndpoint
public class ActionExportReceiverImpl implements ActionExportReceiver {

  @Autowired
  private ActionExportService actionExportService;

  @Override
  @ServiceActivator(inputChannel = "actionInstructionTransformed", adviceChain = "actionInstructionRetryAdvice")
  public void acceptInstruction(ActionInstruction instruction) {
    actionExportService.acceptInstruction(instruction);
  }
}

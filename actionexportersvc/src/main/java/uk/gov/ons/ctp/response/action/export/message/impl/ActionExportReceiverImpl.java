package uk.gov.ons.ctp.response.action.export.message.impl;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.oxm.Marshaller;

import uk.gov.ons.ctp.common.util.DeadLetterLogCommand;
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

  @Inject
  @Qualifier("instructionUnmarshaller")
  Marshaller marshaller;
  
  @Inject
  private ActionExportService actionExportService;

  @Override
  @ServiceActivator(inputChannel = "instructionTransformed")
  public void acceptInstruction(ActionInstruction instruction) {
    DeadLetterLogCommand<ActionInstruction> command = new DeadLetterLogCommand<ActionInstruction>(marshaller, instruction);
    command.run((ActionInstruction x)->actionExportService.acceptInstruction(x));
  }
}

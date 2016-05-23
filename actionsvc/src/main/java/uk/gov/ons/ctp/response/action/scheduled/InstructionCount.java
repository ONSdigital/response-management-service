package uk.gov.ons.ctp.response.action.scheduled;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.ons.ctp.response.action.scheduled.DistributionInfo.Instruction;

@Data
@AllArgsConstructor
public class InstructionCount {
  private String actionTypeName;
  private Instruction instruction;
  private Integer count;
}

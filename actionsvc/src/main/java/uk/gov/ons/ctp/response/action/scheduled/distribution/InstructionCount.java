package uk.gov.ons.ctp.response.action.scheduled.distribution;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.gov.ons.ctp.response.action.scheduled.distribution.DistributionInfo.Instruction;

/**
 * Simple pojo for health info re instructions of a type sent
 *
 */
@Data
@AllArgsConstructor
public class InstructionCount {
  private String actionTypeName;
  private Instruction instruction;
  private Integer count;
}

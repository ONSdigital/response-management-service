package uk.gov.ons.ctp.response.action.scheduled.distribution;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.response.action.ScheduledHealthInfo;

/**
 * info regarding the last action distribution to handlers
 *
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class DistributionInfo extends ScheduledHealthInfo {

  /**
   * the type of instruction
   */
  public enum Instruction {
    REQUEST, CANCEL_REQUEST
  };

  private List<InstructionCount> instructionCounts = new ArrayList<>();

}

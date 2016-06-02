package uk.gov.ons.ctp.response.action.scheduled;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * info regarding the last action distribution to handlers
 *
 */
@Data
public class DistributionInfo {

  private String lastRunTime;

  /**
   * the type of instruction
   */
  public enum Instruction {
    REQUEST, CANCEL_REQUEST
  };

  private List<InstructionCount> instructionCounts = new ArrayList<>();

  /**
   * Add the Instruction count to our list
   *
   * @param count the count of instructions to store
   */
  public void addInstructionCount(InstructionCount count) {
    instructionCounts.add(count);
  }

}

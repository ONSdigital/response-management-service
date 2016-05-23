package uk.gov.ons.ctp.response.action.scheduled;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DistributionInfo {

  private String lastRunTime;
  public enum Instruction {REQUEST, CANCEL_REQUEST};

  private List<InstructionCount> instructionCounts = new ArrayList<> ();

  public void addInstructionCount(InstructionCount count) {
    instructionCounts.add(count);
  }


}

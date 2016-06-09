package uk.gov.ons.ctp.response.action.scheduled.ingest;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import uk.gov.ons.ctp.response.action.config.ActionDistribution;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;

/**
 * Test the action distributor
 */
@RunWith(MockitoJUnitRunner.class)
public class CsvIngesterTest {

  private static String TEMP_FILE_PREFIX = "/tmp_";
  
  @Spy
  private AppConfig appConfig = new AppConfig();

  @Mock
  private InstructionPublisher instructionPublisher;

  @InjectMocks
  private CsvIngester csvIngester;

  /**
   * A Test
   */
  @Before
  public void setup() {
    ActionDistribution actionDistributionConfig = new ActionDistribution();
    actionDistributionConfig.setInstructionMax(1);
    appConfig.setActionDistribution(actionDistributionConfig);

    MockitoAnnotations.initMocks(this);
  }

  /**
   * take a named test file and create a copy of it - is because the ingester
   * will delete the source csv file after ingest
   *
   * @param fileName source file name
   * @return the newly created file
   * @throws Exception oops
   */
  private File getTestFile(String fileName) throws Exception {
    File srcFile = new File(getClass().getClassLoader().getResource("csv/"+fileName).toURI());
    File destFile = new File(srcFile.getParent() + TEMP_FILE_PREFIX + fileName);
    FileUtils.copyFile(srcFile, destFile);
    return destFile;
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testBlueSky() throws Exception {

    csvIngester.ingest(getTestFile("bluesky.csv"));

    verify(instructionPublisher, times(1)).sendInstructions(eq("Field"), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));
  }

  @Test
  public void testInvalidHandler() throws Exception {

    csvIngester.ingest(getTestFile("invalid-handler.csv"));

    verify(instructionPublisher, times(0)).sendInstructions(eq("Field"), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));
  }
}

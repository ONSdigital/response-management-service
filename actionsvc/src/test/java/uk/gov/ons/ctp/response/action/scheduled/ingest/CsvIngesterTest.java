package uk.gov.ons.ctp.response.action.scheduled.ingest;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;

import static org.junit.Assert.assertTrue;
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

  private static final String TEMP_FILE_PREFIX = "/tmp_";

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
    String callerMethodName = new Exception().getStackTrace()[1].getMethodName();
    File srcFile = new File(getClass().getClassLoader().getResource("csv/" + fileName).toURI());
    String destFileName = srcFile.getParent() + "/" + callerMethodName + "/" + fileName;
    File destFile = new File(destFileName);
    File destDir = new File(destFile.getParent());
    try {
      FileUtils.forceDelete(destDir);
    } catch (Exception e) {
      // might not already exist
    }
    FileUtils.copyFile(srcFile, destFile);
    return destFile;
  }

  /**
   * assert that the csv file was mutated into an error csv file with the expected
   * suffix identifying the line and column at fault
   *
   * @param testFile
   * @param errorSuffix
   */
  private void verifyErrorFileExists(File testFile, String errorSuffix) {
    File errorFile = new File(testFile + errorSuffix);
    assertTrue(errorFile.exists());
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testBlueSky() throws Exception {

    csvIngester.ingest(getTestFile("bluesky.csv"));

    verify(instructionPublisher, times(1)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testInvalidHandler() throws Exception {
    File testFile = getTestFile("invalid-handler.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_handler");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testInvalidActionType() throws Exception {
    File testFile = getTestFile("invalid-actionType.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_actionType");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testInvalidInstructionType() throws Exception {
    File testFile = getTestFile("invalid-instructionType.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_instructionType");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testAddressType() throws Exception {
    File testFile = getTestFile("invalid-addressType.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_addressType");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testEstabType() throws Exception {
    File testFile = getTestFile("invalid-estabType.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_estabType");
  }
}

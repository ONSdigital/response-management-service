package uk.gov.ons.ctp.response.action.scheduled.ingest;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
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

import org.springframework.cloud.sleuth.Tracer;
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

  @Spy
  private AppConfig appConfig = new AppConfig();

  @Mock
  private InstructionPublisher instructionPublisher;

  @Mock
  private Tracer tracer;

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
   * @param testFile the original test file
   * @param errorSuffix the expected suffix
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
  public void testActionType() throws Exception {
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
  public void testInstructionType() throws Exception {
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

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLocality() throws Exception {
    File testFile = getTestFile("invalid-locality.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_locality");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testOrganisationName() throws Exception {
    File testFile = getTestFile("invalid-organisationName.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_organisationName");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testCategory() throws Exception {
    File testFile = getTestFile("invalid-category.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_category");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLine1() throws Exception {
    File testFile = getTestFile("invalid-line1.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_line1");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLine2() throws Exception {
    File testFile = getTestFile("invalid-line2.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_line2");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testTownName() throws Exception {
    File testFile = getTestFile("invalid-townName.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_townName");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testPostcodeA() throws Exception {
    File testFile = getTestFile("invalid-postcode-a.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_postcode");
  }
  
  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testPostcodeB() throws Exception {
    File testFile = getTestFile("invalid-postcode-b.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_postcode");
  }
  
  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testPostcodeC() throws Exception {
    File testFile = getTestFile("invalid-postcode-c.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_postcode");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLatitude() throws Exception {
    File testFile = getTestFile("invalid-latitude.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_latitude");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLongitude() throws Exception {
    File testFile = getTestFile("invalid-longitude.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_longitude");
  }

   /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testUprn() throws Exception {
    File testFile = getTestFile("invalid-uprn.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_uprn");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testLadCode() throws Exception {
    File testFile = getTestFile("invalid-ladCode.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
            anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_ladCode");
  }

   /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testTitle() throws Exception {
    File testFile = getTestFile("invalid-title.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_title");
  }

   /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testForename() throws Exception {
    File testFile = getTestFile("invalid-forename.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_forename");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testSurname() throws Exception {
    File testFile = getTestFile("invalid-surname.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_surname");
  }
  
  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testEmail() throws Exception {
    File testFile = getTestFile("invalid-emailAddress.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_emailAddress");
  }
   
  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testTelephone() throws Exception {
    File testFile = getTestFile("invalid-telephoneNumber.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_telephoneNumber");
  }
  
  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testCaseId() throws Exception {
    File testFile = getTestFile("invalid-caseId.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_caseId");
  }


  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testCaseRef() throws Exception {
    File testFile = getTestFile("invalid-caseRef.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_caseRef");
  }


  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testIac() throws Exception {
    File testFile = getTestFile("invalid-iac.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_iac");
  }

  /**
   * Test ...
   *
   * @throws Exception oops
   */
  @Test
  public void testPriority() throws Exception {
    File testFile = getTestFile("invalid-priority.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
        anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_priority");
  }

  @Test
  public void testActionPlan() throws Exception {
    File testFile = getTestFile("invalid-actionPlan.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
            anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_actionPlan");
  }

  @Test
  public void testQuestionSet() throws Exception {
    File testFile = getTestFile("invalid-questionSet.csv");
    csvIngester.ingest(testFile);

    verify(instructionPublisher, times(0)).sendInstructions(anyString(), anyListOf(ActionRequest.class),
            anyListOf(ActionCancel.class));

    verifyErrorFileExists(testFile, ".error_LINE_2_COLUMN_questionSet");
  }

}

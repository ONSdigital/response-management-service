package uk.gov.ons.ctp.response.action.message.impl;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import com.google.common.collect.Lists;

import liquibase.util.csv.CSVReader;
import liquibase.util.csv.opencsv.bean.ColumnPositionMappingStrategy;
import liquibase.util.csv.opencsv.bean.CsvToBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.domain.model.Action.ActionPriority;
import uk.gov.ons.ctp.response.action.message.CsvIngester;
import uk.gov.ons.ctp.response.action.message.InstructionPublisher;
import uk.gov.ons.ctp.response.action.message.instruction.ActionCancel;
import uk.gov.ons.ctp.response.action.message.instruction.ActionRequest;
import uk.gov.ons.ctp.response.action.message.instruction.Priority;

/**
 * The ingester is configured from the spring integration xml. It is called, by
 * virtue of the @ServiceActivator reference, whenever a candidate CSV file is
 * found in the ingest folder. The CSV file is a superset of all of the fields
 * in the ActionRequest/ActionCancel/ActionUpdate(tbd) messages which can be
 * sent to handlers, as well as the handler name and the action type that the
 * handler will be sent. The CSV file may contain actions for multiple handlers,
 * and even multiple action types for each handler. The generation of the CSV
 * file is outside the remit of the action service.
 *
 */
@MessageEndpoint
@Slf4j
public class CsvIngesterImpl extends CsvToBean implements CsvIngester {

  private static final int LINE_NUM_MODULO = 100;

  private static final String CHANNEL = "csvIngest";

  private static final String REQUEST_INSTRUCTION = "Request";
  private static final String CANCEL_INSTRUCTION = "Cancel";

  private static final String DATE_FORMAT = "yyMMddHHmmssSSS";

  private static final String HANDLER = "handler";
  private static final String REASON = "Cancelled by Response Management CSV Ingest";

  private static final String ACTION_TYPE = "actionType";
  private static final String INSTRUCTION_TYPE = "instructionType";
  private static final String ADDRESS_TYPE = "addressType";
  private static final String ESTAB_TYPE = "estabType";
  private static final String LOCALITY = "locality";
  private static final String ORGANISATION_NAME = "organisationName";
  private static final String CATEGORY = "category";
  private static final String LINE1 = "line1";
  private static final String LINE2 = "line2";
  private static final String TOWN_NAME = "townName";
  private static final String POSTCODE = "postcode";
  private static final String LATITUDE = "latitude";
  private static final String LONGITUDE = "longitude";
  private static final String UPRN = "uprn";
  private static final String CONTACT_NAME = "contactName";
  private static final String CASE_ID = "caseId";
  private static final String QUESTIONNAIRE_ID = "questionnaireId";
  private static final String PRIORITY = "priority";
  private static final String IAC = "iac";
  private static final String EVENTS = "events";

  /**
   * Inner class to encapsulate the request and cancel data as they do not have
   * common parentage
   *
   * @author centos
   *
   */
  @Data
  private class InstructionBucket {
    private List<ActionRequest> actionRequests = new ArrayList<>();
    private List<ActionCancel> actionCancels = new ArrayList<>();
  }

  @Inject
  private AppConfig appConfig;

  @Inject
  private InstructionPublisher instructionPublisher;

  /**
   * Lazy create a resusable validator
   *
   * @return the cached validator
   */
  @Cacheable(cacheNames = "csvIngestValidator")
  private Validator getValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * uk.gov.ons.ctp.response.action.message.impl.CsvIngester#ingest(java.io.
   * File)
   */
  @Override
  @ServiceActivator(inputChannel = CHANNEL)
  public void ingest(File csvFile) {
    log.debug("INGESTED " + csvFile.toString());
    SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
    String nowStr = fmt.format(new Date());
    CSVReader reader = null;

    Map<String, InstructionBucket> handlerInstructionBuckets = new HashMap<>();

    try {
      ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
      strat.setType(CsvLine.class);
      String[] columns = new String[] {HANDLER, ACTION_TYPE, INSTRUCTION_TYPE, ADDRESS_TYPE, ESTAB_TYPE, LOCALITY,
          ORGANISATION_NAME, CATEGORY, LINE1, LINE2, TOWN_NAME, POSTCODE, LATITUDE, LONGITUDE, UPRN, CONTACT_NAME,
          CASE_ID, QUESTIONNAIRE_ID, PRIORITY, IAC, EVENTS };
      strat.setColumnMapping(columns);

      reader = new CSVReader(new FileReader(csvFile));
      String[] nextLine = null;
      int lineNum = 0;
      try {
        while ((nextLine = reader.readNext()) != null) {
          if (lineNum++ > 0) {
            CsvLine csvLine = (CsvLine) processLine(strat, nextLine);
            Set<ConstraintViolation<CsvLine>> violations = getValidator().validate(csvLine);
            if (violations.size() > 0) {
              reader.close();
              String fieldNames = violations.stream().map(v -> v.getPropertyPath().toString())
                  .collect(Collectors.joining("_"));
              log.error("Problem parsing {} due to {} - entire ingest aborted", Arrays.toString(nextLine), fieldNames);
              csvFile.renameTo(new File(csvFile.getPath() + ".error_LINE_" + lineNum + "_COLUMN_" + fieldNames));
              return;
            }
            // first - which handler is it for?
            String handler = csvLine.getHandler();

            // get the handlers bucket
            InstructionBucket handlerInstructionBucket = handlerInstructionBuckets.get(handler);
            if (handlerInstructionBucket == null) {
              handlerInstructionBucket = new InstructionBucket();
              handlerInstructionBuckets.put(handler, handlerInstructionBucket);
            }

            // parse the line
            if (csvLine.getInstructionType().equals(REQUEST_INSTRUCTION)) {
              ActionRequest request = ActionRequest.builder()
                  .withActionId(new BigInteger(nowStr + String.format("%03d", lineNum % LINE_NUM_MODULO)))
                  .withActionType(csvLine.getActionType())
                  .withAddress()
                  .withCategory(csvLine.getCategory())
                  .withEstabType(csvLine.getEstabType())
                  .withLatitude(new BigDecimal(csvLine.getLatitude()))
                  .withLongitude(new BigDecimal(csvLine.getLongitude()))
                  .withLine1(csvLine.getLine1())
                  .withLine2(csvLine.getLine2())
                  .withLocality(csvLine.getLocality())
                  .withOrganisationName(csvLine.getOrganisationName())
                  .withPostcode(csvLine.getPostcode())
                  .withTownName(csvLine.getTownName())
                  .withType(csvLine.getAddressType())
                  .end()
                  .withCaseId(new BigInteger(csvLine.getCaseId()))
                  .withContactName(csvLine.getContactName())
                  .withIac(csvLine.getIac())
                  .withPriority(
                      Priority.fromValue(ActionPriority.valueOf(Integer.parseInt(csvLine.getPriority())).getName()))
                  .withQuestionnaireId(new BigInteger(csvLine.getQuestionnaireId()))
                  .withUprn(new BigInteger(csvLine.getUprn()))
                  .withEvents()
                  .withEvents(csvLine.getEvents().split("\\|"))
                  .end()
                  .build();

              // store the request in the handlers bucket
              handlerInstructionBucket.getActionRequests().add(request);

            } else if (csvLine.getInstructionType().equals(CANCEL_INSTRUCTION)) {
              ActionCancel cancel = ActionCancel.builder()
                  .withActionId(new BigInteger(nowStr + lineNum))
                  .withReason(REASON)
                  .build();
              // store the cancel in the handlers bucket
              handlerInstructionBucket.getActionCancels().add(cancel);
            }
          }
        }
      } catch (Exception e) {
        log.error("Problem parsing {} - entire ingest aborted : {}", nextLine, e);
        reader.close();
        csvFile.renameTo(new File(csvFile.getPath() + ".fix_line_" + lineNum));
        return;
      }

      reader.close();
      csvFile.delete();

      // all lines parsed successfully - now send out bucket contents
      publishBuckets(handlerInstructionBuckets);

    } catch (Exception e) {
      log.error("Problem reading ingest file {} because : ", csvFile.getPath(), e);
    }
  }

  /**
   * takes the map of buckets for all handlers, and splits the diff action
   * instructions into messages of the configured max size
   *
   * @param buckets the map of buckets keyed by handler
   */
  private void publishBuckets(Map<String, InstructionBucket> buckets) {
    buckets.forEach((handler, handlerInstructionBucket) -> {
      for (List<ActionRequest> partition : Lists.partition(handlerInstructionBucket.actionRequests,
          appConfig.getActionDistribution().getInstructionMax())) {
        instructionPublisher.sendInstructions(handler, partition, null);
      }
      for (List<ActionCancel> partition : Lists.partition(handlerInstructionBucket.actionCancels,
          appConfig.getActionDistribution().getInstructionMax())) {
        instructionPublisher.sendInstructions(handler, null, partition);
      }
    });
  }
}

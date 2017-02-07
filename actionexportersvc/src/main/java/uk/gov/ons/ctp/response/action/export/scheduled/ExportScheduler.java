package uk.gov.ons.ctp.response.action.export.scheduled;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.distributed.DistributedInstanceManager;
import uk.gov.ons.ctp.common.distributed.DistributedLatchManager;
import uk.gov.ons.ctp.common.distributed.DistributedLockManager;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.ExportMessage;
import uk.gov.ons.ctp.response.action.export.message.SftpServicePublisher;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;
import uk.gov.ons.ctp.response.action.export.service.ExportReportService;
import uk.gov.ons.ctp.response.action.export.service.TemplateMappingService;
import uk.gov.ons.ctp.response.action.export.service.TransformationService;

/**
 * This class will be responsible for the scheduling of export actions
 *
 */
@Named
@Slf4j
public class ExportScheduler implements HealthIndicator {

  private static final String DATE_FORMAT_IN_FILE_NAMES = "ddMMyyyy_HHmm";
  private static final String DISTRIBUTED_OBJECT_KEY_FILE_LATCH = "filelatch";
  private static final String DISTRIBUTED_OBJECT_KEY_REPORT_LATCH = "reportlatch";
  private static final String DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT = "scheduler";
  private static final String DISTRIBUTED_OBJECT_KEY_REPORT = "report";

  @Inject
  private TransformationService transformationService;

  @Inject
  private TemplateMappingService templateMappingService;

  @Inject
  private SftpServicePublisher sftpService;

  @Inject
  private ActionRequestService actionRequestService;

  @Inject
  private DistributedLockManager actionExportLockManager;

  @Inject
  private DistributedInstanceManager actionExportInstanceManager;

  @Inject
  private DistributedLatchManager actionExportLatchManager;

  @Inject
  private ExportReportService exportReportService;

  @Inject
  private ExportInfo exportInfo;

  @Override
  public Health health() {
    return Health.up()
        .withDetail("exportInfo", exportInfo)
        .build();
  }

  /**
   * Initialise export scheduler
   *
   */
  @PostConstruct
  public void init() {
    actionExportInstanceManager.incrementInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT);
    log.info("{} {} instance/s running",
        actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT),
        DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT);
  }

  /**
   * Clean up scheduler on bean destruction
   *
   */
  @PreDestroy
  public void cleanUp() {
    actionExportInstanceManager.decrementInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT);
    // Make sure no locks if interrupted in middle of run
    actionExportLockManager.unlockInstanceLocks();
    log.info("{} {} instance/s running",
        actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT),
        DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT);
  }

  /**
   * Carry out scheduled actions according to configured cron expression
   *
   */
  @Scheduled(cron = "#{appConfig.exportSchedule.cronExpression}")
  public void scheduleExport() {

    log.info("Scheduled run start");

    // Warn if Mapping document cannot deal with all ActionRequests stored
    List<String> storedActionTypes = actionRequestService.retrieveActionTypes();
    List<String> mappedActionTypes = templateMappingService.retrieveActionTypes();
    storedActionTypes.forEach((actionType) -> {
      if (!mappedActionTypes.contains(actionType)) {
        log.warn("No mapping for actionType : {}.", actionType);
      }
    });

    // Process templateMappings by file, have to as may be many actionTypes in
    // one file. Does not assume actionTypes in the same file use the same
    // template even so.
    String timeStamp = new SimpleDateFormat(DATE_FORMAT_IN_FILE_NAMES).format(Calendar.getInstance().getTime());
    actionExportLatchManager.setCountDownLatch(DISTRIBUTED_OBJECT_KEY_FILE_LATCH,
        actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT));
    templateMappingService.retrieveAllTemplateMappingsByFilename()
        .forEach((fileName, templatemappings) -> {
          log.info("Lock test {} {}", fileName, actionExportLockManager.isLocked(fileName));
          if (!actionExportLockManager.isLocked(fileName)) {
            if (actionExportLockManager.lock(fileName)) {
              log.info("Lock file {} {}", fileName, actionExportLockManager.isLocked(fileName));
              ExportMessage message = new ExportMessage();
              // process Collection of templateMappings
              templatemappings.forEach((templateMapping) -> {
                List<ActionRequestInstruction> requests = actionRequestService
                    .findByDateSentIsNullAndActionType(templateMapping.getActionType());
                if (requests.isEmpty()) {
                  log.info("No requests for actionType {} to process", templateMapping.getActionType());
                } else {
                  try {
                    transformationService.processActionRequests(message, requests);
                  } catch (CTPException e) {
                    // Error retrieving TemplateMapping in
                    // transformationService
                    log.error("Scheduled run error transforming ActionRequests");
                  }
                }
              });
              if (!message.isEmpty()) {
                sftpService.sendMessage(fileName + "_" + timeStamp + ".csv", message.getMergedActionRequestIds(),
                    message.getMergedOutputStreams());
              }
            }
          }
        });
    // Wait for all instances to finish to synchronise the removal of locks
    try {
      actionExportLatchManager.countDown(DISTRIBUTED_OBJECT_KEY_FILE_LATCH);
      if (!actionExportLatchManager.awaitCountDownLatch(DISTRIBUTED_OBJECT_KEY_FILE_LATCH)) {
        log.error("Scheduled run error countdownlatch timed out, should be {} instances running",
            actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT));
      }
    } catch (InterruptedException e) {
      log.error("Scheduled run error waiting for countdownlatch: {}", e.getMessage());
    } finally {
      actionExportLockManager.unlockInstanceLocks();
      actionExportLatchManager.deleteCountDownLatch(DISTRIBUTED_OBJECT_KEY_FILE_LATCH);
      if (!createReport()) {
        log.error("Scheduled run error creating report");
      }
      log.info("{} {} instance/s running",
          actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT),
          DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT);
    }
  }

  /**
   * Create an entry for the report service for exports created. Have to wait
   * for all potential service instances to finish the scheduled run prior to
   * calling this method to produce a report. Also have to synchronise all
   * potential services creating report to ensure only one report created per
   * scheduled run so need a latch for this purpose.
   */
  private boolean createReport() {
    boolean result = false;
    actionExportLatchManager.setCountDownLatch(DISTRIBUTED_OBJECT_KEY_REPORT_LATCH,
        actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT));
    if (!actionExportLockManager.isLocked(DISTRIBUTED_OBJECT_KEY_REPORT)) {
      if (actionExportLockManager.lock(DISTRIBUTED_OBJECT_KEY_REPORT)) {
        result = exportReportService.createReport();
      } else {
        result = true;
      }
    } else {
      result = true;
    }
    try {
      actionExportLatchManager.countDown(DISTRIBUTED_OBJECT_KEY_REPORT_LATCH);
      if (!actionExportLatchManager.awaitCountDownLatch(DISTRIBUTED_OBJECT_KEY_REPORT_LATCH)) {
        log.error("Report run error countdownlatch timed out, should be {} instances running",
            actionExportInstanceManager.getInstanceCount(DISTRIBUTED_OBJECT_KEY_INSTANCE_COUNT));
      }
    } catch (InterruptedException e) {
      log.error("Report run error waiting for countdownlatch: {}", e.getMessage());
    } finally {
      actionExportLockManager.unlock(DISTRIBUTED_OBJECT_KEY_REPORT);
      actionExportLatchManager.deleteCountDownLatch(DISTRIBUTED_OBJECT_KEY_REPORT_LATCH);
    }
    return result;
  }
}

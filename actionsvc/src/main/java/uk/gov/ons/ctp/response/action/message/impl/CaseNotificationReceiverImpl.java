package uk.gov.ons.ctp.response.action.message.impl;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.oxm.Marshaller;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.util.DeadLetterLogCommand;
import uk.gov.ons.ctp.response.action.message.CaseNotificationReceiver;
import uk.gov.ons.ctp.response.action.service.CaseNotificationService;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotifications;

/**
 * Message end point for Case notification life cycle messages, please see
 * flows.xml.
 *
 */
@MessageEndpoint
@Slf4j
public class CaseNotificationReceiverImpl implements CaseNotificationReceiver {

  @Inject
  @Qualifier("caseNotificationUnmarshaller")
  Marshaller marshaller;

  @Inject
  private CaseNotificationService caseNotificationService;

  @Override
  @ServiceActivator(inputChannel = "caseNotificationTransformed")
  public void acceptNotification(CaseNotifications caseNotifications) {
    log.debug("Receiving case notifications for case ids {}", caseNotifications.getCaseNotifications().stream()
                  .map(cn -> cn.getCaseId().toString())
                  .collect(Collectors.joining(",")));

    DeadLetterLogCommand<CaseNotifications> command = new DeadLetterLogCommand<CaseNotifications>(marshaller, caseNotifications);
    command.run((CaseNotifications x)->caseNotificationService.acceptNotification(x.getCaseNotifications()));
  }

}

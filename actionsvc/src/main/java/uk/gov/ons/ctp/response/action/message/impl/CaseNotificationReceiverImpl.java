package uk.gov.ons.ctp.response.action.message.impl;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.message.CaseNotificationReceiver;
import uk.gov.ons.ctp.response.action.service.CaseNotificationService;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotifications;

/**
 * Message end point for Case notification life cycle messages, please see flows.xml.
 */
@MessageEndpoint
@Slf4j
public class CaseNotificationReceiverImpl implements CaseNotificationReceiver {

  @Autowired
  private CaseNotificationService caseNotificationService;

  @Override
  @ServiceActivator(inputChannel = "caseNotificationTransformed", adviceChain = "caseNotificationRetryAdvice")
  public void acceptNotification(CaseNotifications caseNotifications) {
    log.debug("Receiving case notifications for case ids {}", caseNotifications.getCaseNotifications().stream()
                  .map(cn -> cn.getCaseId().toString())
                  .collect(Collectors.joining(",")));
    caseNotificationService.acceptNotification(caseNotifications.getCaseNotifications());
  }
}

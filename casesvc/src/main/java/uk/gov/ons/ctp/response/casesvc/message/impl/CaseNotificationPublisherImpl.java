package uk.gov.ons.ctp.response.casesvc.message.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import uk.gov.ons.ctp.response.casesvc.message.CaseNotificationPublisher;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotification;
import uk.gov.ons.ctp.response.casesvc.message.notification.CaseNotifications;

/**
 * Service implementation responsible for publishing case lifecycle events to
 * notification channel
 *
 */
@MessageEndpoint
@Slf4j
public class CaseNotificationPublisherImpl implements CaseNotificationPublisher {

  @Qualifier("caseNotificationRabbitTemplate")
  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Override
  public void sendNotifications(List<CaseNotification> caseNotificationList) {
    log.debug("Entering sendNotifications with {} CaseNotification ", caseNotificationList.size());
    CaseNotifications caseNotifications = new CaseNotifications();
    caseNotifications.getCaseNotifications().addAll(caseNotificationList);
    rabbitTemplate.convertAndSend(caseNotifications);
  }
}

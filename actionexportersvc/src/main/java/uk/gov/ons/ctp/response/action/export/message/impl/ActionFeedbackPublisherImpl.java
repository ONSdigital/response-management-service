package uk.gov.ons.ctp.response.action.export.message.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.MessageEndpoint;
import uk.gov.ons.ctp.response.action.export.message.ActionFeedbackPublisher;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

/**
 * Service implementation responsible for publishing an action feedback message
 * to the action service.
 *
 */
@MessageEndpoint
@Slf4j
public class ActionFeedbackPublisherImpl implements ActionFeedbackPublisher {

  @Qualifier("actionFeedbackRabbitTemplate")
  @Autowired
  private RabbitTemplate rabbitTemplate;

  public void sendActionFeedback(ActionFeedback actionFeedback) {
    log.debug("Entering sendActionFeedback for actionId {} ", actionFeedback.getActionId());
    rabbitTemplate.convertAndSend(actionFeedback);
  }
}

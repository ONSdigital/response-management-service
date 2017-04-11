package uk.gov.ons.ctp.response.action.export.message.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.export.message.ActionFeedbackPublisher;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

/**
 * Service implementation responsible for publishing an action feedback message
 * to the action service.
 *
 */
@Named
@Slf4j
public class ActionFeedbackPublisherImpl implements ActionFeedbackPublisher {

  @Qualifier("actionFeedbackRabbitTemplate")
  @Inject
  private RabbitTemplate rabbitTemplate;

  public void sendActionFeedback(ActionFeedback actionFeedback) {
    log.debug("Entering sendActionFeedback for actionId {} ", actionFeedback.getActionId());
    rabbitTemplate.convertAndSend(actionFeedback);
  }
}

package uk.gov.ons.ctp.response.action.message.impl;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import uk.gov.ons.ctp.response.action.message.FeedbackReceiver;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

/**
 * The entry point for inbound feedback messages from SpringIntegration. See the integration-context.xml
 *
 */
@MessageEndpoint
public class FeedbackReceiverImpl implements FeedbackReceiver {
  @Override
  @ServiceActivator(inputChannel = "feedbackTransformed")
  public void acceptFeedback(ActionFeedback feedback) {
    System.out.println("We have feedback with situation " + feedback.getSituation());
  }
}

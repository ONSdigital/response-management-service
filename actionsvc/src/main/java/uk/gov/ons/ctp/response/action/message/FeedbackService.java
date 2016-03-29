package uk.gov.ons.ctp.response.action.message;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

@MessageEndpoint
public class FeedbackService {
  @ServiceActivator(inputChannel="feedback.transformed")
  public void acceptFeedback(ActionFeedback feedback) {
    System.out.println("We have feedback with situation " + feedback.getSituation());
  }
}

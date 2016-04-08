package uk.gov.ons.ctp.response.action.message.impl;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import uk.gov.ons.ctp.response.action.message.FeedbackReceiver;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

@MessageEndpoint
<<<<<<< HEAD:actionsvc/src/main/java/uk/gov/ons/ctp/response/action/message/impl/FeedbackReceiverImpl.java
public class FeedbackReceiverImpl implements FeedbackReceiver {
  /* (non-Javadoc)
   * @see uk.gov.ons.ctp.response.action.message.impl.FeedbackReceiver#acceptFeedback(uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback)
   */
  @Override
  @ServiceActivator(inputChannel="feedbackTransformed")
  public void acceptFeedback(ActionFeedback feedback) {
    System.out.println("We have feedback with situation " + feedback.getSituation());
  }
=======
public class FeedbackService {

	private String status = "feedbackNotAccepted";

	@ServiceActivator(inputChannel = "feedbackTransformed")
	public void acceptFeedback(ActionFeedback feedback) {
		status = "feedbackAccepted";
		System.out.println("We have feedback with situation " + feedback.getSituation());
	}

	public String getStatus() {
		return status;
	}
>>>>>>> 2e7b810f8a132e3ad0aa4a987f68d4044178d1b0:actionsvc/src/main/java/uk/gov/ons/ctp/response/action/message/FeedbackService.java
}

package uk.gov.ons.ctp.response.action.message;

import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

@MessageEndpoint
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
}

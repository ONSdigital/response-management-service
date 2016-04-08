package uk.gov.ons.ctp.response.action.message;

import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

public interface FeedbackReceiver {

  void acceptFeedback(ActionFeedback feedback);

}
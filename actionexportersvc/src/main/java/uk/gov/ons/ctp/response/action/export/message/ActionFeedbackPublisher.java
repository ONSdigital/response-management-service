package uk.gov.ons.ctp.response.action.export.message;

import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

/**
 * Service responsible for publishing an action feedback message to the action service.
 *
 */
public interface ActionFeedbackPublisher {
  /**
   * To publish an ActionFeedback message
   *
   * @param actionFeedback the ActionFeedback to publish.
   */
  void sendActionFeedback(ActionFeedback actionFeedback);
}

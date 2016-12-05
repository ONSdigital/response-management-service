package uk.gov.ons.ctp.response.action.service;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;

/**
 * simple interface for the service that deals with received feedback from handlers
 *
 */
public interface FeedbackService {
  /**
   * take feedback. do somethng with it.
   * @param feedback you know. like feedback man.
   */
 void acceptFeedback(ActionFeedback feedback) throws CTPException;
}

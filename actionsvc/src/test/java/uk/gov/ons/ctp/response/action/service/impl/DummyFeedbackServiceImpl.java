package uk.gov.ons.ctp.response.action.service.impl;

import uk.gov.ons.ctp.response.action.message.feedback.ActionFeedback;
import uk.gov.ons.ctp.response.action.service.FeedbackService;

/**
 * a dummey service impl
 *
 */
public class DummyFeedbackServiceImpl implements FeedbackService {

  @Override
  public void acceptFeedback(ActionFeedback feedback) {
    // Do nothing - here only as a concrete mock stand in
  }

}

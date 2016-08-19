package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.response.action.domain.model.ActionCase;

/**
 * Service to persist case life cycle event notifications.
 *
 */
public interface CaseNotificationService {

  /**
   * Deal with case life cycle notification
   *
   * @param cases List of ActionCase entity objects with notification type.
   */
  void acceptNotification(List<ActionCase> cases);

}

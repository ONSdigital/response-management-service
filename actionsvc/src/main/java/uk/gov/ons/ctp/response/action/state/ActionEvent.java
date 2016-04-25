package uk.gov.ons.ctp.response.action.state;

/**
 * Definition of events that can be applied to the Action entity
 */
public enum ActionEvent {
  REQUEST_DISTRIBUTED, REQUEST_FAILED, REQUEST_ACCEPTED, REQUEST_COMPLETED,
  REQUEST_CANCELLED, CANCELLATION_DISTRIBUTED, CANCELLATION_FAILED, CANCELLATION_ACCEPTED, CANCELLATION_COMPLETED
}

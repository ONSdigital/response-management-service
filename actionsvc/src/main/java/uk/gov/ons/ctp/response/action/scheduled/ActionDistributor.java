package uk.gov.ons.ctp.response.action.scheduled;

public interface ActionDistributor {
  /**
   * The scheduled wake up method - this is where it all kicks off.
   */
  void wakeUp();

}
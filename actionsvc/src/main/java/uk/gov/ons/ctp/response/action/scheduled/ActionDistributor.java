package uk.gov.ons.ctp.response.action.scheduled;

import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

@Named
public class ActionDistributor {

  @Scheduled(fixedRate=5000)
  public void work() {
      System.out.println("Boo!");
  }
}

package uk.gov.ons.ctp.response.action.scheduled;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.service.ActionService;

@Named
@Slf4j
public class ActionDistributor {

  @Inject
  private ActionService actionService;
  
  @Scheduled(fixedRate=5000)
  public void work() {
      log.debug("ActionDistributor is in the house!");
      
      List<ActionType> actionTypes = actionService.findActionTypes();
  }
}

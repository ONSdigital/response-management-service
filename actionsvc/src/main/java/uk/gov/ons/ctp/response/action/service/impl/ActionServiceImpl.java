package uk.gov.ons.ctp.response.action.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.repository.ActionRepository;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * An implementation of the ActionService using JPA Repository class(es)
 * The business logic for the application should reside here.
 */

@Named
@Slf4j
public class ActionServiceImpl implements ActionService {

  @Inject
  private ActionRepository actionRepo;

  @Override
  public final Action findActionByActionId(final Integer actionId) {
    log.debug("Entering findActionByActionId with {}", actionId);
    return actionRepo.findOne(actionId);
  }

  @Override
  public final List<Action> findActionsByCaseId(final Integer caseId) {
    log.debug("Entering findActionsByCaseId with {}", caseId);
    return actionRepo.findByCaseId(caseId);
  }

}

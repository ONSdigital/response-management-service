package uk.gov.ons.ctp.response.action.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.service.ActionCaseService;

/**
 * An ActionService implementation which encapsulates all business logic
 * operating on the Action entity model.
 */

@Service
public class ActionCaseServiceImpl implements ActionCaseService {

  @Autowired
  private ActionCaseRepository actionCaseRepo;

  @Override
  public ActionCase findActionCase(Integer caseId) {
    return actionCaseRepo.findOne(caseId);
  }
}
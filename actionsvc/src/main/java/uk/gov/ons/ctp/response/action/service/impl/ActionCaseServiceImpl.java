package uk.gov.ons.ctp.response.action.service.impl;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.domain.repository.ActionCaseRepository;
import uk.gov.ons.ctp.response.action.service.ActionCaseService;

/**
 * An ActionService implementation which encapsulates all business logic
 * operating on the Action entity model.
 */

@Named
public class ActionCaseServiceImpl implements ActionCaseService {

  @Inject
  private ActionCaseRepository actionCaseRepo;

  @Override
  public ActionCase findActionCase(Integer caseId) {
    return actionCaseRepo.findOne(caseId);
  }
}
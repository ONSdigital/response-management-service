package uk.gov.ons.ctp.response.action.export.service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.repository.ActionRequestRepository;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;

/**
 * The implementation of ActionRequestService
 */
@Named
@Slf4j
public class ActionRequestServiceImpl implements ActionRequestService {

  @Inject
  private ActionRequestRepository repository;

  @Override
  public List<ActionRequestInstruction> retrieveAllActionRequests() {
    return repository.findAll();
  }

  @Override
  public ActionRequestInstruction retrieveActionRequest(BigInteger actionId) {
    return repository.findOne(actionId);
  }

  @Override
  public ActionRequestInstruction save(final ActionRequestInstruction actionRequest) {
    log.debug("Saving ActionRequest {}", actionRequest.getActionId());
    return repository.save(actionRequest);
  }

  @Override
  public List<ActionRequestInstruction> findByDateSentIsNullAndActionType(String actionType) {
    return repository.findByDateSentIsNullAndActionType(actionType);
  }

  @Override
  public List<String> retrieveActionTypes() {
    return repository.findAllActionType();
  }

  @Override
  public int updateDateSentByActionId(Set<BigInteger> actionIds, Timestamp dateSent) {
    return repository.updateDateSentByActionId(actionIds, dateSent);
  }

  @Override
  public List<BigInteger> retrieveResponseRequiredByActionId(Set<BigInteger> actionIds) {
    return repository.retrieveResponseRequiredByActionId(actionIds);
  }
}

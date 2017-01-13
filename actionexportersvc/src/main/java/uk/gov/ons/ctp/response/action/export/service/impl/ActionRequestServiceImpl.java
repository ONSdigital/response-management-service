package uk.gov.ons.ctp.response.action.export.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestDocument;
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
  public List<ActionRequestDocument> retrieveAllActionRequestDocuments() {
    return repository.findAll();
  }

  @Override
  public ActionRequestDocument retrieveActionRequestDocument(BigInteger actionId) {
    return repository.findOne(actionId);
  }

  @Override
  public ActionRequestDocument save(final ActionRequestDocument actionRequest) {
    log.debug("Saving ActionRequestDocument {}", actionRequest.getActionId());
    return repository.save(actionRequest);
  }

  @Override
  public List<ActionRequestDocument> findByDateSentIsNullAndActionType(String actionType) {
    return repository.findByDateSentIsNullAndActionType(actionType);
  }

  @Override
  public List<String> retrieveActionTypes() {
    return repository.findAllActionType();
  }
}

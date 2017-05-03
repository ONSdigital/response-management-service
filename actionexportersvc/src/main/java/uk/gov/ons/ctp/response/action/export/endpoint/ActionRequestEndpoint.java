package uk.gov.ons.ctp.response.action.export.endpoint;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.ExportMessage;
import uk.gov.ons.ctp.response.action.export.message.SftpServicePublisher;
import uk.gov.ons.ctp.response.action.export.representation.ActionRequestInstructionDTO;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;
import uk.gov.ons.ctp.response.action.export.service.TransformationService;

/**
 * The REST endpoint controller for ActionRequests.
 */
@RestController
@RequestMapping(value = "/actionrequests", produces = "application/json")
@Slf4j
public class ActionRequestEndpoint {

  public static final String ACTION_REQUEST_NOT_FOUND = "ActionRequest not found for actionId";

  public static final String ACTION_REQUEST_TRANSFORM_ERROR = "Error transforming ActionRequest for actionId";

  @Autowired
  private ActionRequestService actionRequestService;

  @Autowired
  private TransformationService transformationService;

  @Autowired
  private SftpServicePublisher sftpService;

  @Autowired
  private MapperFacade mapperFacade;

  /**
   * To retrieve all ActionRequests
   * 
   * @return a list of ActionRequests
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<ActionRequestInstructionDTO> findAllActionRequests() {
    log.debug("Entering findAllActionRequests ...");
    List<ActionRequestInstruction> actionRequests = actionRequestService.retrieveAllActionRequests();
    List<ActionRequestInstructionDTO> results = mapperFacade.mapAsList(actionRequests,
        ActionRequestInstructionDTO.class);
    return CollectionUtils.isEmpty(results) ? null : results;
  }

  /**
   * To retrieve a specific ActionRequest
   * 
   * @param actionId for the specific ActionRequest to retrieve
   * @return the specific ActionRequest
   * @throws CTPException if no ActionRequest found
   */
  @RequestMapping(value = "/{actionId}", method = RequestMethod.GET)
  public ActionRequestInstructionDTO findActionRequest(@PathVariable("actionId") final BigInteger actionId)
      throws CTPException {
    log.debug("Entering findActionRequest with {}", actionId);
    ActionRequestInstruction result = actionRequestService.retrieveActionRequest(actionId);
    if (result == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
          String.format("%s %d", ACTION_REQUEST_NOT_FOUND, actionId));
    }
    return mapperFacade.map(result, ActionRequestInstructionDTO.class);
  }

  /**
   * To export a specific ActionRequest
   * 
   * @param actionId the actionId of the specific ActionRequest
   * @return 201 if successful
   * @throws CTPException if specific ActionRequest not found
   */
  @RequestMapping(value = "/{actionId}", method = RequestMethod.POST)
  public ResponseEntity<?> export(@PathVariable("actionId") final BigInteger actionId) throws CTPException {
    log.debug("Entering export with actionId {}", actionId);
    ActionRequestInstruction actionRequest = actionRequestService.retrieveActionRequest(actionId);
    if (actionRequest == null) {
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
          String.format("%s %d", ACTION_REQUEST_NOT_FOUND, actionId));
    }
    ExportMessage message = new ExportMessage();
    transformationService.processActionRequest(message, actionRequest);
    if (message.isEmpty()) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR,
          String.format("%s %d", ACTION_REQUEST_TRANSFORM_ERROR, actionId));
    }
    message.getOutputStreams().forEach((fileName, stream) -> {
      sftpService.sendMessage(fileName, message.getActionRequestIds(fileName), stream);
    });

    ActionRequestInstructionDTO actionRequestDTO = mapperFacade.map(actionRequest, ActionRequestInstructionDTO.class);
    return ResponseEntity.created(URI.create("TODO")).body(actionRequestDTO);
  }
}

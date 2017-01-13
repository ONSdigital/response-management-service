package uk.gov.ons.ctp.response.action.export.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.ExportMessage;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMapping;
import uk.gov.ons.ctp.response.action.export.service.TemplateMappingService;
import uk.gov.ons.ctp.response.action.export.service.TemplateService;
import uk.gov.ons.ctp.response.action.export.service.TransformationService;

/**
 * The implementation of TransformationService
 */
@Named
@Slf4j
public class TransformationServiceImpl implements TransformationService {

  @Inject
  private TemplateService templateService;

  @Inject
  private TemplateMappingService templateMappingService;

  @Override
  public ExportMessage processActionRequests(ExportMessage message, List<ActionRequestInstruction> requests)
      throws CTPException {
    return buildExportMessage(message, requests);
  }

  @Override
  public ExportMessage processActionRequest(ExportMessage message, ActionRequestInstruction actionRequest)
      throws CTPException {
    List<ActionRequestInstruction> requests = new ArrayList<>();
    requests.add(actionRequest);
    return buildExportMessage(message, requests);
  }

  /**
   * Produces ExportMessage with stream objects and list of ActionRequest Ids.
   * Assumes actionTypes being processed are unique and not already in
   * ExportMessage passed in to be built, if are already present will be
   * replaced.
   *
   * @param message to build
   * @param actionRequestList the list to be processed
   * @return ExportMessage with stream objects and list of ActionRequest Ids.
   * @throws CTPException if cannot retrieve TemplateMapping.
   */
  private ExportMessage buildExportMessage(ExportMessage message,
      List<ActionRequestInstruction> actionRequestList) throws CTPException {

    // if nothing to process return ExportMessage
    if (actionRequestList.isEmpty()) {
      return message;
    }

    Map<String, TemplateMapping> mapping = templateMappingService
        .retrieveAllTemplateMappingsByActionType();
    Map<String, List<ActionRequestInstruction>> templateRequests = actionRequestList.stream()
        .collect(Collectors.groupingBy(ActionRequestInstruction::getActionType));
    templateRequests.forEach((actionType, actionRequests) -> {
      if (mapping.containsKey(actionType)) {
        try {
          message.getOutputStreams().put(actionType,
              templateService.stream(actionRequests, mapping.get(actionType).getTemplate()));
          List<String> addActionIds = new ArrayList<String>();
          message.getActionRequestIds().put(actionType, addActionIds);
          actionRequests.forEach((actionRequest) -> {
            addActionIds.add(actionRequest.getActionId().toString());
          });
        } catch (CTPException e) {
          // catch failure for templateService stream operation for that
          // actionType but try others, if any.
          log.error("Error generating actionType : {}. {}", actionType, e.getMessage());
        }
      } else {
        log.warn("No mapping for actionType : {}.", actionType);
      }
    });
    return message;
  }
}

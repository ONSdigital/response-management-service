package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.common.service.CTPService;
import uk.gov.ons.ctp.response.action.domain.model.Action;

/**
 * Created by Martin.Humphrey on 16/2/16.
 */
public interface ActionService extends CTPService {

  Action findActionByActionId(Integer actionId);

  List<Action> findActionsByCaseId(Integer caseId);

}

package uk.gov.ons.ctp.response.action.service;

import java.util.List;

import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.casesvc.representation.AddressDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CaseGroupDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;

/**
 * A Service which utilises the CaseSvc via RESTful client calls
 *
 */
public interface CaseSvcClientService {
  /**
   * Create and post to Case service a new CaseEvent
   *
   * @param action the action for which we need the event
   * @param actionCategory the category for the event
   * @return the newly created caseeventdto
   */
  CaseEventDTO createNewCaseEvent(final Action action, CategoryDTO.CategoryType actionCategory);

  /**
   * Call CaseSvc using REST to get the Address MAY throw a RuntimeException if
   * the call fails
   *
   * @param uprn identifies the Address to fetch
   * @return the Address we fetched
   */
  AddressDTO getAddress(final Long uprn);

  /**
   * Call CaseSvc using REST to get the CaseGroups details MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseGroupId identifies the Case to fetch
   * @return the Case we fetched
   */
  CaseGroupDTO getCaseGroup(final Integer caseGroupId);

  /**
   * Call CaseSvc using REST to get the Case details MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseId identifies the Case to fetch
   * @return the Case we fetched
   */
  CaseDTO getCase(final Integer caseId);

  /**
   * Call CaseSvc using REST to get the CaseEvents for the Case MAY throw a
   * RuntimeException if the call fails
   *
   * @param caseId identifies the Case to fetch events for
   * @return the CaseEvents we found for the case
   */
  List<CaseEventDTO> getCaseEvents(final Integer caseId);

}

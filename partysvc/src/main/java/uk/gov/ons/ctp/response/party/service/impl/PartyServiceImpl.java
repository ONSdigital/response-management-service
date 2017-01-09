package uk.gov.ons.ctp.response.party.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import uk.gov.ons.ctp.response.party.domain.repository.PartyRepository;
import uk.gov.ons.ctp.response.party.service.PartyService;

/**
 * Accept feedback from handlers
 */
@Named
public class PartyServiceImpl implements PartyService {


  @Inject
  private PartyRepository partyRepository;

 
}

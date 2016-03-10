package uk.gov.ons.ctp.response.action.endpoint;

import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_ACTIONSTATE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_ACTIONTYPENAME;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_PLANID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_PRIORITY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_ACTIONSTATE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_ACTIONTYPENAME;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_PLANID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_PRIORITY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_SITUATION;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTIONID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CASEID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CREATEDBY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CREATEDDATE_VALUE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.NON_EXISTING_ID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.OUR_EXCEPTION_MESSAGE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.UNCHECKED_EXCEPTION;

import javax.ws.rs.core.Application;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.service.ActionService;
import uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory;

/**
 * Action Endpoint Unit tests
 */
public class ActionEndpointUnitTest extends CTPJerseyTest {

  @Override
  public Application configure() {
    return super.init(ActionEndpoint.class, ActionService.class, MockActionServiceFactory.class, new ActionBeanMapper()); 
  }

  @Test
  public void findActionsByCaseIdFound() {
    with("http://localhost:9998/actions/case/%s", ACTION_CASEID)
    .assertResponseCodeIs(HttpStatus.OK)
    .assertArrayLengthInBodyIs(2)
    .assertIntegerOccursThroughoutListInBody("$..caseId",  ACTION_CASEID)
    .assertIntegerListInBody("$..actionPlanId", ACTION1_PLANID, ACTION2_PLANID)
    .assertStringListInBody("$..state", ACTION1_ACTIONSTATE, ACTION2_ACTIONSTATE)
    .assertStringListInBody("$..actionTypeName", ACTION1_ACTIONTYPENAME, ACTION2_ACTIONTYPENAME)
    .assertStringListInBody("$..priority", ACTION1_PRIORITY, ACTION2_PRIORITY)
    .assertStringOccursThroughoutListInBody("$..createdDatetime", ACTION_CREATEDDATE_VALUE)
    .assertStringOccursThroughoutListInBody("$..createdBy",  ACTION_CREATEDBY)
    .andClose();
  }
 
  @Test
  public void findActionByActionIdFound() {
    with("http://localhost:9998/actions/%s", ACTIONID)
    .assertResponseCodeIs(HttpStatus.OK)
    .assertIntegerInBody("$.actionId",  ACTIONID)
    .assertIntegerInBody("$.caseId",  ACTION_CASEID)
    .assertIntegerInBody("$.actionPlanId",  ACTION2_PLANID)
    .assertStringInBody("$.state",  ACTION2_ACTIONSTATE)
    .assertStringInBody("$.actionTypeName",  ACTION2_ACTIONTYPENAME)
    .assertStringInBody("$.priority",  ACTION2_PRIORITY)
    .assertStringInBody("$.situation",  ACTION2_SITUATION)
    .assertStringInBody("$.createdDatetime",  ACTION_CREATEDDATE_VALUE)
    .assertStringInBody("$.createdBy",  ACTION_CREATEDBY)
    .andClose();
  }

  @Test
  public void findActionByCaseIdNotFound() {
    with("http://localhost:9998/actions/case/%s", NON_EXISTING_ID)
      .assertResponseCodeIs(HttpStatus.NO_CONTENT)
      .assertResponseLengthIs(-1)
      .andClose();
  }


  @Test
  public void findActionByActionIdNotFound() {
    with("http://localhost:9998/actions/%s", NON_EXISTING_ID)
      .assertResponseCodeIs(HttpStatus.NOT_FOUND)
      .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
      .assertTimestampExists()
      .assertMessageEquals("Action not found for id %s", NON_EXISTING_ID)
      .andClose();
  }

  @Test
  public void findActionByActionIdUnCheckedException() {
    with("http://localhost:9998/actions/%s", UNCHECKED_EXCEPTION)
      .assertResponseCodeIs(HttpStatus.INTERNAL_SERVER_ERROR)
      .assertFaultIs(CTPException.Fault.SYSTEM_ERROR)
      .assertTimestampExists()
      .assertMessageEquals(OUR_EXCEPTION_MESSAGE)
      .andClose();
  } 
}

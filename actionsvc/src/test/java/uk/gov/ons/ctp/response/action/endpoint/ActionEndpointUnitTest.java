package uk.gov.ons.ctp.response.action.endpoint;

import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_ACTIONSTATE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_ACTIONTYPENAME;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_PLANID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_PRIORITY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_RULEID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION1_SITUATION;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_ACTIONSTATE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_ACTIONTYPENAME;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_PLANID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_PRIORITY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_RULEID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION2_SITUATION;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION3_ACTIONSTATE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTIONID_2;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CASEID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CREATEDBY;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_CREATEDDATE_VALUE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.ACTION_NOTFOUND;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.NON_EXISTING_ID;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.OUR_EXCEPTION_MESSAGE;
import static uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory.UNCHECKED_EXCEPTION;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionService;
import uk.gov.ons.ctp.response.action.utility.MockActionServiceFactory;

/**
 * ActionEndpoint Unit tests
 */
public final class ActionEndpointUnitTest extends CTPJerseyTest {

  private static final String ACTION_VALIDJSON = "{\"caseId\": " + ACTION_CASEID + ","
      + "\"actionPlanId\": " + ACTION2_PLANID + ","
      + "\"actionRuleId\": " + ACTION2_RULEID + ","
      + "\"actionTypeName\": \"" + ACTION2_ACTIONTYPENAME + "\","
      + "\"createdBy\": \"" + ACTION_CREATEDBY + "\","
      + "\"priority\": " + ACTION2_PRIORITY + ","
      + "\"state\": \"" + ACTION2_ACTIONSTATE + "\"}";

  private static final String ACTION_INVALIDJSON_PROP = "{\"caseId\": " + ACTION_CASEID + ","
      + "\"actionPlanId\": " + ACTION2_PLANID + ","
      + "\"actionRuleId\": " + ACTION2_RULEID + ","
      + "\"actionTypename\": \"" + ACTION2_ACTIONTYPENAME + "\","
      + "\"createdBy\": \"" + ACTION_CREATEDBY + "\","
      + "\"priority\": " + ACTION2_PRIORITY + ","
      + "\"state\": \"" + ACTION2_ACTIONSTATE + "\"}";

  private static final String ACTION_INVALIDJSON_MISSING_PROP = "{\"caseId\": " + ACTION_CASEID + ","
      + "\"actionRuleId\": " + ACTION2_RULEID + ","
      + "\"createdBy\": \"" + ACTION_CREATEDBY + "\","
      + "\"priority\": " + ACTION2_PRIORITY + ","
      + "\"state\": \"" + ACTION2_ACTIONSTATE + "\"}";

  @Override
  public Application configure() {
    return super.init(ActionEndpoint.class, ActionService.class, MockActionServiceFactory.class,
        new ActionBeanMapper(), new CTPMessageBodyReader<ActionDTO>(ActionDTO.class));
  }

  /**
   * Test requesting Actions filtered by action type name and state found.
   */
  @Test
  public void findActionsByActionTypeAndStateFound() {
    with("http://localhost:9998/actions?actiontype=%s&state=%s", ACTION2_ACTIONTYPENAME, ACTION2_ACTIONSTATE)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
        .assertStringListInBody("$..situation", ACTION2_SITUATION)
        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test requesting Actions filtered by action type name and state not found.
   */
  @Test
  public void findActionsByActionTypeAndStateNotFound() {
    with("http://localhost:9998/actions?actiontype=%s&state=%s", ACTION_NOTFOUND, ACTION2_ACTIONSTATE)
        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
        .assertResponseLengthIs(-1)
        .andClose();
  }

  /**
   * Test requesting Actions filtered by action type name found.
   */
  @Test
  public void findActionsByActionTypeFound() {
    with("http://localhost:9998/actions?actiontype=%s", ACTION2_ACTIONTYPENAME)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
        .assertStringListInBody("$..situation", ACTION2_SITUATION)
        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test requesting Actions filtered by action type name not found.
   */
  @Test
  public void findActionsByActionTypeNotFound() {
    with("http://localhost:9998/actions?actiontype=%s", ACTION_NOTFOUND)
        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
        .assertResponseLengthIs(-1)
        .andClose();
  }

  /**
   * Test requesting Actions filtered by action state found.
   */
  @Test
  public void findActionsByStateFound() {
    with("http://localhost:9998/actions?state=%s", ACTION2_ACTIONSTATE.toString())
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
        .assertStringListInBody("$..situation", ACTION2_SITUATION)
        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test requesting an Action by action Id found.
   */
  @Test
  public void findActionByActionIdFound() {
    with("http://localhost:9998/actions/%s", ACTIONID_2.intValue())
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerInBody("$.actionId", ACTIONID_2.intValue())
        .assertIntegerInBody("$.caseId", ACTION_CASEID)
        .assertIntegerInBody("$.actionPlanId", ACTION2_PLANID)
        .assertIntegerInBody("$.actionRuleId", ACTION2_RULEID)
        .assertStringInBody("$.actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringInBody("$.createdBy", ACTION_CREATEDBY)
        .assertIntegerInBody("$.priority", ACTION2_PRIORITY)
        .assertStringInBody("$.situation", ACTION2_SITUATION)
        .assertStringInBody("$.state", ACTION2_ACTIONSTATE.toString())
        .assertStringInBody("$.createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test requesting an Action by action Id not found.
   */
  @Test
  public void findActionByActionIdNotFound() {
    with("http://localhost:9998/actions/%s", NON_EXISTING_ID)
        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
        .assertTimestampExists()
        .assertMessageEquals("Action not found for id %s", NON_EXISTING_ID)
        .andClose();
  }

  /**
   * Test requesting Actions by case Id found.
   */
  @Test
  public void findActionsByCaseIdFound() {
    with("http://localhost:9998/actions/case/%s", ACTION_CASEID)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerListInBody("$..actionId", new Integer(1), new Integer(2))
        .assertIntegerListInBody("$..caseId", ACTION_CASEID, ACTION_CASEID)
        .assertIntegerListInBody("$..actionPlanId", ACTION1_PLANID, ACTION2_PLANID)
        .assertIntegerListInBody("$..actionRuleId", ACTION1_RULEID, ACTION2_RULEID)
        .assertStringListInBody("$..actionTypeName", ACTION1_ACTIONTYPENAME, ACTION2_ACTIONTYPENAME)
        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY, ACTION_CREATEDBY)
        .assertIntegerListInBody("$..priority", ACTION1_PRIORITY, ACTION2_PRIORITY)
        .assertStringListInBody("$..situation", ACTION1_SITUATION, ACTION2_SITUATION)
        .assertStringListInBody("$..state", ACTION1_ACTIONSTATE.toString(), ACTION2_ACTIONSTATE.toString())
        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE, ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test requesting Actions by case Id not found.
   */
  @Test
  public void findActionByCaseIdNotFound() {
    with("http://localhost:9998/actions/case/%s", NON_EXISTING_ID)
        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
        .assertResponseLengthIs(-1)
        .andClose();
  }

  /**
   * Test updating action not found
   */
  @Test
  public void updateActionByActionIdNotFound() {
    with("http://localhost:9998/actions/%s", NON_EXISTING_ID).put(MediaType.APPLICATION_JSON_TYPE, ACTION_VALIDJSON)
        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
        .andClose();
  }

  /**
   * Test requesting an Action creating an Unchecked Exception.
   */
  @Test
  public void findActionByActionIdUnCheckedException() {
    with("http://localhost:9998/actions/%s", UNCHECKED_EXCEPTION)
        .assertResponseCodeIs(HttpStatus.INTERNAL_SERVER_ERROR)
        .assertFaultIs(CTPException.Fault.SYSTEM_ERROR)
        .assertTimestampExists()
        .assertMessageEquals(OUR_EXCEPTION_MESSAGE)
        .andClose();
  }

  /**
   * Test creating an Action with valid JSON.
   */
  @Test
  public void createActionGoodJsonProvided() {
    with("http://localhost:9998/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_VALIDJSON)
        .assertResponseCodeIs(HttpStatus.CREATED)
        .assertIntegerInBody("$.actionId", ACTIONID_2.intValue())
        .assertIntegerInBody("$.caseId", ACTION_CASEID)
        .assertIntegerInBody("$.actionPlanId", ACTION2_PLANID)
        .assertIntegerInBody("$.actionRuleId", ACTION2_RULEID)
        .assertStringInBody("$.actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringInBody("$.createdBy", ACTION_CREATEDBY)
        .assertIntegerInBody("$.priority", ACTION2_PRIORITY)
        .assertStringInBody("$.situation", ACTION2_SITUATION)
        .assertStringInBody("$.state", ACTION2_ACTIONSTATE.toString())
        .assertStringInBody("$.createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

  /**
   * Test creating an Action with invalid JSON Property.
   */
  @Test
  public void createActionInvalidPropJsonProvided() {
    with("http://localhost:9998/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_INVALIDJSON_PROP)
        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
        .assertTimestampExists()
        .assertMessageEquals("Provided json is incorrect.")
        .andClose();
  }

  /**
   * Test creating an Action with missing JSON Property.
   */
  @Test
  public void createActionMissingPropJsonProvided() {
    with("http://localhost:9998/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_INVALIDJSON_MISSING_PROP)
        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
        .assertTimestampExists()
        .assertMessageEquals("Provided json fails validation.")
        .andClose();
  }

  /**
   * Test cancelling an Action.
   */
  @Test
  public void cancelActions() {
    with("http://localhost:9998/actions/case/%s/cancel", ACTION_CASEID).put(MediaType.APPLICATION_JSON_TYPE, "")
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
        .assertStringListInBody("$..situation", ACTION2_SITUATION)
        .assertStringListInBody("$..state", ACTION3_ACTIONSTATE.toString())
        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
        .andClose();
  }

//  /**
//   * Test cancelling an Action.
//   */
//  @Test
//  public void cancelActionsCaseNotFound() {
//    with("http://localhost:9998/actions/case/%s/cancel", NON_EXISTING_ID).put(MediaType.APPLICATION_JSON_TYPE, "")
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .andClose();
//  }
}

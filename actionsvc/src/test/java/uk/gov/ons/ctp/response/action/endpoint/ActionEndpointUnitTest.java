package uk.gov.ons.ctp.response.action.endpoint;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.ons.ctp.common.MvcHelper.getJson;
import static uk.gov.ons.ctp.common.utility.MockMvcControllerAdviceHelper.mockAdviceFor;

import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionCaseService;
import uk.gov.ons.ctp.response.action.service.ActionService;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ActionEndpoint Unit tests
 */
public final class ActionEndpointUnitTest {

  @InjectMocks
  private ActionEndpoint actionEndpoint;

  @Mock
  private ActionService actionService;

  @Mock
  private ActionCaseService actionCaseService;

  @Spy
  private MapperFacade mapperFacade = new ActionBeanMapper();

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders
            .standaloneSetup(actionEndpoint)
            .setHandlerExceptionResolvers(mockAdviceFor(RestExceptionHandler.class))
            .build();
  }

  private static final ActionDTO.ActionState ACTION2_ACTIONSTATE = ActionDTO.ActionState.COMPLETED;

  private static final Integer ACTION_CASEID = 124;
  private static final Integer ACTION2_PRIORITY = 3;
  private static final Integer ACTION2_PLANID = 2;
  private static final Integer ACTION2_RULEID = 2;

  private static final BigInteger ACTIONID_2 = BigInteger.valueOf(2);

  private static final Boolean ACTION2_ACTIONTYPECANCEL = false;
  private static final Boolean ACTION2_RESPONSEREQUIRED = false;
  private static final Boolean ACTION2_MANUALLY_CREATED = false;

  private static final Timestamp ACTION_CREATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  private static final Timestamp ACTION_UPDATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");

  private static final String ACTION2_ACTIONTYPENAME = "actiontypename2";
  private static final String ACTION2_ACTIONTYPEDESC = "actiontypedesc2";
  private static final String ACTION2_ACTIONTYPEHANDLER = "Field";
  private static final String ACTION2_SITUATION = "Sent";
  private static final String ACTION_CREATEDBY = "Unit Tester";
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

  /**
   * Test requesting Actions filtered by action type name and state found.
   */
  @Test
  public void findActionsByActionTypeAndStateFound() throws Exception {
    List<Action> result = new ArrayList<Action>();
    ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
    result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION,
            ACTION2_ACTIONSTATE, ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
    when(actionService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(ACTION2_ACTIONTYPENAME,
            ACTION2_ACTIONSTATE)).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions?actiontype=%s&state=%s", ACTION2_ACTIONTYPENAME, ACTION2_ACTIONSTATE)));

    actions.andExpect(status().isOk());


//    with("/actions?actiontype=%s&state=%s", ACTION2_ACTIONTYPENAME, ACTION2_ACTIONSTATE)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
//        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
//        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
//        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
//        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
//        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
//        .assertStringListInBody("$..situation", ACTION2_SITUATION)
//        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
//        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
  }

//  /**
//   * Test requesting Actions filtered by action type name and state not found.
//   */
//  @Test
//  public void findActionsByActionTypeAndStateNotFound() {
//    with("/actions?actiontype=%s&state=%s", ACTION_NOTFOUND, ACTION2_ACTIONSTATE)
//        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
//        .assertResponseLengthIs(-1)
//        .andClose();
//  }
//
//  /**
//   * Test requesting Actions filtered by action type name found.
//   */
//  @Test
//  public void findActionsByActionTypeFound() {
//    with("/actions?actiontype=%s", ACTION2_ACTIONTYPENAME)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
//        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
//        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
//        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
//        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
//        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
//        .assertStringListInBody("$..situation", ACTION2_SITUATION)
//        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
//        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test requesting Actions filtered by action type name not found.
//   */
//  @Test
//  public void findActionsByActionTypeNotFound() {
//    with("/actions?actiontype=%s", ACTION_NOTFOUND)
//        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
//        .assertResponseLengthIs(-1)
//        .andClose();
//  }
//
//  /**
//   * Test requesting Actions filtered by action state found.
//   */
//  @Test
//  public void findActionsByStateFound() {
//    with("/actions?state=%s", ACTION2_ACTIONSTATE.toString())
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
//        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
//        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
//        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
//        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
//        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
//        .assertStringListInBody("$..situation", ACTION2_SITUATION)
//        .assertStringListInBody("$..state", ACTION2_ACTIONSTATE.toString())
//        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test requesting an Action by action Id found.
//   */
//  @Test
//  public void findActionByActionIdFound() {
//    with("/actions/%s", ACTIONID_2.intValue())
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerInBody("$.actionId", ACTIONID_2.intValue())
//        .assertIntegerInBody("$.caseId", ACTION_CASEID)
//        .assertIntegerInBody("$.actionPlanId", ACTION2_PLANID)
//        .assertIntegerInBody("$.actionRuleId", ACTION2_RULEID)
//        .assertStringInBody("$.actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringInBody("$.createdBy", ACTION_CREATEDBY)
//        .assertIntegerInBody("$.priority", ACTION2_PRIORITY)
//        .assertStringInBody("$.situation", ACTION2_SITUATION)
//        .assertStringInBody("$.state", ACTION2_ACTIONSTATE.toString())
//        .assertStringInBody("$.createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test requesting an Action by action Id not found.
//   */
//  @Test
//  public void findActionByActionIdNotFound() {
//    with("/actions/%s", NON_EXISTING_ID)
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
//        .assertTimestampExists()
//        .assertMessageEquals("Action not found for id %s", NON_EXISTING_ID)
//        .andClose();
//  }
//
//  /**
//   * Test requesting Actions by case Id found.
//   */
//  @Test
//  public void findActionsByCaseIdFound() {
//    with("/actions/case/%s", ACTION_CASEID)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerListInBody("$..actionId", new Integer(1), new Integer(2))
//        .assertIntegerListInBody("$..caseId", ACTION_CASEID, ACTION_CASEID)
//        .assertIntegerListInBody("$..actionPlanId", ACTION1_PLANID, ACTION2_PLANID)
//        .assertIntegerListInBody("$..actionRuleId", ACTION1_RULEID, ACTION2_RULEID)
//        .assertStringListInBody("$..actionTypeName", ACTION1_ACTIONTYPENAME, ACTION2_ACTIONTYPENAME)
//        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY, ACTION_CREATEDBY)
//        .assertIntegerListInBody("$..priority", ACTION1_PRIORITY, ACTION2_PRIORITY)
//        .assertStringListInBody("$..situation", ACTION1_SITUATION, ACTION2_SITUATION)
//        .assertStringListInBody("$..state", ACTION1_ACTIONSTATE.toString(), ACTION2_ACTIONSTATE.toString())
//        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE, ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test requesting Actions by case Id not found.
//   */
//  @Test
//  public void findActionByCaseIdNotFound() {
//    with("/actions/case/%s", NON_EXISTING_ID)
//        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
//        .assertResponseLengthIs(-1)
//        .andClose();
//  }
//
//  /**
//   * Test updating action not found
//   */
//  @Test
//  public void updateActionByActionIdNotFound() {
//    with("/actions/%s", NON_EXISTING_ID).put(MediaType.APPLICATION_JSON_TYPE, ACTION_VALIDJSON)
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
//        .andClose();
//  }
//
//  /**
//   * Test requesting an Action creating an Unchecked Exception.
//   */
//  @Test
//  public void findActionByActionIdUnCheckedException() {
//    with("/actions/%s", UNCHECKED_EXCEPTION)
//        .assertResponseCodeIs(HttpStatus.INTERNAL_SERVER_ERROR)
//        .assertFaultIs(CTPException.Fault.SYSTEM_ERROR)
//        .assertTimestampExists()
//        .assertMessageEquals(OUR_EXCEPTION_MESSAGE)
//        .andClose();
//  }
//
//  /**
//   * Test creating an Action with valid JSON.
//   */
//  @Test
//  public void createActionGoodJsonProvided() {
//    with("/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_VALIDJSON)
//        .assertResponseCodeIs(HttpStatus.CREATED)
//        .assertIntegerInBody("$.actionId", ACTIONID_2.intValue())
//        .assertIntegerInBody("$.caseId", ACTION_CASEID)
//        .assertIntegerInBody("$.actionPlanId", ACTION2_PLANID)
//        .assertIntegerInBody("$.actionRuleId", ACTION2_RULEID)
//        .assertStringInBody("$.actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringInBody("$.createdBy", ACTION_CREATEDBY)
//        .assertIntegerInBody("$.priority", ACTION2_PRIORITY)
//        .assertStringInBody("$.situation", ACTION2_SITUATION)
//        .assertStringInBody("$.state", ACTION2_ACTIONSTATE.toString())
//        .assertStringInBody("$.createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test creating an Action with invalid JSON Property.
//   */
//  @Test
//  public void createActionInvalidPropJsonProvided() {
//    with("/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_INVALIDJSON_PROP)
//        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
//        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
//        .assertTimestampExists()
//        .assertMessageEquals("Provided json is incorrect.")
//        .andClose();
//  }
//
//  /**
//   * Test creating an Action with missing JSON Property.
//   */
//  @Test
//  public void createActionMissingPropJsonProvided() {
//    with("/actions").post(MediaType.APPLICATION_JSON_TYPE, ACTION_INVALIDJSON_MISSING_PROP)
//        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
//        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
//        .assertTimestampExists()
//        .assertMessageEquals("Provided json fails validation.")
//        .andClose();
//  }
//
//  /**
//   * Test cancelling an Action.
//   */
//  @Test
//  public void cancelActions() {
//    with("/actions/case/%s/cancel", ACTION_CASEID).put(MediaType.APPLICATION_JSON_TYPE, "")
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerListInBody("$..actionId", ACTIONID_2.intValue())
//        .assertIntegerListInBody("$..caseId", ACTION_CASEID)
//        .assertIntegerListInBody("$..actionPlanId", ACTION2_PLANID)
//        .assertIntegerListInBody("$..actionRuleId", ACTION2_RULEID)
//        .assertStringListInBody("$..actionTypeName", ACTION2_ACTIONTYPENAME)
//        .assertStringListInBody("$..createdBy", ACTION_CREATEDBY)
//        .assertIntegerListInBody("$..priority", ACTION2_PRIORITY)
//        .assertStringListInBody("$..situation", ACTION2_SITUATION)
//        .assertStringListInBody("$..state", ACTION3_ACTIONSTATE.toString())
//        .assertStringListInBody("$..createdDateTime", ACTION_CREATEDDATE_VALUE)
//        .andClose();
//  }
//
//  /**
//   * Test cancelling an Action.
//   */
//  @Test
//  public void cancelActionsCaseNotFound() {
//    with("/actions/case/%s/cancel", NON_EXISTING_ID).put(MediaType.APPLICATION_JSON_TYPE, "")
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .andClose();
//  }
}

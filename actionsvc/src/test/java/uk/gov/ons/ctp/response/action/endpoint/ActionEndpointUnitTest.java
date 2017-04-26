package uk.gov.ons.ctp.response.action.endpoint;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.ons.ctp.common.MvcHelper.getJson;
import static uk.gov.ons.ctp.common.utility.MockMvcControllerAdviceHelper.mockAdviceFor;

import ma.glasnost.orika.MapperFacade;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.common.error.CTPException;
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

  private static final ActionDTO.ActionState ACTION1_ACTIONSTATE = ActionDTO.ActionState.ACTIVE;
  private static final ActionDTO.ActionState ACTION2_ACTIONSTATE = ActionDTO.ActionState.COMPLETED;

  private static final Integer ACTION_CASEID = 124;
  private static final Integer ACTION1_PRIORITY = 1;
  private static final Integer ACTION2_PRIORITY = 3;
  private static final Integer ACTION1_PLANID = 1;
  private static final Integer ACTION2_PLANID = 2;
  private static final Integer ACTION1_RULEID = 1;
  private static final Integer ACTION2_RULEID = 2;
  private static final Integer NON_EXISTING_ID = 998;

  private static final BigInteger ACTIONID_1 = BigInteger.valueOf(1);
  private static final BigInteger ACTIONID_2 = BigInteger.valueOf(2);

  private static final Boolean ACTION1_ACTIONTYPECANCEL = true;
  private static final Boolean ACTION2_ACTIONTYPECANCEL = false;
  private static final Boolean ACTION1_RESPONSEREQUIRED = true;
  private static final Boolean ACTION2_RESPONSEREQUIRED = false;
  private static final Boolean ACTION1_MANUALLY_CREATED = true;
  private static final Boolean ACTION2_MANUALLY_CREATED = false;

  private static final Timestamp ACTION_CREATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  private static final Timestamp ACTION_UPDATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");

  private static final String ACTION1_ACTIONTYPENAME = "actiontypename1";
  private static final String ACTION2_ACTIONTYPENAME = "actiontypename2";
  private static final String ACTION1_ACTIONTYPEDESC = "actiontypedesc1";
  private static final String ACTION2_ACTIONTYPEDESC = "actiontypedesc2";
  private static final String ACTION1_ACTIONTYPEHANDLER = "Field";
  private static final String ACTION2_ACTIONTYPEHANDLER = "Field";
  private static final String ACTION1_SITUATION = "Assigned";
  private static final String ACTION2_SITUATION = "Sent";
  private static final String ACTION_CREATEDBY = "Unit Tester";
  private static final String ACTION_CREATEDDATE_VALUE = "2016-02-26T18:30:00.000+0000";
  private static final String ACTION_NOTFOUND = "NotFound";

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
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActions"));
    actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
    actions.andExpect(jsonPath("$[0].actionId", is(ACTIONID_2.intValue())));
    actions.andExpect(jsonPath("$[0].caseId", is(ACTION_CASEID)));
    actions.andExpect(jsonPath("$[0].actionPlanId", is(ACTION2_PLANID)));
    actions.andExpect(jsonPath("$[0].actionRuleId", is(ACTION2_RULEID)));
    actions.andExpect(jsonPath("$[0].actionTypeName", is(ACTION2_ACTIONTYPENAME)));
    actions.andExpect(jsonPath("$[0].createdBy", is(ACTION_CREATEDBY)));
    actions.andExpect(jsonPath("$[0].priority", is(ACTION2_PRIORITY)));
    actions.andExpect(jsonPath("$[0].situation", is(ACTION2_SITUATION)));
    actions.andExpect(jsonPath("$[0].state", is(ACTION2_ACTIONSTATE.name())));
    // TODO
//    actions.andExpect(jsonPath("$[0].createdDateTime", is(ACTION_CREATEDDATE_TIMESTAMP)));
  }

  /**
   * Test requesting Actions filtered by action type name and state not found.
   */
  @Test
  public void findActionsByActionTypeAndStateNotFound() throws Exception {
    when(actionService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(ACTION_NOTFOUND,
            ACTION2_ACTIONSTATE)).thenReturn(new ArrayList<Action>());

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions?actiontype=%s&state=%s", ACTION_NOTFOUND, ACTION2_ACTIONSTATE)));

    actions.andExpect(status().isNoContent());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActions"));
  }

  /**
   * Test requesting Actions filtered by action type name found.
   */
  @Test
  public void findActionsByActionTypeFound() throws Exception {
    List<Action> result = new ArrayList<Action>();
    ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
    result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION,
            ACTION2_ACTIONSTATE, ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
    when(actionService.findActionsByType(ACTION2_ACTIONTYPENAME)).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions?actiontype=%s", ACTION2_ACTIONTYPENAME)));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActions"));
    actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
    actions.andExpect(jsonPath("$[0].actionId", is(ACTIONID_2.intValue())));
    actions.andExpect(jsonPath("$[0].caseId", is(ACTION_CASEID)));
    actions.andExpect(jsonPath("$[0].actionPlanId", is(ACTION2_PLANID)));
    actions.andExpect(jsonPath("$[0].actionRuleId", is(ACTION2_RULEID)));
    actions.andExpect(jsonPath("$[0].actionTypeName", is(ACTION2_ACTIONTYPENAME)));
    actions.andExpect(jsonPath("$[0].createdBy", is(ACTION_CREATEDBY)));
    actions.andExpect(jsonPath("$[0].priority", is(ACTION2_PRIORITY)));
    actions.andExpect(jsonPath("$[0].situation", is(ACTION2_SITUATION)));
    actions.andExpect(jsonPath("$[0].state", is(ACTION2_ACTIONSTATE.name())));
    // TODO
//    actions.andExpect(jsonPath("$[0].createdDateTime", is(ACTION_CREATEDDATE_TIMESTAMP)));
  }


  /**
   * Test requesting Actions filtered by action type name not found.
   */
  @Test
  public void findActionsByActionTypeNotFound() throws Exception {
    when(actionService.findActionsByType(ACTION_NOTFOUND)).thenReturn(new ArrayList<Action>());

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions?actiontype=%s", ACTION_NOTFOUND)));

    actions.andExpect(status().isNoContent());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActions"));
  }

  /**
   * Test requesting Actions filtered by action state found.
   */
  @Test
  public void findActionsByStateFound() throws Exception {
    List<Action> result = new ArrayList<Action>();
    ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
    result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION,
            ACTION2_ACTIONSTATE, ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
    when(actionService.findActionsByState(ACTION2_ACTIONSTATE)).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions?state=%s", ACTION2_ACTIONSTATE.toString())));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActions"));
    actions.andExpect(jsonPath("$", Matchers.hasSize(1)));
    actions.andExpect(jsonPath("$[0].actionId", is(ACTIONID_2.intValue())));
    actions.andExpect(jsonPath("$[0].caseId", is(ACTION_CASEID)));
    actions.andExpect(jsonPath("$[0].actionPlanId", is(ACTION2_PLANID)));
    actions.andExpect(jsonPath("$[0].actionRuleId", is(ACTION2_RULEID)));
    actions.andExpect(jsonPath("$[0].actionTypeName", is(ACTION2_ACTIONTYPENAME)));
    actions.andExpect(jsonPath("$[0].createdBy", is(ACTION_CREATEDBY)));
    actions.andExpect(jsonPath("$[0].priority", is(ACTION2_PRIORITY)));
    actions.andExpect(jsonPath("$[0].situation", is(ACTION2_SITUATION)));
    actions.andExpect(jsonPath("$[0].state", is(ACTION2_ACTIONSTATE.name())));
    // TODO
//    actions.andExpect(jsonPath("$[0].createdDateTime", is(ACTION_CREATEDDATE_TIMESTAMP)));
  }

  /**
   * Test requesting an Action by action Id found.
   */
  @Test
  public void findActionByActionIdFound() throws Exception {
    ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
    Action action = new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION,
            ACTION2_ACTIONSTATE, ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0);
    when(actionService.findActionByActionId(ACTIONID_2)).thenReturn(action);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions/%s", ACTIONID_2.intValue())));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActionByActionId"));
    actions.andExpect(jsonPath("$.actionId", is(ACTIONID_2.intValue())));
    actions.andExpect(jsonPath("$.caseId", is(ACTION_CASEID)));
    actions.andExpect(jsonPath("$.actionPlanId", is(ACTION2_PLANID)));
    actions.andExpect(jsonPath("$.actionRuleId", is(ACTION2_RULEID)));
    actions.andExpect(jsonPath("$.actionTypeName", is(ACTION2_ACTIONTYPENAME)));
    actions.andExpect(jsonPath("$.createdBy", is(ACTION_CREATEDBY)));
    actions.andExpect(jsonPath("$.priority", is(ACTION2_PRIORITY)));
    actions.andExpect(jsonPath("$.situation", is(ACTION2_SITUATION)));
    actions.andExpect(jsonPath("$.state", is(ACTION2_ACTIONSTATE.name())));
    // TODO
//    actions.andExpect(jsonPath("$[0].createdDateTime", is(ACTION_CREATEDDATE_TIMESTAMP)));
  }

  /**
   * Test requesting an Action by action Id not found.
   */
  @Test
  public void findActionByActionIdNotFound() throws Exception {
    ResultActions actions = mockMvc.perform(getJson(String.format("/actions/%s", NON_EXISTING_ID)));

    actions.andExpect(status().isNotFound());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActionByActionId"));
    actions.andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())));
    actions.andExpect(jsonPath("$.error.message", is(String.format("Action not found for id %s", NON_EXISTING_ID))));
    actions.andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * Test requesting Actions by case Id found.
   */
  @Test
  public void findActionsByCaseIdFound() throws Exception {
    List<Action> result = new ArrayList<Action>();
    ActionType actionType1 = new ActionType(1, ACTION1_ACTIONTYPENAME, ACTION1_ACTIONTYPEDESC,
            ACTION1_ACTIONTYPEHANDLER, ACTION1_ACTIONTYPECANCEL, ACTION1_RESPONSEREQUIRED);
    ActionType actionType2 = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
    result.add(new Action(ACTIONID_1, ACTION_CASEID, ACTION1_PLANID, ACTION1_RULEID, ACTION_CREATEDBY,
            ACTION1_MANUALLY_CREATED, actionType1, ACTION1_PRIORITY, ACTION1_SITUATION, ACTION1_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
    result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType2, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
    when(actionService.findActionsByCaseId(ACTION_CASEID)).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actions/case/%s", ACTION_CASEID)));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionEndpoint.class));
    actions.andExpect(handler().methodName("findActionsByCaseId"));
    actions.andExpect(jsonPath("$", Matchers.hasSize(2)));
    actions.andExpect(jsonPath("$[*].actionId", containsInAnyOrder(new Integer(1), new Integer(2))));
    actions.andExpect(jsonPath("$[*].caseId", containsInAnyOrder(ACTION_CASEID, ACTION_CASEID)));
    actions.andExpect(jsonPath("$[*].actionPlanId", containsInAnyOrder(ACTION1_PLANID, ACTION2_PLANID)));
    actions.andExpect(jsonPath("$[*].actionRuleId", containsInAnyOrder(ACTION1_RULEID, ACTION2_RULEID)));
    actions.andExpect(jsonPath("$[*].actionTypeName", containsInAnyOrder(ACTION1_ACTIONTYPENAME, ACTION2_ACTIONTYPENAME)));
    actions.andExpect(jsonPath("$[*].createdBy", containsInAnyOrder(ACTION_CREATEDBY, ACTION_CREATEDBY)));
    actions.andExpect(jsonPath("$[*].priority", containsInAnyOrder(ACTION1_PRIORITY, ACTION2_PRIORITY)));
    actions.andExpect(jsonPath("$[*].situation", containsInAnyOrder(ACTION1_SITUATION, ACTION2_SITUATION)));
    actions.andExpect(jsonPath("$[*].state", containsInAnyOrder(ACTION1_ACTIONSTATE.name(), ACTION2_ACTIONSTATE.name())));
    // TODO
//    actions.andExpect(jsonPath("$[*].createdDateTime", containsInAnyOrder(ACTION_CREATEDDATE_TIMESTAMP)));
  }
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

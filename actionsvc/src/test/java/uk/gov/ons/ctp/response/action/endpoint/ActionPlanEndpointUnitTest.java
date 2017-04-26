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
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for ActionPlan endpoint
 */
public class ActionPlanEndpointUnitTest {
  private static final Integer ACTIONPLANID = 3;
  private static final Integer ACTIONPLAN_SURVEYID = 1;
  private static final Integer NON_EXISTING_ACTIONPLANID = 998;
  private static final Integer UNCHECKED_EXCEPTION = 999;

  private static final String ACTIONPLAN1_NAME = "HH_APL1";
  private static final String ACTIONPLAN2_NAME = "HGH_APL1";
  private static final String ACTIONPLAN3_NAME = "CH_APL1";
  private static final String ACTIONPLAN1_DESC = "Household Action Plan 1";
  private static final String ACTIONPLAN2_DESC = "Hotel and Guest House Action Plan 1";
  private static final String ACTIONPLAN3_DESC = "Care Home Action Plan 1";
  private static final String CREATED_BY = "whilep1";
  private static final String LAST_RUN_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String OUR_EXCEPTION_MESSAGE = "this is what we throw";

  private static final String ACTIONPLAN_JSON = "{\"actionPlanId\":21,\"surveyId\":1,\"name\":\"HH\","
      + "\"description\":\"philippetesting\",\"createdBy\":\"SYSTEM\","
      + "\"lastRunDateTime\":null}";
  private static final String ACTIONPLAN_INVALIDJSON = "{\"some\":\"joke\"}";

  private static final Timestamp ACTIONPLAN_LAST_GOOD_RUN_DATE_TIMESTAMP = Timestamp
          .valueOf("2016-03-09 11:15:48.023286");

  @InjectMocks
  private ActionPlanEndpoint actionPlanEndpoint;

  @Mock
  private ActionPlanService actionPlanService;

  @Spy
  private MapperFacade mapperFacade = new ActionBeanMapper();

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders
            .standaloneSetup(actionPlanEndpoint)
            .setHandlerExceptionResolvers(mockAdviceFor(RestExceptionHandler.class))
            .build();
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlansFound() throws Exception {
    List<ActionPlan> result = new ArrayList<>();
    result.add(new ActionPlan(1, ACTIONPLAN_SURVEYID, ACTIONPLAN1_NAME, ACTIONPLAN1_DESC, CREATED_BY,
            ACTIONPLAN_LAST_GOOD_RUN_DATE_TIMESTAMP));
    result.add(new ActionPlan(2, ACTIONPLAN_SURVEYID, ACTIONPLAN2_NAME, ACTIONPLAN2_DESC, CREATED_BY,
            ACTIONPLAN_LAST_GOOD_RUN_DATE_TIMESTAMP));
    result.add(new ActionPlan(3, ACTIONPLAN_SURVEYID, ACTIONPLAN3_NAME, ACTIONPLAN3_DESC, CREATED_BY,
            ACTIONPLAN_LAST_GOOD_RUN_DATE_TIMESTAMP));
    when(actionPlanService.findActionPlans()).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson("/actionplans"));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionPlanEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlans"));
    actions.andExpect(jsonPath("$", Matchers.hasSize(3)));
    actions.andExpect(jsonPath("$[*].actionPlanId", containsInAnyOrder(1, 2, 3)));
    actions.andExpect(jsonPath("$[*].surveyId", containsInAnyOrder(ACTIONPLAN_SURVEYID, ACTIONPLAN_SURVEYID, ACTIONPLAN_SURVEYID)));
    actions.andExpect(jsonPath("$[*].name", containsInAnyOrder(ACTIONPLAN1_NAME, ACTIONPLAN2_NAME, ACTIONPLAN3_NAME)));
    actions.andExpect(jsonPath("$[*].description", containsInAnyOrder(ACTIONPLAN1_DESC, ACTIONPLAN2_DESC, ACTIONPLAN3_DESC)));
    actions.andExpect(jsonPath("$[*].createdBy", containsInAnyOrder(CREATED_BY, CREATED_BY, CREATED_BY)));
//    TODO actions.andExpect(jsonPath("$[*].lastRunDateTime", containsInAnyOrder( LAST_RUN_DATE_TIME, LAST_RUN_DATE_TIME, LAST_RUN_DATE_TIME)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanFound() throws Exception {
    when(actionPlanService.findActionPlan(ACTIONPLANID)).thenReturn(new ActionPlan(ACTIONPLANID, ACTIONPLAN_SURVEYID, ACTIONPLAN3_NAME, ACTIONPLAN3_DESC, CREATED_BY,
            ACTIONPLAN_LAST_GOOD_RUN_DATE_TIMESTAMP));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/%s", ACTIONPLANID)));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionPlanEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanByActionPlanId"));
    actions.andExpect(jsonPath("$.actionPlanId", is(ACTIONPLANID)));
    actions.andExpect(jsonPath("$.surveyId", is(ACTIONPLAN_SURVEYID)));
    actions.andExpect(jsonPath("$.name", is(ACTIONPLAN3_NAME)));
    actions.andExpect(jsonPath("$.description", is(ACTIONPLAN3_DESC)));
    actions.andExpect(jsonPath("$.createdBy", is(CREATED_BY)));
    // TODO actions.andExpect(jsonPath("$.lastRunDateTime", is(LAST_RUN_DATE_TIME)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanNotFound() throws Exception {
    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/%s", NON_EXISTING_ACTIONPLANID)));

    actions.andExpect(status().isNotFound());
    actions.andExpect(handler().handlerType(ActionPlanEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanByActionPlanId"));
    actions.andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())));
    actions.andExpect(jsonPath("$.error.message", isA(String.class)));
    actions.andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanUnCheckedException() throws Exception {
    when(actionPlanService.findActionPlan(UNCHECKED_EXCEPTION))
            .thenThrow(new IllegalArgumentException(OUR_EXCEPTION_MESSAGE));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/%s", UNCHECKED_EXCEPTION)));

    actions.andExpect(status().is5xxServerError());
    actions.andExpect(handler().handlerType(ActionPlanEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanByActionPlanId"));
    actions.andExpect(jsonPath("$.error.code", is(CTPException.Fault.SYSTEM_ERROR.name())));
    actions.andExpect(jsonPath("$.error.message", is(OUR_EXCEPTION_MESSAGE)));
    actions.andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void findActionRulesForActionPlanFound() {
//    with("/actionplans/%s/rules", ACTIONPLANID)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertArrayLengthInBodyIs(3)
//        .assertIntegerListInBody("$..actionRuleId", 1, 2, 3)
//        .assertIntegerListInBody("$..actionPlanId", ACTIONPLANID, ACTIONPLANID, ACTIONPLANID)
//        .assertIntegerListInBody("$..priority", ACTIONRULE_PRIORITY, ACTIONRULE_PRIORITY, ACTIONRULE_PRIORITY)
//        .assertIntegerListInBody("$..surveyDateDaysOffset", ACTIONRULE_SURVEYDATEDAYSOFFSET,
//            ACTIONRULE_SURVEYDATEDAYSOFFSET, ACTIONRULE_SURVEYDATEDAYSOFFSET)
//        .assertStringListInBody("$..actionTypeName", ACTIONRULE_ACTIONTYPENAME, ACTIONRULE_ACTIONTYPENAME,
//            ACTIONRULE_ACTIONTYPENAME)
//        .assertStringListInBody("$..name", ACTIONRULE_NAME, ACTIONRULE_NAME, ACTIONRULE_NAME)
//        .assertStringListInBody("$..description", ACTIONRULE_DESCRIPTION, ACTIONRULE_DESCRIPTION,
//            ACTIONRULE_DESCRIPTION)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void findNoActionRulesForActionPlan() {
//    with("/actionplans/%s/rules", ACTIONPLANID_WITHNOACTIONRULE)
//        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void findActionRulesForNonExistingActionPlan() {
//    with("/actionplans/%s/rules", NON_EXISTING_ACTIONPLANID)
//        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
//        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
//        .assertTimestampExists()
//        .andClose();
//  }
//
//
//
//  /**
//   * A Test
//   */
//  @Test
//  public void updateActionPlanNegativeScenarioInvalidJsonProvided() {
//    with("/actionplans/%s", ACTIONPLANID)
//        .put(MediaType.APPLICATION_JSON_TYPE, ACTIONPLAN_INVALIDJSON)
//        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
//        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
//        .assertTimestampExists()
//        .assertMessageEquals(PROVIDED_JSON_INCORRECT)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void updateActionPlanHappyScenario() {
//    with("/actionplans/%s", ACTIONPLANID).put(MediaType.APPLICATION_JSON_TYPE, ACTIONPLAN_JSON)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertIntegerInBody("$.actionPlanId", ACTIONPLANID)
//        .assertIntegerInBody("$.surveyId", ACTIONPLAN_SURVEYID)
//        .assertStringInBody("$.name", ACTIONPLAN3_NAME)
//        .assertStringInBody("$.description", ACTIONPLAN3_DESC)
//        .assertStringInBody("$.createdBy", CREATED_BY)
//        .assertStringInBody("$.lastRunDateTime", LAST_RUN_DATE_TIME)
//        .andClose();
//  }

}

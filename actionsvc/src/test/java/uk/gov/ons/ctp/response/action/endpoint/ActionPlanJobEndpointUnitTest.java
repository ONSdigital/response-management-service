package uk.gov.ons.ctp.response.action.endpoint;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import java.sql.Timestamp;
import java.util.Optional;

public class ActionPlanJobEndpointUnitTest {

  private static final Integer ACTIONPLANJOBID = 1;
  private static final Integer ACTIONPLANJOBID_ACTIONPLANID = 1;
  private static final Integer ACTIONPLANID = 1;
  private static final Integer ACTIONPLANID_WITHNOACTIONPLANJOB = 13;
  private static final Integer NON_EXISTING_ACTIONPLANJOBID = 998;
  private static final Integer UNCHECKED_EXCEPTION_ACTIONPLANJOBID = 999;

  private static final String ACTIONPLANJOBID_CREATED_BY = "theTester";
  private static final String ACTIONPLANJOB_INVALIDJSON = "{\"createdBy\":\"\"}";
  private static final String ACTIONPLANJOB_VALIDJSON = "{\"createdBy\":\"unittest\"}";
  private static final String CREATED_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String UPDATED_DATE_TIME = "2016-04-09T11:15:48.023+0000";
  private static final String OUR_EXCEPTION_MESSAGE = "this is what we throw";

  private static final ActionPlanJobDTO.ActionPlanJobState
          ACTIONPLANJOBID_STATE = ActionPlanJobDTO.ActionPlanJobState.SUBMITTED;

  private static final Timestamp ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP = Timestamp
          .valueOf("2016-03-09 11:15:48.023286");
  private static final Timestamp ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP = Timestamp
          .valueOf("2016-04-09 11:15:48.023286");

  @InjectMocks
  private ActionPlanJobEndpoint actionPlanJobEndpoint;

  @Mock
  private ActionPlanJobService actionPlanJobService;

  @Spy
  private MapperFacade mapperFacade = new ActionBeanMapper();

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders
            .standaloneSetup(actionPlanJobEndpoint)
            .setHandlerExceptionResolvers(mockAdviceFor(RestExceptionHandler.class))
            .build();
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanJobFound() throws Exception {
    when(actionPlanJobService.findActionPlanJob(ACTIONPLANJOBID)).thenReturn(Optional.of(new ActionPlanJob(ACTIONPLANJOBID, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP)));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", ACTIONPLANJOBID)));

    actions.andExpect(status().isOk());
    actions.andExpect(handler().handlerType(ActionPlanJobEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanJobById"));
    actions.andExpect(jsonPath("$.actionPlanJobId", is(ACTIONPLANJOBID)));
    actions.andExpect(jsonPath("$.actionPlanId", is(ACTIONPLANJOBID_ACTIONPLANID)));
    actions.andExpect(jsonPath("$.createdBy", is(ACTIONPLANJOBID_CREATED_BY)));
    actions.andExpect(jsonPath("$.state", is(ACTIONPLANJOBID_STATE.name())));
// TODO   actions.andExpect(jsonPath("$.createdDateTime", is(CREATED_DATE_TIME)));
// TODO   actions.andExpect(jsonPath("$.updatedDateTime", is(UPDATED_DATE_TIME)));
  }


  /**
   * A Test
   */
  @Test
  public void findActionPlanJobNotFound() throws Exception {
    when(actionPlanJobService.findActionPlanJob(NON_EXISTING_ACTIONPLANJOBID)).thenReturn(Optional.empty());

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", NON_EXISTING_ACTIONPLANJOBID)));

    actions.andExpect(status().isNotFound());
    actions.andExpect(handler().handlerType(ActionPlanJobEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanJobById"));
    actions.andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())));
    actions.andExpect(jsonPath("$.error.message", isA(String.class)));
    actions.andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanUnCheckedException() throws Exception {
    when(actionPlanJobService.findActionPlanJob(UNCHECKED_EXCEPTION_ACTIONPLANJOBID)).thenThrow(new IllegalArgumentException(OUR_EXCEPTION_MESSAGE));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", UNCHECKED_EXCEPTION_ACTIONPLANJOBID)));

    actions.andExpect(status().is5xxServerError());
    actions.andExpect(handler().handlerType(ActionPlanJobEndpoint.class));
    actions.andExpect(handler().methodName("findActionPlanJobById"));
    actions.andExpect(jsonPath("$.error.code", is(CTPException.Fault.SYSTEM_ERROR.name())));
    actions.andExpect(jsonPath("$.error.message", is(OUR_EXCEPTION_MESSAGE)));
    actions.andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

//  /**
//   * A Test
//   */
//  @Test
//  public void findNoActionPlanJobForActionPlan() throws Exception {
//    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", ACTIONPLANID_WITHNOACTIONPLANJOB)));
//
//    with("/actionplans/%s/jobs", ACTIONPLANID_WITHNOACTIONPLANJOB)
//        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void findActionPlanJobsForActionPlan() {
//    with("/actionplans/%s/jobs", ACTIONPLANID)
//        .assertResponseCodeIs(HttpStatus.OK)
//        .assertArrayLengthInBodyIs(3)
//        .assertIntegerListInBody("$..actionPlanJobId", 1, 2, 3)
//        .assertIntegerListInBody("$..actionPlanId", ACTIONPLANID, ACTIONPLANID, ACTIONPLANID)
//        .assertStringListInBody("$..createdBy", ACTIONPLANJOBID_CREATED_BY, ACTIONPLANJOBID_CREATED_BY,
//            ACTIONPLANJOBID_CREATED_BY)
//        .assertStringListInBody("$..createdDateTime", CREATED_DATE_TIME, CREATED_DATE_TIME, CREATED_DATE_TIME)
//        .assertStringListInBody("$..updatedDateTime", UPDATED_DATE_TIME, UPDATED_DATE_TIME, UPDATED_DATE_TIME)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void executeActionPlanBadJsonProvided() {
//    with("/actionplans/%s/jobs", ACTIONPLANID)
//        .post(MediaType.APPLICATION_JSON_TYPE, ACTIONPLANJOB_INVALIDJSON)
//        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
//        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
//        .assertTimestampExists()
//        .assertMessageEquals(PROVIDED_JSON_INVALID)
//        .andClose();
//  }
//
//  /**
//   * A Test
//   */
//  @Test
//  public void executeActionPlanGoodJsonProvided() {
//    with("/actionplans/%s/jobs", ACTIONPLANID)
//        .post(MediaType.APPLICATION_JSON_TYPE, ACTIONPLANJOB_VALIDJSON)
//        .assertResponseCodeIs(HttpStatus.CREATED)
//        .assertIntegerInBody("$.actionPlanJobId", ACTIONPLANJOBID)
//        .assertIntegerInBody("$.actionPlanId", ACTIONPLANJOBID_ACTIONPLANID)
//        .assertStringInBody("$.createdBy", ACTIONPLANJOBID_CREATED_BY)
//        .assertStringInBody("$.state", ACTIONPLANJOBID_STATE.name())
//        .assertStringInBody("$.createdDateTime", CREATED_DATE_TIME)
//        .assertStringInBody("$.updatedDateTime", UPDATED_DATE_TIME)
//        .andClose();
//  }

}

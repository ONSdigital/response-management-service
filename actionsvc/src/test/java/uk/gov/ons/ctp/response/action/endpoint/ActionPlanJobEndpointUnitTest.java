package uk.gov.ons.ctp.response.action.endpoint;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.ons.ctp.common.MvcHelper.getJson;
import static uk.gov.ons.ctp.common.MvcHelper.postJson;
import static uk.gov.ons.ctp.common.utility.MockMvcControllerAdviceHelper.mockAdviceFor;
import static uk.gov.ons.ctp.common.error.RestExceptionHandler.INVALID_JSON;

import ma.glasnost.orika.MapperFacade;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.error.RestExceptionHandler;
import uk.gov.ons.ctp.common.jackson.CustomObjectMapper;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionPlanJobEndpointUnitTest {

  private static final Integer ACTIONPLANJOBID = 1;
  private static final Integer ACTIONPLANJOBID_ACTIONPLANID = 1;
  private static final Integer ACTIONPLANID = 1;
  private static final Integer NON_EXISTING_ACTIONPLANJOBID = 998;
  private static final Integer UNCHECKED_EXCEPTION_ACTIONPLANJOBID = 999;

  private static final String ACTIONPLANJOBID_CREATED_BY = "theTester";
  private static final String ACTIONPLANJOB_INVALIDJSON = "{\"createdBy\":\"\"}";
  private static final String ACTIONPLANJOB_VALIDJSON = "{\"createdBy\":\"unittest\"}";
  private static final String CREATED_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String UPDATED_DATE_TIME = "2016-04-09T11:15:48.023+0100";
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
            .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
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

    actions.andExpect(status().isOk())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("findActionPlanJobById"))
            .andExpect(jsonPath("$.actionPlanJobId", is(ACTIONPLANJOBID)))
            .andExpect(jsonPath("$.actionPlanId", is(ACTIONPLANJOBID_ACTIONPLANID)))
            .andExpect(jsonPath("$.createdBy", is(ACTIONPLANJOBID_CREATED_BY)))
            .andExpect(jsonPath("$.state", is(ACTIONPLANJOBID_STATE.name())))
            .andExpect(jsonPath("$.createdDateTime", is(CREATED_DATE_TIME)))
            .andExpect(jsonPath("$.updatedDateTime", is(UPDATED_DATE_TIME)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanJobNotFound() throws Exception {
    when(actionPlanJobService.findActionPlanJob(NON_EXISTING_ACTIONPLANJOBID)).thenReturn(Optional.empty());

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", NON_EXISTING_ACTIONPLANJOBID)));

    actions.andExpect(status().isNotFound())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("findActionPlanJobById"))
            .andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())))
            .andExpect(jsonPath("$.error.message", isA(String.class)))
            .andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanUnCheckedException() throws Exception {
    when(actionPlanJobService.findActionPlanJob(UNCHECKED_EXCEPTION_ACTIONPLANJOBID)).thenThrow(new IllegalArgumentException(OUR_EXCEPTION_MESSAGE));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/jobs/%s", UNCHECKED_EXCEPTION_ACTIONPLANJOBID)));

    actions.andExpect(status().is5xxServerError())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("findActionPlanJobById"))
            .andExpect(jsonPath("$.error.code", is(CTPException.Fault.SYSTEM_ERROR.name())))
            .andExpect(jsonPath("$.error.message", is(OUR_EXCEPTION_MESSAGE)))
            .andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * A Test
   */
  @Test
  public void findActionPlanJobsForActionPlan() throws Exception {
    List<ActionPlanJob> result = new ArrayList<>();
    result.add(new ActionPlanJob(1, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP));
    result.add(new ActionPlanJob(2, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP));
    result.add(new ActionPlanJob(3, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP));
    when(actionPlanJobService.findActionPlanJobsForActionPlan(ACTIONPLANID)).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionplans/%s/jobs", ACTIONPLANID)));

    actions.andExpect(status().isOk())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("findAllActionPlanJobsByActionPlanId"))
            .andExpect(jsonPath("$", Matchers.hasSize(3)))
            .andExpect(jsonPath("$[*].actionPlanJobId", containsInAnyOrder(1, 2, 3)))
            .andExpect(jsonPath("$[*].actionPlanId", containsInAnyOrder(ACTIONPLANID, ACTIONPLANID, ACTIONPLANID)))
            .andExpect(jsonPath("$[*].createdBy", containsInAnyOrder(ACTIONPLANJOBID_CREATED_BY, ACTIONPLANJOBID_CREATED_BY, ACTIONPLANJOBID_CREATED_BY)))
            .andExpect(jsonPath("$[*].createdDateTime", containsInAnyOrder(CREATED_DATE_TIME, CREATED_DATE_TIME, CREATED_DATE_TIME)))
            .andExpect(jsonPath("$[*].updatedDateTime", containsInAnyOrder(UPDATED_DATE_TIME, UPDATED_DATE_TIME, UPDATED_DATE_TIME)));
  }

  /**
   * A Test
   */
  @Test
  public void executeActionPlanBadJsonProvided() throws Exception {
    ResultActions actions = mockMvc.perform(postJson(String.format("/actionplans/%s/jobs", ACTIONPLANID), ACTIONPLANJOB_INVALIDJSON));

    actions.andExpect(status().isBadRequest())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("executeActionPlan"))
            .andExpect(jsonPath("$.error.code", is(CTPException.Fault.VALIDATION_FAILED.name())))
            .andExpect(jsonPath("$.error.message", is(INVALID_JSON)))
            .andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  /**
   * A Test
   */
  @Test
  public void executeActionPlanGoodJsonProvided() throws Exception {
    when(actionPlanJobService.createAndExecuteActionPlanJob(any(ActionPlanJob.class))).thenReturn(Optional.of(new ActionPlanJob(ACTIONPLANJOBID, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP)));

    ResultActions actions = mockMvc.perform(postJson(String.format("/actionplans/%s/jobs", ACTIONPLANID), ACTIONPLANJOB_VALIDJSON));

    actions.andExpect(status().isCreated())
            .andExpect(handler().handlerType(ActionPlanJobEndpoint.class))
            .andExpect(handler().methodName("executeActionPlan"))
            .andExpect(jsonPath("$.actionPlanJobId", is(ACTIONPLANJOBID)))
            .andExpect(jsonPath("$.actionPlanId", is(ACTIONPLANJOBID_ACTIONPLANID)))
            .andExpect(jsonPath("$.createdBy", is(ACTIONPLANJOBID_CREATED_BY)))
            .andExpect(jsonPath("$.state", is(ACTIONPLANJOBID_STATE.name())))
            .andExpect(jsonPath("$.createdDateTime", is(CREATED_DATE_TIME)))
            .andExpect(jsonPath("$.updatedDateTime", is(UPDATED_DATE_TIME)));
  }

}

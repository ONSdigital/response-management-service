package uk.gov.ons.ctp.response.action.export.endpoint;

import ma.glasnost.orika.MapperFacade;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.error.RestExceptionHandler;
import uk.gov.ons.ctp.common.jackson.CustomObjectMapper;
import uk.gov.ons.ctp.response.action.ActionExporterBeanMapper;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.ExportMessage;
import uk.gov.ons.ctp.response.action.export.message.SftpServicePublisher;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;
import uk.gov.ons.ctp.response.action.export.service.TransformationService;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import static uk.gov.ons.ctp.response.action.export.endpoint.ActionRequestEndpoint.ACTION_REQUEST_NOT_FOUND;

/**
 * ActionRequestEndpoint unit tests
 */
public class ActionRequestEndpointTest {

  private final static int NON_EXISTING_ACTION_ID = 1;
  private final static int EXISTING_ACTION_ID = 2;

  @InjectMocks
  private ActionRequestEndpoint actionRequestEndpoint;

  @Mock
  private ActionRequestService actionRequestService;

  @Mock
  private TransformationService transformationService;

  @Mock
  private SftpServicePublisher sftpService;

  @Spy
  private MapperFacade mapperFacade = new ActionExporterBeanMapper();

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = MockMvcBuilders
            .standaloneSetup(actionRequestEndpoint)
            .setHandlerExceptionResolvers(mockAdviceFor(RestExceptionHandler.class))
            .setMessageConverters(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()))
            .build();
  }

  @Test
  public void findAllActionRequests() throws Exception {
    List<ActionRequestInstruction> result = new ArrayList<>();
    for (int i = 0; i < 3; i++){
      result.add(buildActionRequest(i));
    }
    when(actionRequestService.retrieveAllActionRequests()).thenReturn(result);

    ResultActions actions = mockMvc.perform(getJson("/actionrequests/"));

    actions.andExpect(status().isOk())
            .andExpect(handler().handlerType(ActionRequestEndpoint.class))
            .andExpect(handler().methodName("findAllActionRequests"))
            .andExpect(jsonPath("$", Matchers.hasSize(3))).andExpect(jsonPath("$[*].actionId", containsInAnyOrder(new Integer(0), new Integer(1), new Integer(2))));
  }

  @Test
  public void findNonExistingActionRequest() throws Exception {
    ResultActions actions = mockMvc.perform(getJson(String.format("/actionrequests/%s", NON_EXISTING_ACTION_ID)));

    actions.andExpect(status().isNotFound())
            .andExpect(handler().handlerType(ActionRequestEndpoint.class))
            .andExpect(handler().methodName("findActionRequest"))
            .andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())))
            .andExpect(jsonPath("$.error.message", is(String.format("%s %d", ACTION_REQUEST_NOT_FOUND, NON_EXISTING_ACTION_ID))))
            .andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  @Test
  public void findExistingActionRequest() throws Exception {
    when(actionRequestService.retrieveActionRequest(BigInteger.valueOf(EXISTING_ACTION_ID))).thenReturn(buildActionRequest(EXISTING_ACTION_ID));

    ResultActions actions = mockMvc.perform(getJson(String.format("/actionrequests/%s", EXISTING_ACTION_ID)));

    actions.andExpect(status().isOk())
            .andExpect(handler().handlerType(ActionRequestEndpoint.class))
            .andExpect(handler().methodName("findActionRequest"))
            .andExpect(jsonPath("$.actionId", is(EXISTING_ACTION_ID)));
  }

  @Test
  public void exportNonExistingActionRequest() throws Exception {
    ResultActions actions = mockMvc.perform(postJson(String.format("/actionrequests/%s", NON_EXISTING_ACTION_ID), ""));

    actions.andExpect(status().isNotFound())
            .andExpect(handler().handlerType(ActionRequestEndpoint.class))
            .andExpect(handler().methodName("export"))
            .andExpect(jsonPath("$.error.code", is(CTPException.Fault.RESOURCE_NOT_FOUND.name())))
            .andExpect(jsonPath("$.error.message", is(String.format("%s %d", ACTION_REQUEST_NOT_FOUND, NON_EXISTING_ACTION_ID))))
            .andExpect(jsonPath("$.error.timestamp", isA(String.class)));
  }

  @Test
  public void exportExistingActionRequest() throws Exception {
    when(actionRequestService.retrieveActionRequest(BigInteger.valueOf(EXISTING_ACTION_ID))).thenReturn(buildActionRequest(EXISTING_ACTION_ID));
    when(transformationService.processActionRequest(any(ExportMessage.class), any(ActionRequestInstruction.class))).thenAnswer(new Answer<ExportMessage>() {
      public ExportMessage answer(final InvocationOnMock invocation) throws Throwable {
        Object[] args = invocation.getArguments();
        return buildSftpMessage((ExportMessage) args[0]);
      }
    });
    when(sftpService.sendMessage(any(String.class), any(), any())).thenAnswer(new Answer<byte[]>() {
      public byte[] answer(final InvocationOnMock invocation) throws Throwable {
        return "Any string".getBytes();
      }
    });

    ResultActions actions = mockMvc.perform(postJson(String.format("/actionrequests/%s", EXISTING_ACTION_ID), ""));

    actions.andExpect(status().isCreated())
            .andExpect(handler().handlerType(ActionRequestEndpoint.class))
            .andExpect(handler().methodName("export"))
            .andExpect(jsonPath("$.actionId", is(EXISTING_ACTION_ID)));
  }

  private static ActionRequestInstruction buildActionRequest(int actionId) {
    ActionRequestInstruction actionRequest = new ActionRequestInstruction();
    actionRequest.setActionId(BigInteger.valueOf(actionId));
    return actionRequest;
  }

  private ExportMessage buildSftpMessage(ExportMessage message) {
    message.getActionRequestIds().put("dummy", Collections.singletonList("12345"));
    message.getOutputStreams().put("dummy", new ByteArrayOutputStream());
    return message;
  }
}

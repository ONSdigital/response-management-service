package uk.gov.ons.ctp.response.action.service.impl;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.config.CaseSvc;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.casesvc.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.casesvc.representation.CategoryDTO;

/**
 * A test of the case frame service client service
 */
@RunWith(MockitoJUnitRunner.class)
public class CaseSvcClientServiceImplTest {

  @Mock
  Tracer tracer;
  @Mock
  Span span;

  @Mock
  private AppConfig appConfig;

  @Spy
  private RestClient restClient = new RestClient();

  @InjectMocks
  private CaseSvcClientServiceImpl caseSvcClientService;

  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(tracer.getCurrentSpan()).thenReturn(span);
    Mockito.when(tracer.createSpan(any(String.class))).thenReturn(span);
    restClient.setTracer(tracer);
  }


  /**
   * Yep - another test
   */
  @Test
  public void testCreateNewCaseEvent() {
    CaseSvc caseSvcConfig = new CaseSvc();
    caseSvcConfig.setCaseEventsByCasePostPath("cases/{caseid}/events");
    Mockito.when(appConfig.getCaseSvc()).thenReturn(caseSvcConfig);
    RestTemplate restTemplate = this.restClient.getRestTemplate();

    Action action = new Action();
    action.setActionId(BigInteger.valueOf(1));
    action.setActionPlanId(2);
    action.setActionRuleId(3);
    
    ActionType actionType = new ActionType();
    actionType.setActionTypeId(4);
    actionType.setHandler("Field");
    actionType.setName("HouseholdVisit");
    actionType.setDescription("desc");
    action.setActionType(actionType);
    action.setCreatedBy("me");
    action.setCaseId(5);
    action.setSituation("situ");

    MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
    mockServer.expect(requestTo("http://localhost:8080/cases/5/events"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().string(containsString("\"caseId\":" + action.getCaseId() + ",")))
        .andExpect(content().string(containsString("\"caseEventId\":null,")))
        .andExpect(content()
            .string(containsString("\"category\":\"" + CategoryDTO.CategoryType.ACTION_COMPLETED.name() + "\"")))
        .andExpect(content().string(containsString("\"subCategory\":\"" + action.getActionType().getName() + "\"")))
        .andExpect(content().string(containsString("\"createdBy\":\"" + action.getCreatedBy() + "\"")))
        .andExpect(content().string(containsString(
        .andRespond(withSuccess("{"
            + "\"createdDateTime\":1460736159699,"
            + "\"caseEventId\":1,"
            + "\"caseId\":1,"
            + "\"category\":\"ACTION_COMPLETED\","
            + "\"subCategory\":\"subcat\","
            + "\"createdBy\":\"me\","
            + "\"description\":\"desc\""
            + "}", MediaType.APPLICATION_JSON));

    CaseEventDTO caseEventDTO = caseSvcClientService.createNewCaseEvent(action,
        CategoryDTO.CategoryType.ACTION_COMPLETED);
    assertTrue(caseEventDTO != null);
    mockServer.verify();
  }
  
	  
}

package uk.gov.ons.ctp.response.action.service.impl;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import uk.gov.ons.ctp.common.rest.RestClient;
import uk.gov.ons.ctp.response.action.config.AppConfig;
import uk.gov.ons.ctp.response.action.config.CaseFrameSvc;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.caseframe.representation.CaseEventDTO;
import uk.gov.ons.ctp.response.caseframe.representation.CategoryDTO;

/**
 * A test of the case frame service client service
 */
@RunWith(MockitoJUnitRunner.class)
public class CaseFrameSvcClientServiceImplTest {

  @Mock
  private AppConfig appConfig;

  @Spy
  private RestClient restClient = new RestClient("http", "localhost", "8080");

  @InjectMocks
  private CaseFrameSvcClientServiceImpl caseFrameSvcClientService;

  /**
   * Guess what? - a test!
   */
  @Test
  public void testGetOpenCasesForActionPlan() {
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();
    caseFrameSvcConfig.setCaseByStatusAndActionPlanPath("/cases/actionplan/{actionplanid}");
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);
    RestTemplate restTemplate = this.restClient.getRestTemplate();

    MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
    mockServer.expect(requestTo("http://localhost:8080/cases/actionplan/1?state=INIT&state=RESPONDED"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("[1,2,3]", MediaType.APPLICATION_JSON));

    List<Integer> cases = caseFrameSvcClientService.getOpenCasesForActionPlan(1);
    assertTrue(cases != null);
    assertTrue(cases.containsAll(Arrays.asList(new Integer[] {1, 2, 3})));
    mockServer.verify();
  }


  /**
   * Yep - another test
   */
  @Test
  public void testCreateNewCaseEvent() {
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();
    caseFrameSvcConfig.setCaseEventsByCasePostPath("cases/{caseid}/events");
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);
    RestTemplate restTemplate = this.restClient.getRestTemplate();

    Action action = new Action();
    action.setActionId(1);
    action.setActionPlanId(2);
    action.setActionRuleId(3);
    ;
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
            .string(containsString("\"category\":\"" + CategoryDTO.CategoryName.ACTION_COMPLETED.getLabel() + "\"")))
        .andExpect(content().string(containsString("\"subCategory\":\"" + action.getActionType().getName() + "\"")))
        .andExpect(content().string(containsString("\"createdBy\":\"" + action.getCreatedBy() + "\"")))
        .andExpect(content().string(containsString(
            "\"description\":\"" + action.getActionType().getDescription() + " (" + action.getSituation() + ")\"")))
        .andRespond(withSuccess("{"
            + "\"createdDateTime\":1460736159699,"
            + "\"caseEventId\":1,"
            + "\"caseId\":1,"
            + "\"category\":\"cat\","
            + "\"subCategory\":\"subcat\","
            + "\"createdBy\":\"me\","
            + "\"description\":\"desc\""
            + "}", MediaType.APPLICATION_JSON));

    CaseEventDTO caseEventDTO = caseFrameSvcClientService.createNewCaseEvent(action,
        CategoryDTO.CategoryName.ACTION_COMPLETED);
    assertTrue(caseEventDTO != null);
    mockServer.verify();
  }
}

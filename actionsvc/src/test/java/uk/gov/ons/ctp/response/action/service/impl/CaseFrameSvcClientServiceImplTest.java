package uk.gov.ons.ctp.response.action.service.impl;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

@RunWith(MockitoJUnitRunner.class)
public class CaseFrameSvcClientServiceImplTest {

  @Mock
  AppConfig appConfig;
  
  @Spy
  RestClient restClient = new RestClient ("http", "localhost", "8080");

  @InjectMocks
  CaseFrameSvcClientServiceImpl caseFrameSvcClientService;

  @Test
  public void testGetOpenCasesForActionPlan() {
    CaseFrameSvc caseFrameSvcConfig = new CaseFrameSvc();
    caseFrameSvcConfig.setCaseByStatusAndActionPlanPath("/cases/actionplan/{actionplanid}");
    Mockito.when(appConfig.getCaseFrameSvc()).thenReturn(caseFrameSvcConfig);
    RestTemplate restTemplate = this.restClient.getRestTemplate();

    MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
    mockServer.expect(requestTo("http://localhost:8080/cases/actionplan/1?state=INIT&state=RESPONDED")).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess("[1,2,3]", MediaType.APPLICATION_JSON));

    List<Integer> cases = caseFrameSvcClientService.getOpenCasesForActionPlan(1);
    assertTrue(cases != null);
    assertTrue(cases.containsAll(Arrays.asList(new Integer[]{1,2,3})));
    mockServer.verify();
  }

}

package uk.gov.ons.ctp.response.action.endpoint;

import javax.ws.rs.core.Application;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanService;
import uk.gov.ons.ctp.response.action.utility.MockActionPlanServiceFactory;

import static uk.gov.ons.ctp.response.action.utility.MockActionPlanServiceFactory.*;

/**
 * Unit tests for ActionPlan endpoint
 */
public class ActionPlanEndpointUnitTest extends CTPJerseyTest {

  private static final String CREATED_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String LAST_GOOD_RUN_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String ACTIONPLAN_JSON = "{\"actionPlanId\":21,\"surveyId\":1,\"name\":\"HH\",\"description\":\"philippetesting\",\"createdBy\":\"SYSTEM\",\"createdDatetime\":\"2016-03-10T15:10:39.494+0000\",\"lastGoodRunDatetime\":null}";
  private static final String ACTIONPLAN_INVALIDJSON = "{\"some\":\"joke\"}";


  @Override
  public Application configure() {
    return super.init(ActionPlanEndpoint.class, ActionPlanService.class, MockActionPlanServiceFactory.class, new ActionBeanMapper(), new CTPMessageBodyReader<ActionPlanDTO>(ActionPlanDTO.class) {});
  }

  @Test
  public void findActionPlansFound() {
    with("http://localhost:9998/actionplans")
        .assertResponseCodeIs(HttpStatus.OK)
        .assertArrayLengthInBodyIs(3)
        .assertIntegerListInBody("$..actionPlanId", 1, 2, 3)
        .assertIntegerListInBody("$..surveyId", ACTIONPLAN_SURVEYID, ACTIONPLAN_SURVEYID, ACTIONPLAN_SURVEYID)
        .assertStringListInBody("$..name", ACTIONPLAN1_NAME, ACTIONPLAN2_NAME, ACTIONPLAN3_NAME)
        .assertStringListInBody("$..description", ACTIONPLAN1_DESC, ACTIONPLAN2_DESC, ACTIONPLAN3_DESC)
        .assertStringListInBody("$..createdBy", CREATED_BY, CREATED_BY, CREATED_BY)
        .assertStringListInBody("$..createdDatetime", CREATED_DATE_TIME, CREATED_DATE_TIME, CREATED_DATE_TIME)
        .assertStringListInBody("$..lastGoodRunDatetime", LAST_GOOD_RUN_DATE_TIME, LAST_GOOD_RUN_DATE_TIME,
            LAST_GOOD_RUN_DATE_TIME)
        .andClose();
  }

  @Test
  public void findActionPlanFound() {
    with("http://localhost:9998/actionplans/%s", ACTIONPLANID)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerInBody("$.actionPlanId", 3)
        .assertIntegerInBody("$.surveyId", ACTIONPLAN_SURVEYID)
        .assertStringInBody("$.name", ACTIONPLAN3_NAME)
        .assertStringInBody("$.description", ACTIONPLAN3_DESC)
        .assertStringInBody("$.createdBy", CREATED_BY)
        .assertStringInBody("$.createdDatetime", CREATED_DATE_TIME)
        .assertStringInBody("$.lastGoodRunDatetime", LAST_GOOD_RUN_DATE_TIME)
        .andClose();
  }

  @Test
  public void findActionPlanNotFound() {
    with("http://localhost:9998/actionplans/%s", NON_EXISTING_ACTIONPLANID)
      .assertResponseCodeIs(HttpStatus.NOT_FOUND)
      .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
      .assertTimestampExists()
      .assertMessageEquals("ActionPlan not found for id %s", NON_EXISTING_ACTIONPLANID)
      .andClose();
  }

  @Test
  public void findActionPlanUnCheckedException() {
    with("http://localhost:9998/actionplans/%s", UNCHECKED_EXCEPTION)
      .assertResponseCodeIs(HttpStatus.INTERNAL_SERVER_ERROR)
      .assertFaultIs(CTPException.Fault.SYSTEM_ERROR)
      .assertTimestampExists()
      .assertMessageEquals(OUR_EXCEPTION_MESSAGE)
      .andClose();
  }

  @Test
  public void findActionRulesForActionPlanFound() {
    with("http://localhost:9998/actionplans/%s/rules", ACTIONPLANID)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertArrayLengthInBodyIs(3)
        .assertIntegerListInBody("$..actionRuleId", 1, 2, 3)
        .assertIntegerListInBody("$..actionPlanId", ACTIONPLANID, ACTIONPLANID, ACTIONPLANID)
        .assertIntegerListInBody("$..priority", ACTIONRULE_PRIORITY, ACTIONRULE_PRIORITY, ACTIONRULE_PRIORITY)
        .assertIntegerListInBody("$..surveyDateDaysOffset", ACTIONRULE_SURVEYDATEDAYSOFFSET,
            ACTIONRULE_SURVEYDATEDAYSOFFSET, ACTIONRULE_SURVEYDATEDAYSOFFSET)
        .assertStringListInBody("$..actionTypeName", ACTIONRULE_ACTIONTYPENAME, ACTIONRULE_ACTIONTYPENAME,
            ACTIONRULE_ACTIONTYPENAME)
        .assertStringListInBody("$..name", ACTIONRULE_NAME, ACTIONRULE_NAME, ACTIONRULE_NAME)
        .assertStringListInBody("$..description", ACTIONRULE_DESCRIPTION, ACTIONRULE_DESCRIPTION,
            ACTIONRULE_DESCRIPTION)
        .andClose();
  }


  @Test
  public void findNoActionRulesForActionPlan() {
    with("http://localhost:9998/actionplans/%s/rules", ACTIONPLANID_WITHNOACTIONRULE)
        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
        .andClose();
  }

  @Test
  public void findActionRulesForNonExistingActionPlan() {
    with("http://localhost:9998/actionplans/%s/rules", NON_EXISTING_ACTIONPLANID)
        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
        .assertTimestampExists()
        .assertMessageEquals("ActionPlan not found for id %s", NON_EXISTING_ACTIONPLANID)
        .andClose();
  }

  @Test
  public void createActionPlanPositiveScenario() {
    with("http://localhost:9998/actionplans").post(ACTIONPLAN_JSON)
        .assertResponseCodeIs(HttpStatus.NOT_IMPLEMENTED)
        .andClose();
  }

  @Test
  public void createActionPlanNegativeScenario() {
    with("http://localhost:9998/actionplans").post(ACTIONPLAN_INVALIDJSON)
        .assertResponseCodeIs(HttpStatus.NOT_IMPLEMENTED)
        .andClose();
  }

  @Test
  public void updateActionPlanNegativeScenarioInvalidJsonProvided() {
    with("http://localhost:9998/actionplans/%s", ACTIONPLANID).put(ACTIONPLAN_INVALIDJSON)
        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
        .assertTimestampExists()
        .assertMessageEquals(PROVIDED_JSON_INCORRECT)
        .andClose();
  }

  @Test
  public void updateActionPlanHappyScenario() {
    with("http://localhost:9998/actionplans/%s", ACTIONPLANID).put(ACTIONPLAN_JSON)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerInBody("$.actionPlanId", ACTIONPLANID)
        .assertIntegerInBody("$.surveyId", ACTIONPLAN_SURVEYID)
        .assertStringInBody("$.name", ACTIONPLAN3_NAME)
        .assertStringInBody("$.description", ACTIONPLAN3_DESC)
        .assertStringInBody("$.createdBy", CREATED_BY)
        .assertStringInBody("$.createdDatetime", CREATED_DATE_TIME)
        .assertStringInBody("$.lastGoodRunDatetime", LAST_GOOD_RUN_DATE_TIME)
        .andClose();
  }

}

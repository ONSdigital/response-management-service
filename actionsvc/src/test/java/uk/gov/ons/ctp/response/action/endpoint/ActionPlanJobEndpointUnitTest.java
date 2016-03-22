package uk.gov.ons.ctp.response.action.endpoint;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;
import uk.gov.ons.ctp.response.action.utility.MockActionPlanJobServiceFactory;

import javax.ws.rs.core.Application;


import static uk.gov.ons.ctp.response.action.utility.MockActionPlanJobServiceFactory.*;

/**
 * Created by philippe.brossier on 3/15/16.
 */
public class ActionPlanJobEndpointUnitTest extends CTPJerseyTest {

  // invalid as createdBy can NOT be empty
  private static final String ACTIONPLANJOB_INVALIDJSON = "{\"createdBy\":\"\"}";
  private static final String ACTIONPLANJOB_VALIDJSON = "{\"createdBy\":\"unittest\"}";
  private static final String CREATED_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String UPDATED_DATE_TIME = "2016-04-09T10:15:48.023+0000";

  @Override
  public Application configure() {
    return super.init(ActionPlanJobEndpoint.class, ActionPlanJobService.class, MockActionPlanJobServiceFactory.class, new ActionBeanMapper(), new CTPMessageBodyReader<ActionPlanJobDTO>(ActionPlanJobDTO.class) {});
  }

  @Test
  public void findActionPlanJobFound() {
    with("http://localhost:9998/actionplans/jobs/%s", ACTIONPLANJOBID)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerInBody("$.actionPlanJobId", ACTIONPLANJOBID)
        .assertIntegerInBody("$.actionPlanId", ACTIONPLANJOBID_ACTIONPLANID)
        .assertStringInBody("$.createdBy", ACTIONPLANJOBID_CREATED_BY)
        .assertStringInBody("$.state", ACTIONPLANJOBID_STATE)
        .assertStringInBody("$.createdDatetime", CREATED_DATE_TIME)
        .assertStringInBody("$.updatedDateTime", UPDATED_DATE_TIME)
        .andClose();
  }

  @Test
  public void findActionPlanJobNotFound() {
    with("http://localhost:9998/actionplans/jobs/%s", NON_EXISTING_ACTIONPLANJOBID)
        .assertResponseCodeIs(HttpStatus.NOT_FOUND)
        .assertFaultIs(CTPException.Fault.RESOURCE_NOT_FOUND)
        .assertTimestampExists()
        .assertMessageEquals("ActionPlanJob not found for id %s", NON_EXISTING_ACTIONPLANJOBID)
        .andClose();
  }

  @Test
  public void findActionPlanUnCheckedException() {
    with("http://localhost:9998/actionplans/jobs/%s", UNCHECKED_EXCEPTION_ACTIONPLANJOBID)
        .assertResponseCodeIs(HttpStatus.INTERNAL_SERVER_ERROR)
        .assertFaultIs(CTPException.Fault.SYSTEM_ERROR)
        .assertTimestampExists()
        .assertMessageEquals(OUR_EXCEPTION_MESSAGE)
        .andClose();
  }

  @Test
  public void findNoActionPlanJobForActionPlan() {
    with("http://localhost:9998/actionplans/%s/jobs", ACTIONPLANID_WITHNOACTIONPLANJOB)
        .assertResponseCodeIs(HttpStatus.NO_CONTENT)
        .andClose();
  }

  @Test
  public void findActionPlanJobsForActionPlan() {
    with("http://localhost:9998/actionplans/%s/jobs", ACTIONPLANID)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertArrayLengthInBodyIs(3)
        .assertIntegerListInBody("$..actionPlanJobId", 1, 2, 3)
        .assertIntegerListInBody("$..actionPlanId", ACTIONPLANID, ACTIONPLANID, ACTIONPLANID)
        .assertStringListInBody("$..createdBy", ACTIONPLANJOBID_CREATED_BY, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_CREATED_BY)
        .assertStringListInBody("$..createdDatetime", CREATED_DATE_TIME, CREATED_DATE_TIME, CREATED_DATE_TIME)
        .assertStringListInBody("$..updatedDateTime", UPDATED_DATE_TIME, UPDATED_DATE_TIME, UPDATED_DATE_TIME)
        .andClose();
  }

  @Test
  public void executeActionPlanBadJsonProvided() {
    with("http://localhost:9998/actionplans/%s/jobs", ACTIONPLANID).post(ACTIONPLANJOB_INVALIDJSON)
        .assertResponseCodeIs(HttpStatus.BAD_REQUEST)
        .assertFaultIs(CTPException.Fault.VALIDATION_FAILED)
        .assertTimestampExists()
        .assertMessageEquals(PROVIDED_JSON_INVALID)
        .andClose();
  }

  @Test
  public void executeActionPlanGoodJsonProvided() {
    with("http://localhost:9998/actionplans/%s/jobs", ACTIONPLANID).post(ACTIONPLANJOB_VALIDJSON)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertIntegerInBody("$.actionPlanJobId", ACTIONPLANJOBID)
        .assertIntegerInBody("$.actionPlanId", ACTIONPLANJOBID_ACTIONPLANID)
        .assertStringInBody("$.createdBy", ACTIONPLANJOBID_CREATED_BY)
        .assertStringInBody("$.state", ACTIONPLANJOBID_STATE)
        .assertStringInBody("$.createdDatetime", CREATED_DATE_TIME)
        .assertStringInBody("$.updatedDateTime", UPDATED_DATE_TIME)
        .andClose();
  }

}

package uk.gov.ons.ctp.response.action.endpoint;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.action.ActionBeanMapper;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;
import uk.gov.ons.ctp.response.action.utility.MockActionPlanJobServiceFactory;

import javax.ws.rs.core.Application;


import static uk.gov.ons.ctp.response.action.utility.MockActionPlanJobServiceFactory.*;

/**
 * Created by philippe.brossier on 3/15/16.
 */
public class ActionPlanJobEndpointUnitTest extends CTPJerseyTest {

  private static final String CREATED_DATE_TIME = "2016-03-09T11:15:48.023+0000";
  private static final String UPDATED_DATE_TIME = "2016-04-09T10:15:48.023+0000";

  @Override
  public Application configure() {
    return super.init(ActionPlanJobEndpoint.class, ActionPlanJobService.class, MockActionPlanJobServiceFactory.class, new ActionBeanMapper());
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

}

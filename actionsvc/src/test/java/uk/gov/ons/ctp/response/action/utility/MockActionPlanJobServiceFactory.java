package uk.gov.ons.ctp.response.action.utility;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.service.ActionPlanJobService;

import java.sql.Timestamp;

/**
 * Created by philippe.brossier on 3/15/16.
 */
public class MockActionPlanJobServiceFactory implements Factory<ActionPlanJobService> {
  public static final Integer ACTIONPLANJOBID = 1;
  public static final Integer ACTIONPLANJOBID_ACTIONPLANID = 1;
  public static final String ACTIONPLANJOBID_CREATED_BY = "theTester";
  public static final String ACTIONPLANJOBID_STATE = "theState";
  private static final Timestamp ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP =
      Timestamp.valueOf("2016-03-09 11:15:48.023286");
  private static final Timestamp ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP =
      Timestamp.valueOf("2016-04-09 11:15:48.023286");

  public ActionPlanJobService provide() {
    final ActionPlanJobService mockedService = Mockito.mock(ActionPlanJobService.class);

    Mockito.when(mockedService.findActionPlanJob(ACTIONPLANJOBID)).thenAnswer(new Answer<ActionPlanJob>() {
      public ActionPlanJob answer(InvocationOnMock invocation)
          throws Throwable {
        return new ActionPlanJob(ACTIONPLANJOBID, ACTIONPLANJOBID_ACTIONPLANID, ACTIONPLANJOBID_CREATED_BY,
            ACTIONPLANJOBID_STATE, ACTIONPLANJOBID_CREATEDDATE_TIMESTAMP, ACTIONPLANJOBID_UPDATED_DATE_TIMESTAMP);
      }
    });
    return mockedService;
  }

  public void dispose(ActionPlanJobService t) {
  }
}

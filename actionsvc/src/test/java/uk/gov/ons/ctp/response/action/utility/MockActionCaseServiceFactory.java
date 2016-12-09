package uk.gov.ons.ctp.response.action.utility;

import java.sql.Timestamp;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.gov.ons.ctp.response.action.domain.model.ActionCase;
import uk.gov.ons.ctp.response.action.service.ActionCaseService;

/**
 * A MockActionServiceFactory
 */
public final class MockActionCaseServiceFactory implements Factory<ActionCaseService> {

  public static final Integer ACTION_CASEID = 124;
  public static final Integer ACTION1_PLANID = 1;
  public static final Timestamp ACTION_CREATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  public static final Integer NON_EXISTING_ID = 998;

  @Override
  public ActionCaseService provide() {

    final ActionCaseService mockedService = Mockito.mock(ActionCaseService.class);

    Mockito.when(mockedService.findActionCase(ACTION_CASEID)).thenAnswer(new Answer<ActionCase>() {
          public ActionCase answer(final InvocationOnMock invocation) throws Throwable {
            return new ActionCase(ACTION_CASEID, ACTION1_PLANID, ACTION_CREATEDDATE_TIMESTAMP);
          }
        });

    Mockito.when(mockedService.findActionCase(NON_EXISTING_ID)).thenAnswer(new Answer<ActionCase>() {
          public ActionCase answer(final InvocationOnMock invocation) throws Throwable {
            return null;
          }
        });

    return mockedService;
  }

  @Override
  public void dispose(final ActionCaseService t) {
  }
}

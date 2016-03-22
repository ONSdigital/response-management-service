package uk.gov.ons.ctp.response.action.utility;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Matchers.any;

import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.Action.StateType;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * Created by Martin.Humphrey on 26/2/2016.
 */
public final class MockActionServiceFactory implements Factory<ActionService> {

  public static final Integer ACTION_CASEID = 124;
  public static final Integer ACTION1_PLANID = 1;
  public static final Integer ACTION2_PLANID = 2;
  public static final Integer ACTION1_RULEID = 1;
  public static final Integer ACTION2_RULEID = 2;
  public static final StateType ACTION1_ACTIONSTATE = Action.StateType.ACTIVE;
  public static final StateType ACTION2_ACTIONSTATE = Action.StateType.COMPLETED;
  public static final Boolean ACTION1_MANUALLY_CREATED = true;
  public static final Boolean ACTION2_MANUALLY_CREATED = false;
  public static final String ACTION1_ACTIONTYPENAME = "actiontypename1";
  public static final String ACTION2_ACTIONTYPENAME = "actiontypename2";
  public static final Integer ACTION1_PRIORITY = 1;
  public static final Integer ACTION2_PRIORITY = 3;
  public static final String ACTION1_SITUATION = "Assigned";
  public static final String ACTION2_SITUATION = "Sent";
  public static final String ACTION_CREATEDDATE_VALUE = "2016-02-26T18:30:00.000+0000";
  public static final Timestamp ACTION_CREATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  public static final Timestamp ACTION_UPDATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  public static final String ACTION_CREATEDBY = "Unit Tester";
  public static final Integer ACTIONID = 2;
  public static final Integer NON_EXISTING_ID = 998;
  public static final Integer UNCHECKED_EXCEPTION = 999;
  public static final String ACTION_NOTFOUND = "NotFound";
  public static final String OUR_EXCEPTION_MESSAGE = "this is what we throw";

  @Override
  public ActionService provide() {

    final ActionService mockedService = Mockito.mock(ActionService.class);

    Mockito.when(mockedService.findActionsByTypeAndState(ACTION2_ACTIONTYPENAME, ACTION2_ACTIONSTATE.toString()))
        .thenAnswer(new Answer<List<Action>>() {
          public List<Action> answer(final InvocationOnMock invocation)
              throws Throwable {
            List<Action> result = new ArrayList<Action>();
            result.add(new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
                ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
                ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP));
            return result;
          }
        });

    Mockito.when(mockedService.findActionsByTypeAndState(ACTION_NOTFOUND, ACTION_NOTFOUND))
        .thenAnswer(new Answer<List<Action>>() {
          public List<Action> answer(final InvocationOnMock invocation)
              throws Throwable {
            return new ArrayList<Action>();
          }
        });

    Mockito.when(mockedService.findActionsByType(ACTION2_ACTIONTYPENAME)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        List<Action> result = new ArrayList<Action>();
        result.add(new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP));
        return result;
      }
    });

    Mockito.when(mockedService.findActionsByType(ACTION_NOTFOUND)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        return new ArrayList<Action>();
      }
    });

    Mockito.when(mockedService.findActionsByState(ACTION2_ACTIONSTATE.toString())).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        List<Action> result = new ArrayList<Action>();
        result.add(new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP));
        return result;
      }
    });

    Mockito.when(mockedService.findActionsByState(ACTION_NOTFOUND)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        return new ArrayList<Action>();
      }
    });

    Mockito.when(mockedService.findActionByActionId(ACTIONID)).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation)
          throws Throwable {
        return new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP);
      }
    });

    Mockito.when(mockedService.findActionByActionId(NON_EXISTING_ID)).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation)
          throws Throwable {
        return null;
      }
    });

    Mockito.when(mockedService.findActionsByCaseId(ACTION_CASEID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        List<Action> result = new ArrayList<Action>();
        result.add(new Action(1, ACTION_CASEID, ACTION1_PLANID, ACTION1_RULEID, ACTION1_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION1_MANUALLY_CREATED, ACTION1_PRIORITY, ACTION1_SITUATION, ACTION1_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP));
        result.add(new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP));
        return result;
      }
    });

    Mockito.when(mockedService.findActionByActionId(UNCHECKED_EXCEPTION))
        .thenThrow(new IllegalArgumentException(OUR_EXCEPTION_MESSAGE));

    Mockito.when(mockedService.findActionsByCaseId(NON_EXISTING_ID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation)
          throws Throwable {
        return new ArrayList<Action>();
      }
    });

    Mockito.when(mockedService.createAction(any(Action.class))).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation)
          throws Throwable {
        return new Action(2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION2_ACTIONTYPENAME,
            ACTION_CREATEDBY, ACTION2_MANUALLY_CREATED, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, null);
      }
    });

    return mockedService;
  }

  @Override
  public void dispose(final ActionService t) {
  }
}

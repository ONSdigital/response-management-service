package uk.gov.ons.ctp.response.action.utility;

import static org.mockito.Matchers.any;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionType;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.service.ActionService;

/**
 * A MockActionServiceFactory
 */
public final class MockActionServiceFactory {

  public static final Integer ACTION_CASEID = 124;
  public static final Integer ACTION1_PLANID = 1;
  public static final Integer ACTION2_PLANID = 2;
  public static final Integer ACTION1_RULEID = 1;
  public static final Integer ACTION2_RULEID = 2;
  public static final ActionDTO.ActionState ACTION1_ACTIONSTATE = ActionDTO.ActionState.ACTIVE;
  public static final ActionDTO.ActionState ACTION2_ACTIONSTATE = ActionDTO.ActionState.COMPLETED;
  public static final ActionDTO.ActionState ACTION3_ACTIONSTATE = ActionDTO.ActionState.CANCELLED;
  public static final Boolean ACTION1_MANUALLY_CREATED = true;
  public static final Boolean ACTION2_MANUALLY_CREATED = false;
  public static final String ACTION1_ACTIONTYPENAME = "actiontypename1";
  public static final String ACTION2_ACTIONTYPENAME = "actiontypename2";
  public static final String ACTION1_ACTIONTYPEDESC = "actiontypedesc1";
  public static final String ACTION2_ACTIONTYPEDESC = "actiontypedesc2";
  public static final String ACTION1_ACTIONTYPEHANDLER = "Printer";
  public static final String ACTION2_ACTIONTYPEHANDLER = "Field";
  public static final Boolean ACTION1_ACTIONTYPECANCEL = true;
  public static final Boolean ACTION2_ACTIONTYPECANCEL = false;
  public static final Boolean ACTION1_RESPONSEREQUIRED = true;
  public static final Boolean ACTION2_RESPONSEREQUIRED = false;
  public static final Integer ACTION1_PRIORITY = 1;
  public static final Integer ACTION2_PRIORITY = 3;
  public static final String ACTION1_SITUATION = "Assigned";
  public static final String ACTION2_SITUATION = "Sent";
  public static final String ACTION_CREATEDDATE_VALUE = "2016-02-26T18:30:00.000+0000";
  public static final Timestamp ACTION_CREATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  public static final Timestamp ACTION_UPDATEDDATE_TIMESTAMP = Timestamp.valueOf("2016-02-26 18:30:00");
  public static final String ACTION_CREATEDBY = "Unit Tester";
  public static final BigInteger ACTIONID_1 = BigInteger.valueOf(1);
  public static final BigInteger ACTIONID_2 = BigInteger.valueOf(2);
  public static final Integer NON_EXISTING_ID = 998;
  public static final BigInteger NON_EXISTING_ACTION_ID = BigInteger.valueOf(998);
  public static final BigInteger UNCHECKED_EXCEPTION = BigInteger.valueOf(999);
  public static final String ACTION_NOTFOUND = "NotFound";
  public static final String OUR_EXCEPTION_MESSAGE = "this is what we throw";

  public ActionService provide() {

    final ActionService mockedService = Mockito.mock(ActionService.class);

    Mockito.when(mockedService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(ACTION2_ACTIONTYPENAME,
        ACTION2_ACTIONSTATE)).thenAnswer(new Answer<List<Action>>() {
          public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
            List<Action> result = new ArrayList<Action>();
            ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
                ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
            result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
                ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION,
                ACTION2_ACTIONSTATE, ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
            return result;
          }
        });

    Mockito.when(mockedService.findActionsByTypeAndStateOrderedByCreatedDateTimeDescending(ACTION_NOTFOUND,
        ACTION2_ACTIONSTATE)).thenAnswer(new Answer<List<Action>>() {
          public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
            return new ArrayList<Action>();
          }
        });

    Mockito.when(mockedService.findActionsByType(ACTION2_ACTIONTYPENAME)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        List<Action> result = new ArrayList<Action>();
        ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
        return result;
      }
    });

    Mockito.when(mockedService.findActionsByType(ACTION_NOTFOUND)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        return new ArrayList<Action>();
      }
    });

    Mockito.when(mockedService.findActionsByState(ACTION2_ACTIONSTATE)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        List<Action> result = new ArrayList<Action>();
        ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
        return result;
      }
    });

    Mockito.when(mockedService.findActionByActionId(ACTIONID_2)).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation) throws Throwable {
        ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        return new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0);
      }
    });

    Mockito.when(mockedService.findActionByActionId(NON_EXISTING_ACTION_ID)).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation) throws Throwable {
        return null;
      }
    });

    Mockito.when(mockedService.findActionsByCaseId(ACTION_CASEID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        List<Action> result = new ArrayList<Action>();
        ActionType actionType1 = new ActionType(1, ACTION1_ACTIONTYPENAME, ACTION1_ACTIONTYPEDESC,
            ACTION1_ACTIONTYPEHANDLER, ACTION1_ACTIONTYPECANCEL, ACTION1_RESPONSEREQUIRED);
        ActionType actionType2 = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        result.add(new Action(ACTIONID_1, ACTION_CASEID, ACTION1_PLANID, ACTION1_RULEID, ACTION_CREATEDBY,
            ACTION1_MANUALLY_CREATED, actionType1, ACTION1_PRIORITY, ACTION1_SITUATION, ACTION1_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
        result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType2, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
        return result;
      }
    });

    Mockito.when(mockedService.findActionByActionId(UNCHECKED_EXCEPTION))
        .thenThrow(new IllegalArgumentException(OUR_EXCEPTION_MESSAGE));

    Mockito.when(mockedService.findActionsByCaseId(NON_EXISTING_ID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        return new ArrayList<Action>();
      }
    });

    Mockito.when(mockedService.createAction(any(Action.class))).thenAnswer(new Answer<Action>() {
      public Action answer(final InvocationOnMock invocation) throws Throwable {
        ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        return new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION2_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, null, 0);
      }
    });

    Mockito.when(mockedService.cancelActions(ACTION_CASEID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        List<Action> result = new ArrayList<Action>();
        ActionType actionType = new ActionType(1, ACTION2_ACTIONTYPENAME, ACTION2_ACTIONTYPEDESC,
            ACTION2_ACTIONTYPEHANDLER, ACTION2_ACTIONTYPECANCEL, ACTION2_RESPONSEREQUIRED);
        result.add(new Action(ACTIONID_2, ACTION_CASEID, ACTION2_PLANID, ACTION2_RULEID, ACTION_CREATEDBY,
            ACTION2_MANUALLY_CREATED, actionType, ACTION2_PRIORITY, ACTION2_SITUATION, ACTION3_ACTIONSTATE,
            ACTION_CREATEDDATE_TIMESTAMP, ACTION_UPDATEDDATE_TIMESTAMP, 0));
        return result;
      }
    });

    Mockito.when(mockedService.cancelActions(NON_EXISTING_ID)).thenAnswer(new Answer<List<Action>>() {
      public List<Action> answer(final InvocationOnMock invocation) throws Throwable {
        List<Action> result = new ArrayList<Action>();
        return result;
      }
    });

    return mockedService;
  }
}

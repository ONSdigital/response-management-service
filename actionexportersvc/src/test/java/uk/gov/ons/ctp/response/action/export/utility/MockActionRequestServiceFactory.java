package uk.gov.ons.ctp.response.action.export.utility;


import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.service.ActionRequestService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock ActionRequestService response HK2 JSE JSR-330 dependency injection factory
 */
public class MockActionRequestServiceFactory implements Factory<ActionRequestService> {

  public final static int EXISTING_ACTION_ID = 2;
  public final static int NON_EXISTING_ACTION_ID = 1;

  /**
   * provide method
   *
   * @return mocked service
   */
  public ActionRequestService provide() {
    final ActionRequestService mockedService = Mockito.mock(ActionRequestService.class);

    Mockito.when(mockedService.retrieveAllActionRequests()).thenAnswer(new Answer<List<ActionRequestInstruction>>() {
      public List<ActionRequestInstruction> answer(final InvocationOnMock invocation) throws Throwable {
        List<ActionRequestInstruction> result = new ArrayList<>();
        for (int i = 0; i < 3; i++){
          result.add(buildActionRequest(i));
        }
        return result;
      }
    });

    Mockito.when(mockedService.retrieveActionRequest(BigInteger.valueOf(NON_EXISTING_ACTION_ID))).thenAnswer(new Answer<ActionRequestInstruction>() {
      public ActionRequestInstruction answer(final InvocationOnMock invocation) throws Throwable {
        return null;
      }
    });

    Mockito.when(mockedService.retrieveActionRequest(BigInteger.valueOf(EXISTING_ACTION_ID))).thenAnswer(new Answer<ActionRequestInstruction>() {
      public ActionRequestInstruction answer(final InvocationOnMock invocation) throws Throwable {
        return buildActionRequest(EXISTING_ACTION_ID);
      }
    });

    return mockedService;
  }

  /**
   * dispose method
   *
   * @param t service to dispose
   */
  public void dispose(final ActionRequestService t) {
  }

  private static ActionRequestInstruction buildActionRequest(int actionId) {
    ActionRequestInstruction actionRequest = new ActionRequestInstruction();
    actionRequest.setActionId(BigInteger.valueOf(actionId));
    return actionRequest;
  }
}

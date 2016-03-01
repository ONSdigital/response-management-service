package uk.gov.ons.ctp.response.caseframe.utility;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import uk.gov.ons.ctp.response.caseframe.domain.model.Questionnaire;
import uk.gov.ons.ctp.response.caseframe.service.QuestionnaireService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philippe.brossier on 2/26/16.
 */
public class MockQuestionnaireServiceFactory implements Factory<QuestionnaireService> {

  public static final String QUESTIONNAIRE_IAC = "iac";
  public static final String QUESTIONNAIRE_IAC_NOT_FOUND = "iacnotfound";
  public static final Integer QUESTIONNAIRE_CASEID = 1;
  public static final Integer QUESTIONNAIRE_CASEID_NOT_FOUND = 666;
  public static final Integer QUESTIONNAIRE_ID_1 = 1;
  public static final Integer QUESTIONNAIRE_ID_2 = 2;
  public static final Integer QUESTIONNAIRE_ID_SERVER_SIDE_ERROR = 666;

  public QuestionnaireService provide() {
    final QuestionnaireService mockedService = Mockito.mock(QuestionnaireService.class);
    Mockito.when(mockedService.findQuestionnaireByIac(QUESTIONNAIRE_IAC)).thenAnswer(new Answer<Questionnaire>() {
      public Questionnaire answer(InvocationOnMock invocation)
              throws Throwable {
        String iacCode = (String)invocation.getArguments()[0];
        return QuestionnaireBuilder.questionnaire()
                .iac(iacCode)
                .id(QUESTIONNAIRE_ID_1)
                .caseid(QUESTIONNAIRE_CASEID)
                .buildQuestionnaire();
      }
    });

    Mockito.when(mockedService.findQuestionnaireByIac(QUESTIONNAIRE_IAC_NOT_FOUND)).thenAnswer(new Answer<Questionnaire>() {
      public Questionnaire answer(InvocationOnMock invocation)
              throws Throwable {
        return null;
      }
    });

    Mockito.when(mockedService.findQuestionnairesByCaseId(QUESTIONNAIRE_CASEID)).thenAnswer(new Answer<List<Questionnaire>>() {
      public List<Questionnaire> answer(InvocationOnMock invocation)
              throws Throwable {
        Integer caseid = (Integer)invocation.getArguments()[0];
        Questionnaire questionnaire1 = QuestionnaireBuilder.questionnaire()
                .iac(QUESTIONNAIRE_IAC)
                .id(QUESTIONNAIRE_ID_1)
                .caseid(caseid)
                .buildQuestionnaire();
        Questionnaire questionnaire2 = QuestionnaireBuilder.questionnaire()
                .iac(QUESTIONNAIRE_IAC)
                .id(QUESTIONNAIRE_ID_2)
                .caseid(caseid)
                .buildQuestionnaire();
        List<Questionnaire> list = new ArrayList<>();
        list.add(questionnaire1);
        list.add(questionnaire2);
        return list;
      }
    });

    Mockito.when(mockedService.findQuestionnairesByCaseId(QUESTIONNAIRE_CASEID_NOT_FOUND)).thenAnswer(new Answer<List<Questionnaire>>() {
      public List<Questionnaire> answer(InvocationOnMock invocation)
              throws Throwable {
        return new ArrayList<>();
      }
    });

    Mockito.when(mockedService.updateResponseTime(QUESTIONNAIRE_ID_1)).thenReturn(1);
    Mockito.when(mockedService.closeParentCase(QUESTIONNAIRE_ID_1)).thenReturn(1);

    Mockito.when(mockedService.updateResponseTime(QUESTIONNAIRE_ID_SERVER_SIDE_ERROR)).thenReturn(0);
    Mockito.when(mockedService.closeParentCase(QUESTIONNAIRE_ID_SERVER_SIDE_ERROR)).thenReturn(0);

    return mockedService;
  }

  public void dispose(QuestionnaireService t) {}
}

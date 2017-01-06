package uk.gov.ons.ctp.response.iac.endpoint;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.ons.ctp.common.jaxrs.CTPMessageBodyReader;
import uk.gov.ons.ctp.common.jersey.CTPJerseyTest;
import uk.gov.ons.ctp.response.iac.ServiceBeanMapper;
import uk.gov.ons.ctp.response.iac.config.AppConfig;
import uk.gov.ons.ctp.response.iac.representation.CreateInternetAccessCodeDTO;
import uk.gov.ons.ctp.response.iac.representation.UpdateInternetAccessCodeDTO;
import uk.gov.ons.ctp.response.iac.service.InternetAccessCodeService;
import uk.gov.ons.ctp.response.iac.utility.MockAppConfigFactory;
import uk.gov.ons.ctp.response.iac.utility.MockInternetAccessCodeServiceFactory;


/**
 * Test the IAC service endpoints
 *
 */
public class InternetAccessCodeEndpointTest extends CTPJerseyTest {

  /**
   * configure the test
   */
  @Override
  public Application configure() {
    CTPJerseyTest.ServiceFactoryPair[] mockedFactoryBeanPairs = new CTPJerseyTest.ServiceFactoryPair[] {
        new CTPJerseyTest.ServiceFactoryPair(InternetAccessCodeService.class,
            MockInternetAccessCodeServiceFactory.class),
        new CTPJerseyTest.ServiceFactoryPair(AppConfig.class, MockAppConfigFactory.class)
    };

    return super.init(InternetAccessCodeEndpoint.class, mockedFactoryBeanPairs, new ServiceBeanMapper(),
        new CTPMessageBodyReader<CreateInternetAccessCodeDTO>(CreateInternetAccessCodeDTO.class),
        new CTPMessageBodyReader<UpdateInternetAccessCodeDTO>(UpdateInternetAccessCodeDTO.class));
  }

  public static final String CTX_IAC_EXTERNAL = "6hlhlqwrbdqq";
  public static final String CTX_IAC_INTERNAL = "6hlhlqwrbdqq";
  public static final String CTX_QUESTION_SET = "HH";
  public static final Long CTX_UPRN = 10012136162L;
  public static final Integer CTX_CASE_ID = 1;
  public static final Integer CTX_SAMPLE_ID = 2;
  public static final Integer CTX_SURVEY_ID = 3;
  public static final Integer CTX_PARENT_CASE_ID = 4;

  public static final Integer CREATE_GOOD = 10;
  public static final Integer CREATE_BAD = 1001;

  public static final String CREATED_BY = "MyLittleUnitTest";

  /**
   * Test the get context/GET endpoint
   */
  @Test
  public void getIACCaseContext() {
    with("/iacs/%s", CTX_IAC_EXTERNAL)
        .assertResponseCodeIs(HttpStatus.OK)
        .assertStringInBody("$.iac", CTX_IAC_EXTERNAL)
        .assertStringInBody("$.questionSet", CTX_QUESTION_SET)
        .assertIntegerInBody("$.caseId", CTX_CASE_ID)
        .assertIntegerInBody("$.caseRef", CTX_CASE_ID)
        .assertBooleanInBody("$.active", true)
        .andClose();
  }

  /**
   * TEst the generatedCodes/POST endpoint
   * @throws JsonProcessingException
   */
  @Test
  public void postGenerateCodes() throws JsonProcessingException {
    CreateInternetAccessCodeDTO createInternetAccessCodeDTO = new CreateInternetAccessCodeDTO();
    createInternetAccessCodeDTO.setCount(CREATE_GOOD);
    createInternetAccessCodeDTO.setCreatedBy(CREATED_BY);
    ObjectMapper objMapper = new ObjectMapper();

    with("/iacs")
        .post(MediaType.APPLICATION_JSON_TYPE, objMapper.writeValueAsString(createInternetAccessCodeDTO))
        .assertResponseCodeIs(HttpStatus.CREATED)
        .assertArrayLengthInBodyIs(CREATE_GOOD)
        .andClose();
  }

  /**
   * Test the deactivate/PUT endpoint
   * @throws JsonProcessingException
   */
  @Test
  public void putDeactivateCode() throws JsonProcessingException {
    UpdateInternetAccessCodeDTO updateInternetAccessCodeDTO = new UpdateInternetAccessCodeDTO();
    updateInternetAccessCodeDTO.setUpdatedBy(CREATED_BY);
    ObjectMapper objMapper = new ObjectMapper();

    with("/iacs/%s", CTX_IAC_EXTERNAL)
        .put(MediaType.APPLICATION_JSON_TYPE, objMapper.writeValueAsString(updateInternetAccessCodeDTO))
        .assertStringInBody("$.code", CTX_IAC_EXTERNAL)
        .assertStringInBody("$.createdBy", CREATED_BY)
        .assertStringInBody("$.updatedBy", CREATED_BY)
        .assertBooleanInBody("$.active", false)
        .assertResponseCodeIs(HttpStatus.OK)
        .andClose();
  }
}

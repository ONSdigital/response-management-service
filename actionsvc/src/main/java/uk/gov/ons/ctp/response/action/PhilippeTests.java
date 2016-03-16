package uk.gov.ons.ctp.response.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by philippe.brossier on 3/16/16.
 */
public class PhilippeTests {
  public static void main(final String[] args) throws Exception {
    String requestJson = "{\"createdBy\":\"\"}";
    ObjectMapper mapper = new ObjectMapper();
    ActionPlanJobDTO requestObject = mapper.readValue(requestJson, ActionPlanJobDTO.class);

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<ActionPlanJobDTO>> constraintViolations = validator.validate(requestObject);
    System.out.println(constraintViolations.size());

  }
}

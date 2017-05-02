package uk.gov.ons.ctp.response.action.export.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.Address;
import uk.gov.ons.ctp.response.action.export.service.TemplateService;

/**
 * Convenient endpoint to test manually our POC with FreeMarker templates
 *
 * Once integration tests work correctly, the endpoint will be removed.
 */
@RestController
@RequestMapping(value = "/manualtest", produces = "application/json")
@Slf4j
public class ManualTestEndpoint {

  private static final int ACTION_REQUEST_NUMBER = 51;

  @Autowired
  private TemplateService templateService;

  /**
   * To trigger the FreeMarker templating of a list of ActionRequests
   * @param templateName the name of the Template to use for templating
   * @return OK if the templating completed successfully
   * @throws CTPException if issue encountered during the FreeMarker templating
   * @throws UnsupportedEncodingException if issue encountered during the FreeMarker templating
   */
  @RequestMapping(value = "/{templateName}", method = RequestMethod.GET)
  public final ResponseEntity<?> testingFreeMarkerTemplating(@PathVariable("templateName") final String templateName)
          throws CTPException, UnsupportedEncodingException {
    log.debug("Entering testingFreeMarkerTemplating ...");
    ByteArrayOutputStream result = templateService.stream(buildMeListOfActionRequests(), templateName);
    String resultString = result.toString("UTF-8");
    log.debug("resultString = {}", resultString);

    if (!StringUtils.isEmpty(resultString)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR_500).build();
    }
  }

  /**
   * To build a list of ActionRequests
   * @return a list of ActionRequests
   */
  private static List<ActionRequestInstruction> buildMeListOfActionRequests() {
    List<ActionRequestInstruction> result = new ArrayList<>();
    for (int i = 1; i < ACTION_REQUEST_NUMBER; i++) {
      result.add(buildAMeActionRequest(i));
    }
    return result;
  }

  /**
   *  To build an ActionRequest
   * @param i the action id for the ActionRequest
   * @return an ActionRequest
   */
  private static ActionRequestInstruction buildAMeActionRequest(int i) {
    ActionRequestInstruction result =  new ActionRequestInstruction();
    result.setActionId(new BigInteger(new Integer(i).toString()));
    result.setActionType("testActionType");
    result.setIac("testIac");
    result.setAddress(buildAddress());
    return result;
  }

  /**
   * To build an Address
   * @return an Address
   */
  private static Address buildAddress() {
    Address address = new Address();
    address.setLine1("1 High Street");
    address.setTownName("Southampton");
    address.setPostcode("SO16 0AS");
    return address;
  }
}

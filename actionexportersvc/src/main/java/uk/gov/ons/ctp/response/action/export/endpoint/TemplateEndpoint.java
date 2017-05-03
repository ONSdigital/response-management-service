package uk.gov.ons.ctp.response.action.export.endpoint;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.TemplateExpression;
import uk.gov.ons.ctp.response.action.export.representation.TemplateExpressionDTO;
import uk.gov.ons.ctp.response.action.export.service.TemplateService;

/**
 * The REST endpoint controller for Templates.
 */
@RestController
@RequestMapping(value = "/templates", produces = "application/json")
@Slf4j
public class TemplateEndpoint {
  @Autowired
  private TemplateService templateService;

  @Qualifier("actionExporterBeanMapper")
  @Autowired
  private MapperFacade mapperFacade;

  /**
   * To retrieve all Templates
   * 
   * @return a list of Templates
   */
  @RequestMapping(method = RequestMethod.GET)
  public List<TemplateExpressionDTO> findAllTemplates() {
    log.debug("Entering findAllTemplates ...");
    List<TemplateExpression> templates = templateService.retrieveAllTemplates();
    List<TemplateExpressionDTO> results = mapperFacade.mapAsList(templates, TemplateExpressionDTO.class);
    return CollectionUtils.isEmpty(results) ? null : results;
  }

  /**
   * To retrieve a specific Template
   * 
   * @param templateName for the specific Template to retrieve
   * @return the specific Template
   * @throws CTPException if no Template found
   */
  @RequestMapping(value = "/{templateName}", method = RequestMethod.GET)
  public TemplateExpressionDTO findTemplate(@PathVariable("templateName") final String templateName) throws CTPException {
    log.debug("Entering findTemplate with {}", templateName);
    TemplateExpression result = templateService.retrieveTemplate(templateName);
    return mapperFacade.map(result, TemplateExpressionDTO.class);
  }

  /**
   * To store a Template
   * 
   * @param templateName the Template name
   * @param fileContents the Template content
   * @return 201 if created
   * @throws CTPException if the Template can't be stored
   */
  @RequestMapping(value = "/{templateName}", method = RequestMethod.POST, consumes = "multipart/form-data")
  public ResponseEntity<?> storeTemplate(@PathVariable("templateName") final String templateName,
                                         @RequestParam("file") InputStream fileContents) throws CTPException {
    // TODO Test and maybe InputStream fileContents should be MultipartFile file
    log.debug("Entering storeTemplate with templateName {}", templateName);
    TemplateExpression template = templateService.storeTemplate(templateName, fileContents);

    TemplateExpressionDTO templateDTO = mapperFacade.map(template, TemplateExpressionDTO.class);
    return ResponseEntity.created(URI.create("TODO")).body(templateDTO);
  }
}

package uk.gov.ons.ctp.response.action.export.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.TemplateExpression;

/**
 * Service responsible for dealing with Templates (storage/retrieval in/from MongoDB, templating)
 */
public interface TemplateService {
  /**
   * To store a Template
   *
   * @param templateName the Template name
   * @param fileContents the Template content
   * @return the Template stored
   * @throws CTPException if the Template content is empty
   */
  TemplateExpression storeTemplate(String templateName, InputStream fileContents)
          throws CTPException;

  /**
   * To retrieve a given Template
   *
   * @param templateName the Template name to be retrieved
   * @return the given Template
   */
  TemplateExpression retrieveTemplate(String templateName);

  /**
   * To retrieve all Templates
   *
   * @return a list of Templates
   */
  List<TemplateExpression> retrieveAllTemplates();

  /**
   * This produces a csv file for all our action requests.
   *
   * @param actionRequestList the list of action requests.
   * @param templateName the FreeMarker template to use.
   * @param path the full file path. An example is /tmp/csv/forPrinter.csv.
   * @throws CTPException if problem creating file from template.
   * @return the file.
   */
  File file(List<ActionRequestInstruction> actionRequestList, String templateName, String path)
          throws CTPException;

  /**
   * This produces a stream for all our action requests.
   *
   * @param actionRequestList the list of action requests.
   * @param templateName the FreeMarker template to use.
   * @throws CTPException if problem creating stream from template.
   * @return the stream.
   */
  ByteArrayOutputStream stream(List<ActionRequestInstruction> actionRequestList, String templateName)
          throws CTPException;

}

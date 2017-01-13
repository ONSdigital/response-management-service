package uk.gov.ons.ctp.response.action.export.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMappingDocument;

/**
 * Service responsible for dealing with stored TemplateMappingDocuments.
 *
 */
public interface TemplateMappingService {

  /**
   * To store TemplateMappings
   *
   * @param fileContents the TemplateMappingDocument content
   * @return the stored TemplateMappingDocument
   * @throws CTPException if the TemplateMappingDocument content is empty or error storing mapping
   */
  List<TemplateMappingDocument> storeTemplateMappingDocument(InputStream fileContents)
      throws CTPException;

  /**
   * To retrieve a given TemplateMappingDocument
   *
   * @param templateMappingName the TemplateMappingDocument name to be retrieved
   * @return the given TemplateMappingDocument
   * @throws CTPException if the TemplateMappingDocument is not found.
   */
  TemplateMappingDocument retrieveTemplateMappingDocument(String templateMappingName) throws CTPException;

  /**
   * To retrieve all TemplateMappings
   *
   * @return a list of TemplateMappingDocuments
   */
  List<TemplateMappingDocument> retrieveAllTemplateMappingDocuments();

  /**
   * To retrieve a Map of TemplateMappings by filename.
   *
   * @return the Map of TemplateMappingDocuments by filename.
   */
  Map<String, List<TemplateMappingDocument>> retrieveTemplateMappingByFilename();

  /**
   * To retrieve a Map of TemplateMappings by actionType.
   *
   * @return the Map of TemplateMappingDocuments by actionType.
   */
  Map<String, TemplateMappingDocument> retrieveTemplateMappingByActionType();

  /**
   * Return a list of distinct actionTypes in collection
   *
   * @return a list of actionTypes.
   */
  List<String> retrieveActionTypes();

}

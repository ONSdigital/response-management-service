package uk.gov.ons.ctp.response.action.export.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMapping;

/**
 * Service responsible for dealing with stored TemplateMappings.
 *
 */
public interface TemplateMappingService {

  /**
   * To store TemplateMappings
   *
   * @param fileContents the TemplateMappings content to be stored
   * @return the stored TemplateMappings
   * @throws CTPException if error storing TemplateMappings
   */
  List<TemplateMapping> storeTemplateMappings(InputStream fileContents)
      throws CTPException;

  /**
   * To retrieve a given TemplateMapping
   *
   * @param actionType for which to retrieve the TemplateMapping
   * @return the given TemplateMapping
   * @throws CTPException if the TemplateMapping is not found.
   */
  TemplateMapping retrieveTemplateMappingByActionType(String actionType) throws CTPException;

  /**
   * To retrieve all TemplateMappings
   *
   * @return a list of TemplateMappings
   */
  List<TemplateMapping> retrieveAllTemplateMappings();

  /**
   * To retrieve a Map of TemplateMappings by filename.
   *
   * @return the Map of TemplateMappings by filename.
   */
  Map<String, List<TemplateMapping>> retrieveAllTemplateMappingsByFilename();

  /**
   * To retrieve a Map of TemplateMappings by actionType.
   *
   * @return the Map of TemplateMappings by actionType.
   */
  Map<String, TemplateMapping> retrieveAllTemplateMappingsByActionType();

  /**
   * Return a list of distinct actionTypes in the collection
   *
   * @return a list of actionTypes.
   */
  List<String> retrieveActionTypes();

}

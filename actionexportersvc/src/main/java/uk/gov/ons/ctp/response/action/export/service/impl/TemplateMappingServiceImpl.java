package uk.gov.ons.ctp.response.action.export.service.impl;

import static uk.gov.ons.ctp.common.util.InputStreamUtils.getStringFromInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMapping;
import uk.gov.ons.ctp.response.action.export.repository.TemplateMappingRepository;
import uk.gov.ons.ctp.response.action.export.service.TemplateMappingService;

/**
 * The implementation of the TemplateMappingService
 */
@Named
@Slf4j
public class TemplateMappingServiceImpl implements TemplateMappingService {

  public static final String EXCEPTION_STORE_TEMPLATE_MAPPING = "Issue storing TemplateMapping. Appears to be empty.";

  public static final String EXCEPTION_RETRIEVING_TEMPLATE_MAPPING = "TemplateMapping not found.";

  @Inject
  private TemplateMappingRepository repository;

  @Override
  public List<TemplateMapping> storeTemplateMappings(InputStream fileContents)
      throws CTPException {
    List<TemplateMapping> mapping = new ArrayList<TemplateMapping>();
    String stringValue = getStringFromInputStream(fileContents);
    if (StringUtils.isEmpty(stringValue)) {
      log.error(EXCEPTION_STORE_TEMPLATE_MAPPING);
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, EXCEPTION_STORE_TEMPLATE_MAPPING);
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapping = mapper.readValue(stringValue, new TypeReference<List<TemplateMapping>>() {
      });
    } catch (JsonParseException e) {
      log.error("JsonParseException thrown while parsing mapping...", e.getMessage());
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e.getMessage());
    } catch (JsonMappingException e) {
      log.error("JsonMappingException thrown while parsing mapping...", e.getMessage());
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e.getMessage());
    } catch (IOException e) {
      log.error("IOException thrown while parsing mapping...", e.getMessage());
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, e.getMessage());
    }
    mapping.forEach((templateMapping) -> {
      templateMapping.setDateModified(new Date());
    });
    return repository.save(mapping);
  }

  @Override
  public TemplateMapping retrieveTemplateMappingByActionType(String actionType) throws CTPException {
    TemplateMapping templateMapping = repository.findOne(actionType);
    if (templateMapping == null) {
      log.error("No template mapping for actionType {} found.", actionType);
      throw new CTPException(CTPException.Fault.RESOURCE_NOT_FOUND,
          String.format("%s %s", EXCEPTION_RETRIEVING_TEMPLATE_MAPPING, actionType));
    }
    return templateMapping;
  }

  @Override
  public List<TemplateMapping> retrieveAllTemplateMappings() {
    return repository.findAll();
  }

  @Override
  public Map<String, List<TemplateMapping>> retrieveAllTemplateMappingsByFilename() {
    return retrieveAllTemplateMappings().stream()
        .collect(Collectors.groupingBy(TemplateMapping::getFile));
  }

  @Override
  public Map<String, TemplateMapping> retrieveAllTemplateMappingsByActionType() {
    Map<String, TemplateMapping> mappings = new HashMap<String, TemplateMapping>();
    retrieveAllTemplateMappings().forEach((templateMapping) -> {
      mappings.put(templateMapping.getActionType(), templateMapping);
    });
    return mappings;
  }

  @Override
  public List<String> retrieveActionTypes() {
    return repository.findAllActionType();
  }

}

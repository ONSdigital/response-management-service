package uk.gov.ons.ctp.response.action.utility;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by philippe.brossier on 3/9/16.
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ActionPlanDTOMessageBodyReader implements MessageBodyReader<ActionPlanDTO> {

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return type == ActionPlanDTO.class;
  }

  @Override
  public ActionPlanDTO readFrom(Class<ActionPlanDTO> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
      throws IOException, WebApplicationException {
    try {
      log.debug("Entering readFrom...");
      JAXBContext jaxbContext = JAXBContext.newInstance(ActionPlanDTO.class);
      ActionPlanDTO actionPlanDTO = (ActionPlanDTO) jaxbContext.createUnmarshaller().unmarshal(entityStream);
      return actionPlanDTO;
    } catch (JAXBException jaxbException) {
      log.error("Error deserializing an ActionPlanDTO. {}", jaxbException);
      // We return null here so we can then throw the right CTPException in the controller
      return null;
    }
  }
}
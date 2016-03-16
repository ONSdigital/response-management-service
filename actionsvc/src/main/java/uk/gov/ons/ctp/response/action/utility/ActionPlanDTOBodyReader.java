package uk.gov.ons.ctp.response.action.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by philippe.brossier on 3/16/16.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ActionPlanDTOBodyReader implements MessageBodyReader<ActionPlanDTO> {

  // required due to type erasure
  //private final Class<T> theType;

  //public CTPMessageBodyReader(Class<T> aType) {
  //    this.theType = aType;
  //  }

  @Override
  public final boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return true;
  }

  @Override
  public final ActionPlanDTO readFrom(final Class<ActionPlanDTO> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders,
      final InputStream entityStream) throws IOException, WebApplicationException {
    log.debug("Entering readFrom ...");
    try {
      StringWriter writer = new StringWriter();
      IOUtils.copy(entityStream, writer, "UTF-8");
      String requestJson = writer.toString();
      log.debug("requestJson = {}", requestJson);

      ObjectMapper mapper = new ObjectMapper();
      ActionPlanDTO requestObject = mapper.readValue(requestJson, ActionPlanDTO.class);
      log.debug("requestObject = {}", requestObject);
      return requestObject;
    } catch (Exception e) {
      log.error("Exception thrown while reading request body - {}", e);
      return null;
    }
  }
}

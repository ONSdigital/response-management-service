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
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by philippe.brossier on 3/9/16.
 *
 * This class is required by the ActionPlanEndpoint. The method updateActionPlanByActionPlanId expects a valid
 * ActionPlanDTO, ie the json provided in the request body must contains fields present on the ActionPlanDTO. If this is
 * not the case, a JAX-RS exception is thrown and we can not control the message.
 * Thanks to the ActionPlanDTOMessageBodyReader, we verify the json ourselves. Null is returned if the json is not
 * valid. This is then checked in ActionPlanEndpoint where an appropriate CTPException is thrown.
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ActionPlanDTOMessageBodyReader implements MessageBodyReader<ActionPlanDTO> {

  @Override
  public final boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType) {
    return type == ActionPlanDTO.class;
  }

  @Override
  public final ActionPlanDTO readFrom(final Class<ActionPlanDTO> type, final Type genericType,
      final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders,
      final InputStream entityStream) throws IOException, WebApplicationException {
    log.debug("Entering readFrom...");
    try {
      StringWriter writer = new StringWriter();
      IOUtils.copy(entityStream, writer, "UTF-8");
      String requestJson = writer.toString();
      log.debug("requestJson = {}", requestJson);

      ObjectMapper mapper = new ObjectMapper();
      ActionPlanDTO requestObject = mapper.readValue(requestJson, ActionPlanDTO.class);
      return requestObject;
    } catch (Exception e) {
      log.error("Exception thrown while reading request body - {}", e);
      return null;
    }
  }
}

package uk.gov.ons.ctp.response.action.utility;

import com.google.gson.Gson;
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
    log.debug("Entering readFrom...");
    try {
      StringWriter writer = new StringWriter();
      IOUtils.copy(entityStream, writer, "UTF-8");
      String json = writer.toString();
      log.debug("json = {}", json);
      return new Gson().fromJson(json, ActionPlanDTO.class);
    } catch (Exception e){
      log.error("Exception thrown while reading request''s body - {}", e);
      return null;
    }
  }
}
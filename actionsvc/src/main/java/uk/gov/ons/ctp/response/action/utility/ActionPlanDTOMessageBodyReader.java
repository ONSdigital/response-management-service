package uk.gov.ons.ctp.response.action.utility;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

/**
 * Created by philippe.brossier on 3/9/16.
 *
 * This class is required by the ActionPlanEndpoint. The method updateActionPlanByActionPlanId expects a valid
 * ActionPlanDTO, ie the json provided in the request body must contains fields present on the ActionPlanDTO. If this is
 * not the case, a JAX-RS exception is thrown and we can not control the message.
 * Thanks to the ActionPlanDTOMessageBodyReader, we verify the json ourselves. Null is returned if the json is not
 * valid. This is then checked in ActionPlanEndpoint where an appropriate CTPException is thrown.
 */
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class ActionPlanDTOMessageBodyReader extends CTPMessageBodyReader<ActionPlanDTO> {

  public ActionPlanDTOMessageBodyReader(Class<ActionPlanDTO> aType) {
    super(aType);
  }
}

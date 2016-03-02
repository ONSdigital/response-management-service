package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;

@Named
public class ActionBeanMapper extends ConfigurableMapper {
  protected void configure(MapperFactory factory) {

    factory
      .classMap(Action.class, ActionDTO.class)
      .byDefault()
      .register();

    factory
      .classMap(ActionPlan.class, ActionPlanDTO.class)
      .byDefault()
      .register();


  }
}

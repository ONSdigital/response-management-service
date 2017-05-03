package uk.gov.ons.ctp.response.action;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import org.springframework.stereotype.Component;
import uk.gov.ons.ctp.response.action.domain.model.Action;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlan;
import uk.gov.ons.ctp.response.action.domain.model.ActionPlanJob;
import uk.gov.ons.ctp.response.action.domain.model.ActionRule;
import uk.gov.ons.ctp.response.action.representation.ActionDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanDTO;
import uk.gov.ons.ctp.response.action.representation.ActionPlanJobDTO;
import uk.gov.ons.ctp.response.action.representation.ActionRuleDTO;

/**
 * The bean mapper to go from Entity objects to Presentation objects.
 */
@Component
public class ActionBeanMapper extends ConfigurableMapper {

  @Override
  public void configureFactoryBuilder(DefaultMapperFactory.Builder builder) {
    builder.compilerStrategy(new EclipseJdtCompilerStrategy());
  }

  /**
   * This method configures the bean mapper.
   *
   * @param factory the mapper factory
   */
  @Override
  protected final void configure(final MapperFactory factory) {
    factory
        .classMap(Action.class, ActionDTO.class)
        .field("actionType.name", "actionTypeName")
        .byDefault()
        .register();

    factory
        .classMap(ActionPlan.class, ActionPlanDTO.class)
        .byDefault()
        .register();

    factory
        .classMap(ActionPlanJob.class, ActionPlanJobDTO.class)
        .byDefault()
        .register();

    factory
        .classMap(ActionRule.class, ActionRuleDTO.class)
        .field("actionType.name", "actionTypeName")
        .byDefault()
        .register();
  }
}

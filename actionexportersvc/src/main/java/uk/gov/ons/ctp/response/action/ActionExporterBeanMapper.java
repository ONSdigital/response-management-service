package uk.gov.ons.ctp.response.action;

import javax.inject.Named;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.generator.EclipseJdtCompilerStrategy;
import uk.gov.ons.ctp.response.action.export.domain.ActionRequestInstruction;
import uk.gov.ons.ctp.response.action.export.domain.TemplateExpression;
import uk.gov.ons.ctp.response.action.export.domain.TemplateMapping;
import uk.gov.ons.ctp.response.action.export.representation.ActionRequestInstructionDTO;
import uk.gov.ons.ctp.response.action.export.representation.TemplateExpressionDTO;
import uk.gov.ons.ctp.response.action.export.representation.TemplateMappingDTO;

/**
 * The bean mapper to go from Entity objects to Presentation objects.
 */
@Named
public class ActionExporterBeanMapper extends ConfigurableMapper {

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
            .classMap(TemplateExpression.class, TemplateExpressionDTO.class)
            .byDefault()
            .register();

    factory
            .classMap(TemplateMapping.class, TemplateMappingDTO.class)
            .byDefault()
            .register();

    factory
            .classMap(ActionRequestInstruction.class, ActionRequestInstructionDTO.class)
            .byDefault()
            .register();
  }
}

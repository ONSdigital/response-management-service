package uk.gov.ons.ctp.response.action.message;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootConfiguration
@ImportResource(locations = { "classpath:springintegration/InstructionPublisherSITest-context.xml" })
public class InstructionPublisherSITestConfig {
}

package uk.gov.ons.ctp.response.action.message;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootConfiguration
@ImportResource(locations = { "classpath:springintegration/InstructionPublisherITCase-context.xml" })
public class InstructionPublisherITCaseConfig {
}

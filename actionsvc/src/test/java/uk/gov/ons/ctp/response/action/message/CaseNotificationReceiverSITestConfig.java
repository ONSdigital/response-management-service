package uk.gov.ons.ctp.response.action.message;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ImportResource;

@SpringBootConfiguration
@ImportResource(locations = { "classpath:springintegration/CaseNotificationReceiverSITest-context.xml" })
public class CaseNotificationReceiverSITestConfig {
}

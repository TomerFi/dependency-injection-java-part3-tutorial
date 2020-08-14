package info.tomfi.tutorials.mailapp;

import static org.mockito.Mockito.mock;

import info.tomfi.tutorials.mailapp.core.MailEngine;
import info.tomfi.tutorials.mailapp.core.MailService;
import info.tomfi.tutorials.mailapp.engine.RobustMailEngine;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DITestConfiguration {
  private MailService gmailServiceMock;
  private MailService microsoftServiceMock;
  private MailService thirdServiceMock;

  public DITestConfiguration() {
    gmailServiceMock = mock(MailService.class);
    microsoftServiceMock = mock(MailService.class);
    thirdServiceMock = mock(MailService.class);
  }

  @Lazy
  @Bean
  public MailEngine getEngine(final Set<MailService> services) {
    return new RobustMailEngine(Set.of(gmailServiceMock, microsoftServiceMock, thirdServiceMock));
  }

  public MailService getGmailServiceMock() {
    return gmailServiceMock;
  }

  public MailService getMicrosoftServiceMock() {
    return microsoftServiceMock;
  }

  public MailService getThirdServiceMock() {
    return thirdServiceMock;
  }
}

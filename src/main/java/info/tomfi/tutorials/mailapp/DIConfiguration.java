package info.tomfi.tutorials.mailapp;

import info.tomfi.tutorials.mailapp.core.MailEngine;
import info.tomfi.tutorials.mailapp.core.MailService;
import info.tomfi.tutorials.mailapp.core.service.GmailService;
import info.tomfi.tutorials.mailapp.core.service.MicrosoftService;
import info.tomfi.tutorials.mailapp.engine.RobustMailEngine;
import java.util.Set;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class DIConfiguration {
  @Bean
  @Scope(BeanDefinition.SCOPE_PROTOTYPE)
  public Set<MailService> getServices() {
    return Set.of(new GmailService(), new MicrosoftService());
  }

  @Lazy
  @Bean
  public MailEngine getEngine(final Set<MailService> services) {
    return new RobustMailEngine(services);
  }
}

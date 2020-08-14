package info.tomfi.tutorials.mailapp;

import com.google.common.base.Joiner;
import info.tomfi.tutorials.mailapp.core.MailEngine;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public final class MailCollectorApp {
  private MailEngine engine;

  public MailCollectorApp(final MailEngine setEngine) {
    engine = setEngine;
  }

  public String getMail() {
    var ret = "No mail found.";
    if (!engine.getAllMail().isEmpty()) {
      ret = Joiner.on(System.lineSeparator()).join(engine.getAllMail());
    }
    return ret;
  }

  public static void main(final String... args) {
    // try (var container = new ClassPathXmlApplicationContext("spring-beans.xml")) {
    try (var container =
        new AnnotationConfigApplicationContext(MailCollectorApp.class, DIConfiguration.class)) {
      var app = container.getBean(MailCollectorApp.class);

      System.out.println(app.getMail());
    }
  }
}

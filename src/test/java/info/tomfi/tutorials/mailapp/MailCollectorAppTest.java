package info.tomfi.tutorials.mailapp;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.willReturn;

import com.github.javafaker.Faker;
import info.tomfi.tutorials.mailapp.core.MailService;
import info.tomfi.tutorials.mailapp.core.mail.GmailImpl;
import info.tomfi.tutorials.mailapp.core.mail.MicrosoftImpl;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class MailCollectorAppTest {
  private MailService gmailServiceMock;
  private MailService microsoftServiceMock;
  private MailService thirdServiceMock;

  private MailCollectorApp sut;

  private ConfigurableApplicationContext context;
  private Faker faker;

  @BeforeEach
  public void initialize() {
    faker = new Faker();

    context =
        new AnnotationConfigApplicationContext(MailCollectorApp.class, DITestConfiguration.class);

    var confWorkAround = context.getBean(DITestConfiguration.class);

    gmailServiceMock = confWorkAround.getGmailServiceMock();
    microsoftServiceMock = confWorkAround.getMicrosoftServiceMock();
    thirdServiceMock = confWorkAround.getThirdServiceMock();

    sut = context.getBean(MailCollectorApp.class);
  }

  @AfterEach
  public void cleanup() {
    context.close();
  }

  @Test
  @DisplayName(
      "make the services mocks return no mail and validate the return string as 'No mail found'")
  public void getMail_noMailExists_returnsNoMailFound() {
    willReturn(emptyList()).given(gmailServiceMock).getMail();
    willReturn(emptyList()).given(microsoftServiceMock).getMail();
    willReturn(emptyList()).given(thirdServiceMock).getMail();

    then(sut.getMail()).isEqualTo("No mail found.");
  }

  @Test
  @DisplayName(
      "make the services return legitimate mail and validate the return string as expected")
  public void getMail_foundMail_returnsExpectedString() {
    var mail1 =
        GmailImpl.builder()
            .from(faker.internet().emailAddress())
            .subject(faker.lorem().sentence())
            .build();
    var mail2 =
        MicrosoftImpl.builder()
            .from(faker.internet().emailAddress())
            .subject(faker.lorem().sentence())
            .build();
    var mail3 =
        MicrosoftImpl.builder()
            .from(faker.internet().emailAddress())
            .subject(faker.lorem().sentence())
            .build();

    willReturn(List.of(mail1)).given(gmailServiceMock).getMail();
    willReturn(List.of(mail2, mail3)).given(microsoftServiceMock).getMail();
    willReturn(emptyList()).given(thirdServiceMock).getMail();

    then(sut.getMail().split(System.lineSeparator()))
        .containsOnly(mail1.toString(), mail2.toString(), mail3.toString());
  }
}

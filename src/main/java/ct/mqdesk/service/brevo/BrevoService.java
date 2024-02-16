package ct.mqdesk.service.brevo;

import ct.mqdesk.entity.MQDeskAccount;
import ct.mqdesk.entity.Profile;
import ct.mqdesk.records.brevo.Contact;
import ct.mqdesk.records.brevo.Message;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class BrevoService {
    public static final String ADMINISTRATOR_NAME = "Achille de chillo.tech";
    public static final String ADMINISTRATOR_EMAIL = "achille.mbougueng@chillo.tech";
    private SpringTemplateEngine springTemplateEngine;
    private BrevoClient brevoClient;

    @Async
    public void sendIncriptionEmails(final MQDeskAccount mqDeskAccount, final String password) {
        final Profile profile = mqDeskAccount.getProfile();
        final String customer;
        if (Strings.isNotEmpty(profile.getFirstName())) {
            customer = String.format(
                    "%s%s %s",
                    profile.getFirstName().charAt(0),
                    profile.getFirstName().substring(1).toLowerCase(),
                    profile.getLastName().toUpperCase()
            );
        } else {
            customer = profile.getLastName().toUpperCase();
        }
        final Context context = new Context();
        context.setVariable("customer", customer);
        context.setVariable("password", password);
        context.setVariable("username", mqDeskAccount.getUsername());

        String htmlBody = this.springTemplateEngine.process("customer-account.html", context);
        Message message = new Message(
                "Bonne exploitation de RabbitMQ sur mqdesk.io",
                htmlBody,
                new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL),
                Set.of(new Contact(customer, profile.getEmail()))
        );
        this.brevoClient.message(message);

        this.sendPassword(mqDeskAccount, password);

        htmlBody = this.springTemplateEngine.process("customer-account-admin-notification.html", context);
        message = new Message(
                "Nouveau compte sur mqdesk.io",
                htmlBody,
                new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL),
                Set.of(new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL))
        );
        this.brevoClient.message(message);

    }

    public void sendPassword(final MQDeskAccount mqDeskAccount, final String password) {
        final Profile profile = mqDeskAccount.getProfile();
        final String customer;
        if (Strings.isNotEmpty(profile.getFirstName())) {
            customer = String.format(
                    "%s%s %s",
                    profile.getFirstName().charAt(0),
                    profile.getFirstName().substring(1).toLowerCase(),
                    profile.getLastName().toUpperCase()
            );
        } else {
            customer = profile.getLastName().toUpperCase();
        }

        final Context context = new Context();
        context.setVariable("customer", customer);
        context.setVariable("password", password);
        context.setVariable("username", mqDeskAccount.getUsername());
        final String htmlBody = this.springTemplateEngine.process("customer-password.html", context);
        final Message message = new Message(
                "Votre mot de passe pour RabbitMQ sur mqdesk.io",
                htmlBody,
                new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL),
                Set.of(new Contact(customer, profile.getEmail()))
        );
        this.brevoClient.message(message);
    }

    public void sendMessage(final Map<String, String> messageParams) {

        final Context context = new Context();
        for (final String key : messageParams.keySet()) {
            context.setVariable(key, messageParams.get(key));
        }
        final String htmlBody = this.springTemplateEngine.process("customer-message.html", context);
        final Message message = new Message(
                "Nouveau message sur mqdesk.io",
                htmlBody,
                new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL),
                Set.of(new Contact(BrevoService.ADMINISTRATOR_NAME, BrevoService.ADMINISTRATOR_EMAIL))
        );
        this.brevoClient.message(message);
    }
}

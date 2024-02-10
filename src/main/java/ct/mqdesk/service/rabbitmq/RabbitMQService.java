package ct.mqdesk.service.rabbitmq;

import ct.mqdesk.entity.MQDeskAccount;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class RabbitMQService {

    private RabbitMQClient rabbitMQClient;

    @Async
    public void createCurstomerVhost(final MQDeskAccount mqDeskAccount, final String password) {
        this.rabbitMQClient.createVhost(mqDeskAccount.getUsername());
        this.createUser(mqDeskAccount, password);
        this.rabbitMQClient.setUserPermissions(
                mqDeskAccount.getUsername(), mqDeskAccount.getUsername(),
                Map.of(
                        "configure", ".*",
                        "read", ".*",
                        "write", ".*"
                )
        );
        this.setPolicies(mqDeskAccount);
    }

    public void createUser(final MQDeskAccount mqDeskAccount, final String password) {
        this.rabbitMQClient.createUser(mqDeskAccount.getUsername(), Map.of("password", password, "tags", "management"));
    }

    public void setPolicies(final MQDeskAccount mqDeskAccount) {

        this.rabbitMQClient.setPolicies(
                mqDeskAccount.getUsername(),
                "queues-max-3-exchanges",
                Map.of(
                        "pattern", ".*",
                        "apply-to", "exchanges",
                        "priority", 0,
                        "definition", Map.of(
                                "max-length", 3,
                                "overflow", "reject-publish"

                        )
                )
        );
        this.rabbitMQClient.setPolicies(
                mqDeskAccount.getUsername(),
                "queues-max-10-messages",
                Map.of(
                        "pattern", ".*",
                        "apply-to", "queues",
                        "priority", 0,
                        "definition", Map.of(
                                "max-length", 10,
                                "overflow", "reject-publish"

                        )
                )
        );
    }
}

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
        this.rabbitMQClient.createUser(mqDeskAccount.getUsername(), Map.of("password", password, "tags", "management"));
        this.rabbitMQClient.setUserPermissions(
                mqDeskAccount.getUsername(), mqDeskAccount.getUsername(),
                Map.of(
                        "configure", ".*",
                        "read", ".*",
                        "write", ".*"
                )
        );
    }
}

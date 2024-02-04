package ct.mqdesk.service.rabbitmq;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "rabbitmqmessages", url = "${providers.rabbitmq.host}", configuration = RabbitMQClientConfiguration.class)
public interface RabbitMQClient {

    @PutMapping(path = "/api/vhosts/{vhost}")
    void createVhost(@PathVariable String vhost);

    @PutMapping(path = "/api/users/{user}")
    void createUser(@PathVariable String user, @RequestBody Map<String, String> data);

    @PutMapping(path = "/api/permissions/{vhost}/{user}")
    void setUserPermissions(@PathVariable String vhost, @PathVariable String user, @RequestBody Map<String, String> data);

    @DeleteMapping(path = "/api/users/{user}")
    void deleteUser(@PathVariable String user);
}

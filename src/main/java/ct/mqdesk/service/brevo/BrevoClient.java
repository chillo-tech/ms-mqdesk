package ct.mqdesk.service.brevo;

import ct.mqdesk.records.brevo.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "brevomessages", url = "${providers.brevo.host}")
public interface BrevoClient {

    @PostMapping(path = "/${providers.brevo.path}")
    Map<String, Object> message(@RequestBody Message message);
}

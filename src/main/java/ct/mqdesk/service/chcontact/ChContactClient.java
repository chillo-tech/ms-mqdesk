package ct.mqdesk.service.chcontact;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ChContactessages", url = "${providers.ch-contact.host}")
public interface ChContactClient {

    @PostMapping(path = "/${providers.ch-contact.path}")
    Map<String, Object> sendMessage(@RequestBody Map<String, String> params);
}

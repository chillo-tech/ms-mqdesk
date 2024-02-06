package ct.mqdesk.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Value("${providers.brevo.token}")
    String brevoToken;
    @Value("${providers.ch-contact.token}")
    String chContactToken;

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        FeignRequestInterceptor.log.info("Intercept request {}", requestTemplate.feignTarget().name());
        requestTemplate.header("content-type", APPLICATION_JSON_VALUE);
        requestTemplate.header("produces", APPLICATION_JSON_VALUE);

        if (requestTemplate.feignTarget().name().equalsIgnoreCase("brevomessages")) {
            requestTemplate.header("api-key", this.brevoToken);
        }
        if (requestTemplate.feignTarget().name().equalsIgnoreCase("ChContactessages")) {
            requestTemplate.header("Authorization", String.format("Bearer %s", this.chContactToken));
        }

    }
}

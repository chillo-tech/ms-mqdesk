package ct.mqdesk.service.rabbitmq;

import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQClientConfiguration {


    @Value("${providers.rabbitmq.username}")
    String rabbitmqUsername;

    @Value("${providers.rabbitmq.password}")
    String rabbitmqPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(this.rabbitmqUsername, this.rabbitmqPassword);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

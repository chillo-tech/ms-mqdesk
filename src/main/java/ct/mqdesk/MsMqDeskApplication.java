package ct.mqdesk;

import ct.mqdesk.service.ClientApplicationService;
import feign.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@SpringBootApplication
public class MsMqDeskApplication {
    @Autowired
    private ClientApplicationService clientApplicationService;

    public static void main(final String[] args) {
        SpringApplication.run(MsMqDeskApplication.class, args);
    }

    /*
        @Bean
        public CommandLineRunner startup() {

            return args ->
                    this.clientApplicationService.generate("front-mqdesk");

        }
    */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}

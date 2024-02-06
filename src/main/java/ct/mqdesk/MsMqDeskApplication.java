package ct.mqdesk;

import ct.mqdesk.service.ClientApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
}

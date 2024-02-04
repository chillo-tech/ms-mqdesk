package ct.mqdesk.service;

import ct.mqdesk.entity.ClientApplication;
import ct.mqdesk.repository.ClientApplicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ClientApplicationService {
    private TokenService tokenService;
    private ClientApplicationRepository clientApplicationRepository;

    public void generate(final String name) {
        ClientApplication clientApplication = new ClientApplication();
        clientApplication.setName(name);
        clientApplication = this.clientApplicationRepository.save(clientApplication);
        this.tokenService.generate(clientApplication);
    }
}

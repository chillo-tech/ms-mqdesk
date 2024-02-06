package ct.mqdesk.service;

import ct.mqdesk.entity.MQDeskAccount;
import ct.mqdesk.repository.MQDeskAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MQDeskAccountService {
    private MQDeskAccountRepository mqDeskAccountRepository;

    public MQDeskAccount save(final MQDeskAccount mqDeskAccount) {
        return this.mqDeskAccountRepository.save(mqDeskAccount);
    }

    public MQDeskAccount readUserAccount(final String email) {
        final Optional<MQDeskAccount> mqDeskAccountOptional = this.mqDeskAccountRepository.findByProfileEmail(email);
        if (mqDeskAccountOptional.isEmpty()) {
            throw new IllegalArgumentException("Votre mail est inconnu");
        }
        return mqDeskAccountOptional.get();
    }
}

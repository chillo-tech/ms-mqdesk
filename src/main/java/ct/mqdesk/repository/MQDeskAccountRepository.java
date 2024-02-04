package ct.mqdesk.repository;

import ct.mqdesk.entity.MQDeskAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MQDeskAccountRepository extends CrudRepository<MQDeskAccount, Integer> {
    Optional<MQDeskAccount> findByProfileEmail(String email);
}

package ct.mqdesk.repository;

import ct.mqdesk.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Integer> {
    Optional<Token> findByValue(String value);

    Optional<Token> findByValueAndActive(String value, boolean active);

    Optional<Token> findByApplicationNameAndActive(String application, boolean active);

}

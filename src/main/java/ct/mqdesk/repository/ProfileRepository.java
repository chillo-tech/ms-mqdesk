package ct.mqdesk.repository;

import ct.mqdesk.entity.Profile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {
    Optional<Profile> findByEmail(String email);

}

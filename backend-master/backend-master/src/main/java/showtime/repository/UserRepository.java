package showtime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import showtime.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByEmail(String email);
}

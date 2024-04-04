package org.greenhouse.repository.user;

import java.util.Optional;
import org.greenhouse.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

}

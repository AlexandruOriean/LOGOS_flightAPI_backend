package com.codecool.flightapi.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User getById(long id);

    Optional<User> getByUsername(String username);

    User getByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

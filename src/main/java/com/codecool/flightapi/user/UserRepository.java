package com.codecool.flightapi.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

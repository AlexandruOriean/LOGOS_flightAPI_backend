package com.codecool.flightapi.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/** Registration depends on every role being present in the seed data. */
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @ParameterizedTest
    @EnumSource(RoleName.class)
    @DisplayName("every role used by registration is seeded")
    void everyRoleIsSeeded(RoleName role) {
        assertThat(roleRepository.findByName(role)).isPresent();
    }
}

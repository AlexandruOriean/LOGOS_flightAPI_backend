package com.codecool.flightapi.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecool.flightapi.airport.AirportRepository;
import com.codecool.flightapi.flight.FlightRepository;
import com.codecool.flightapi.user.RoleName;
import com.codecool.flightapi.user.RoleRepository;
import java.time.LocalDate;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Boots the full application against a real PostgreSQL (the production database) to prove that
 * the Flyway migrations apply cleanly, that Hibernate's schema validation passes, and that the
 * native queries work on PostgreSQL. Skipped automatically when Docker is not available.
 */
@SpringBootTest(
        properties =
                // Test-only key (64 random bytes, Base64). Never use outside tests.
                "app.jwt.secret=dGVzdC1vbmx5LWtleS1kby1ub3QtdXNlLWluLXByb2R1Y3Rpb24tMDEyMzQ1Njc4OWFiY2RlZmdoaWprbG1ub3A=")
@Testcontainers(disabledWithoutDocker = true)
class PostgresMigrationIT {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17-alpine");

    @Autowired
    private Flyway flyway;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("all migrations are applied on PostgreSQL")
    void migrationsAreApplied() {
        assertThat(flyway.info().pending()).isEmpty();
        assertThat(flyway.info().current().getVersion().getVersion()).isEqualTo("2");
    }

    @Test
    @DisplayName("seed data and native queries work on PostgreSQL")
    void nativeQueriesWorkOnPostgres() {
        assertThat(airportRepository.getAirportByIataCode("otp").getAirportName())
                .isEqualTo("Henri Coanda International Airport");
        assertThat(flightRepository.getFlightByParams("London", "Henri", LocalDate.of(2020, 11, 1)))
                .hasSize(1);
        assertThat(roleRepository.findByName(RoleName.ROLE_ADMIN)).isPresent();
    }
}

package com.codecool.flightapi.airport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/** Native airport queries against the schema and seed data from the real Flyway migrations (embedded H2). */
@DataJpaTest
class AirportRepositoryTest {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("IATA lookup is case-insensitive")
    void findsAirportByIataCodeIgnoringCase() {
        Airport airport = airportRepository.getAirportByIataCode("otp");

        assertThat(airport).isNotNull();
        assertThat(airport.getAirportName()).isEqualTo("Henri Coanda International Airport");
        assertThat(airport.getCity().getCityName()).isEqualTo("Bucuresti");
    }

    @Test
    @DisplayName("IATA lookup returns null for an unknown code")
    void unknownIataCodeReturnsNull() {
        assertThat(airportRepository.getAirportByIataCode("XXX")).isNull();
    }

    @Test
    @DisplayName("airport names are returned for every seeded airport")
    void returnsAllAirportNames() {
        assertThat(airportRepository.getAllAirportsNames())
                .hasSize(4)
                .contains("Henri Coanda International Airport", "London Heathrow Airport");
    }
}

package com.codecool.flightapi.airport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
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

    @ParameterizedTest
    @CsvSource({"CDG, Charles de Gaulle International Airport", "TSR, Timisoara Traian Vuia Airport"})
    @DisplayName("corrected seed IATA codes resolve to the right airport")
    void correctedIataCodesResolve(String iataCode, String airportName) {
        assertThat(airportRepository.getAirportByIataCode(iataCode).getAirportName())
                .isEqualTo(airportName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"CHD", "TVT"})
    @DisplayName("the wrong IATA codes from the original seed data are gone")
    void wrongSeedIataCodesAreGone(String wrongCode) {
        assertThat(airportRepository.getAirportByIataCode(wrongCode)).isNull();
    }

    @Test
    @DisplayName("airport names are returned for every seeded airport")
    void returnsAllAirportNames() {
        assertThat(airportRepository.getAllAirportsNames())
                .hasSize(4)
                .contains("Henri Coanda International Airport", "London Heathrow Airport");
    }
}

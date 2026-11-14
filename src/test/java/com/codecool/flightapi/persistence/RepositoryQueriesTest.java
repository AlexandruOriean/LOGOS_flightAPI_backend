package com.codecool.flightapi.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.codecool.flightapi.airport.Airport;
import com.codecool.flightapi.airport.AirportRepository;
import com.codecool.flightapi.flight.Flight;
import com.codecool.flightapi.flight.FlightRepository;
import com.codecool.flightapi.user.RoleName;
import com.codecool.flightapi.user.RoleRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/**
 * Exercises the custom (native) queries against the schema and seed data produced by the
 * real Flyway migrations, on an embedded H2 database.
 */
@DataJpaTest
class RepositoryQueriesTest {

    private static final LocalDate SEEDED_FLIGHT_DATE = LocalDate.of(2020, 11, 1);

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private RoleRepository roleRepository;

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
    @DisplayName("flight search matches partial airport names on the given date")
    void searchesFlightsByPartialAirportNamesAndDate() {
        List<Flight> flights = flightRepository.getFlightByParams("London", "Henri", SEEDED_FLIGHT_DATE);

        assertThat(flights).singleElement().satisfies(flight -> {
            assertThat(flight.getDepartureAirport().getAirportIataCode()).isEqualTo("LHR");
            assertThat(flight.getArrivalAirport().getAirportIataCode()).isEqualTo("OTP");
            assertThat(flight.getAirline().getName()).isEqualTo("Tarom");
        });
    }

    @Test
    @DisplayName("flight search returns nothing for a date without flights")
    void flightSearchOnOtherDateIsEmpty() {
        assertThat(flightRepository.getFlightByParams("London", "Henri", SEEDED_FLIGHT_DATE.plusDays(1)))
                .isEmpty();
    }

    @ParameterizedTest
    @EnumSource(RoleName.class)
    @DisplayName("every role used by registration is seeded")
    void everyRoleIsSeeded(RoleName role) {
        assertThat(roleRepository.findByName(role)).isPresent();
    }
}

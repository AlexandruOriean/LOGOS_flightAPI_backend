package com.codecool.flightapi.flight;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/** The native flight search query against the real Flyway schema and seed data (embedded H2). */
@DataJpaTest
class FlightRepositoryTest {

    private static final LocalDate SEEDED_FLIGHT_DATE = LocalDate.of(2020, 11, 1);

    @Autowired
    private FlightRepository flightRepository;

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
}

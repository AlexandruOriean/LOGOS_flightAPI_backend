package com.codecool.flightapi.flight;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.codecool.flightapi.airline.Airline;
import com.codecool.flightapi.airport.Airport;
import com.codecool.flightapi.city.City;
import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Revives the flight search test that was commented out in FlightApiProjectApplicationTests, as unit tests. */
@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    private static final LocalDate DATE = LocalDate.of(2021, 1, 15);

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private final Airport otopeni = new Airport(1L, "OTP", "Otopeni", new City(12L, "Bucuresti"));
    private final Airport heathrow = new Airport(2L, "LHR", "Heathrow", new City(21L, "London"));
    private final Airline tarom = new Airline(10000L, "Tarom", "TRO");

    @Test
    void searchReturnsTheFlightsFoundForRouteAndDate() {
        List<Flight> flights = List.of(
                flight(1L, LocalTime.of(9, 20), LocalTime.of(11, 20)),
                flight(2L, LocalTime.of(10, 20), LocalTime.of(12, 20)),
                flight(3L, LocalTime.of(10, 20), LocalTime.of(13, 20)));
        when(flightRepository.getFlightByParams("Otopeni", "Heathrow", DATE)).thenReturn(flights);

        assertThat(flightService.search("Otopeni", "Heathrow", DATE)).hasSize(3).allSatisfy(flight -> {
            assertThat(flight.getDepartureAirport()).isEqualTo(otopeni);
            assertThat(flight.getArrivalAirport()).isEqualTo(heathrow);
            assertThat(flight.getDepartureDate()).isEqualTo(DATE);
        });
    }

    @Test
    void searchReturnsEmptyListWhenNothingMatches() {
        when(flightRepository.getFlightByParams("Otopeni", "Heathrow", DATE)).thenReturn(List.of());

        assertThat(flightService.search("Otopeni", "Heathrow", DATE)).isEmpty();
    }

    @Test
    void findByIdThrowsNotFoundForUnknownId() {
        when(flightRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> flightService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Flight with id '99' was not found");
    }

    @Test
    void updateCopiesScheduleAndPrice() {
        Flight existing = flight(1L, LocalTime.of(9, 20), LocalTime.of(11, 20));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(existing));
        Flight changes = Flight.builder()
                .departureDate(DATE.plusDays(1))
                .departureTime(LocalTime.of(7, 0))
                .arrivalTime(LocalTime.of(9, 0))
                .price(250L)
                .build();

        Flight updated = flightService.update(1L, changes);

        assertThat(updated.getDepartureDate()).isEqualTo(DATE.plusDays(1));
        assertThat(updated.getDepartureTime()).isEqualTo(LocalTime.of(7, 0));
        assertThat(updated.getArrivalTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(updated.getPrice()).isEqualTo(250L);
        // the route is not changed by an update
        assertThat(updated.getDepartureAirport()).isEqualTo(otopeni);
    }

    private Flight flight(Long id, LocalTime departure, LocalTime arrival) {
        return new Flight(id, DATE, 100L, departure, arrival, otopeni, heathrow, tarom);
    }
}

package com.codecool.flightapi.airline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Revives the airline tests that were commented out in FlightApiProjectApplicationTests, as unit tests. */
@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AirlineService airlineService;

    private final Airline tarom = new Airline(10000L, "Tarom", "TRO");

    @Test
    void findAllReturnsEveryAirline() {
        when(airlineRepository.findAll())
                .thenReturn(List.of(tarom, new Airline(10001L, "Wizz", "WIZZ"), new Airline(10002L, "BlueAir", "BLA")));

        assertThat(airlineService.findAll()).extracting(Airline::getName).containsExactly("Tarom", "Wizz", "BlueAir");
    }

    @Test
    void findByIdReturnsTheAirline() {
        when(airlineRepository.findById(10000L)).thenReturn(Optional.of(tarom));

        assertThat(airlineService.findById(10000L)).isSameAs(tarom);
    }

    @Test
    void findByIdThrowsNotFoundForUnknownId() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airlineService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Airline with id '1' was not found");
    }

    @Test
    void createSavesTheAirlineAndIgnoresClientSuppliedId() {
        when(airlineRepository.save(any(Airline.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Airline created = airlineService.create(new Airline(123L, "British", "BRT"));

        assertThat(created.getId()).isNull();
        assertThat(created.getName()).isEqualTo("British");
    }

    @Test
    void updateCopiesNameAndIso() {
        when(airlineRepository.findById(10000L)).thenReturn(Optional.of(tarom));

        Airline updated = airlineService.update(10000L, new Airline(null, "TAROM Romanian Air Transport", "ROT"));

        assertThat(updated.getName()).isEqualTo("TAROM Romanian Air Transport");
        assertThat(updated.getIso()).isEqualTo("ROT");
    }

    @Test
    void deleteRemovesAnExistingAirline() {
        when(airlineRepository.findById(10000L)).thenReturn(Optional.of(tarom));

        airlineService.delete(10000L);

        verify(airlineRepository).delete(tarom);
    }

    @Test
    void deleteThrowsNotFoundAndDeletesNothingForUnknownId() {
        when(airlineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airlineService.delete(1L)).isInstanceOf(ResourceNotFoundException.class);
        verify(airlineRepository, never()).delete(any(Airline.class));
    }
}

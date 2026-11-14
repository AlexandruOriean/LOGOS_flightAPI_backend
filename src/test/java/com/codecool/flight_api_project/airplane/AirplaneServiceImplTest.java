package com.codecool.flight_api_project.airplane;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codecool.flight_api_project.common.error.ResourceNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AirplaneServiceImplTest {

    @Mock
    private AirplaneRepository airplaneRepository;

    @InjectMocks
    private AirplaneServiceImpl airplaneService;

    @Test
    void findByIdThrowsNotFoundForUnknownId() {
        when(airplaneRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airplaneService.findById(42L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Airplane with id '42' was not found");
    }

    @Test
    void createIgnoresClientSuppliedId() {
        Airplane request = new Airplane(7L, "Airbus", "A320", 150L, 800);
        when(airplaneRepository.save(any(Airplane.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Airplane created = airplaneService.create(request);

        assertThat(created.getId()).isNull();
    }

    @Test
    void updateCopiesEveryField() {
        Airplane existing = new Airplane(1L, "Boeing", "B737", 180L, 700);
        when(airplaneRepository.findById(1L)).thenReturn(Optional.of(existing));

        Airplane updated = airplaneService.update(1L, new Airplane(null, "Airbus", "A350", 300L, 950));

        assertThat(updated.getManufacturer()).isEqualTo("Airbus");
        assertThat(updated.getModel()).isEqualTo("A350");
        assertThat(updated.getNumberOfSeats()).isEqualTo(300L);
        // regression: speed used to be overwritten with numberOfSeats
        assertThat(updated.getSpeed()).isEqualTo(950);
    }

    @Test
    void deleteThrowsNotFoundAndDeletesNothingForUnknownId() {
        when(airplaneRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airplaneService.delete(42L)).isInstanceOf(ResourceNotFoundException.class);
        verify(airplaneRepository, never()).delete(any(Airplane.class));
    }
}

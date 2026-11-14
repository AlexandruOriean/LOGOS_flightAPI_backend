package com.codecool.flightapi.flight;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public Flight findById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Flight", id));
    }

    @Override
    public List<Flight> search(String from, String to, LocalDate date) {
        return flightRepository.getFlightByParams(from, to, date);
    }

    @Override
    @Transactional
    public Flight create(Flight flight) {
        flight.setFlight_id(null); // never let a client-supplied id overwrite an existing row
        return flightRepository.save(flight);
    }

    @Override
    @Transactional
    public Flight update(Long id, Flight changes) {
        Flight flight = findById(id);
        flight.setDepartureDate(changes.getDepartureDate());
        flight.setDepartureTime(changes.getDepartureTime());
        flight.setArrivalTime(changes.getArrivalTime());
        flight.setPrice(changes.getPrice());
        return flight;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        flightRepository.delete(findById(id));
    }
}

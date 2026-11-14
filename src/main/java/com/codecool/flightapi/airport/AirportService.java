package com.codecool.flightapi.airport;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportService {

    private final AirportRepository airportRepository;

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    public Airport findById(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Airport", id));
    }

    /**
     * Returns the airport with the given IATA code (case-insensitive).
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    public Airport findByIataCode(String iataCode) {
        return Optional.ofNullable(airportRepository.getAirportByIataCode(iataCode))
                .orElseThrow(() -> new ResourceNotFoundException("Airport", "IATA code", iataCode));
    }

    public List<String> findAllNames() {
        return airportRepository.getAllAirportsNames();
    }

    /** Creates a new airport; any client-supplied id is ignored. */
    @Transactional
    public Airport create(Airport airport) {
        airport.setId(null); // never let a client-supplied id overwrite an existing row
        return airportRepository.save(airport);
    }

    @Transactional
    public Airport update(Long id, Airport changes) {
        Airport airport = findById(id);
        airport.setAirportName(changes.getAirportName());
        airport.setAirportIataCode(changes.getAirportIataCode());
        airport.setCity(changes.getCity());
        return airport;
    }

    @Transactional
    public void delete(Long id) {
        airportRepository.delete(findById(id));
    }
}

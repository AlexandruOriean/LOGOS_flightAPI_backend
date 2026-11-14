package com.codecool.flight_api_project.airport;

import com.codecool.flight_api_project.common.error.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public Airport findById(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Airport", id));
    }

    @Override
    public Airport findByIataCode(String iataCode) {
        return Optional.ofNullable(airportRepository.getAirportByIataCode(iataCode))
                .orElseThrow(() -> new ResourceNotFoundException("Airport", "IATA code", iataCode));
    }

    @Override
    public List<String> findAllNames() {
        return airportRepository.getAllAirportsNames();
    }

    @Override
    @Transactional
    public Airport create(Airport airport) {
        airport.setId(null); // never let a client-supplied id overwrite an existing row
        return airportRepository.save(airport);
    }

    @Override
    @Transactional
    public Airport update(Long id, Airport changes) {
        Airport airport = findById(id);
        airport.setAirportName(changes.getAirportName());
        airport.setAirportIataCode(changes.getAirportIataCode());
        airport.setCity(changes.getCity());
        return airport;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        airportRepository.delete(findById(id));
    }
}

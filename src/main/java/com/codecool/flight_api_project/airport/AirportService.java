package com.codecool.flight_api_project.airport;

import java.util.List;

public interface AirportService {

    List<Airport> findAll();

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flight_api_project.common.error.ResourceNotFoundException if absent
     */
    Airport findById(Long id);

    /**
     * Returns the airport with the given IATA code (case-insensitive).
     *
     * @throws com.codecool.flight_api_project.common.error.ResourceNotFoundException if absent
     */
    Airport findByIataCode(String iataCode);

    List<String> findAllNames();

    /** Creates a new airport; any client-supplied id is ignored. */
    Airport create(Airport airport);

    Airport update(Long id, Airport changes);

    void delete(Long id);
}

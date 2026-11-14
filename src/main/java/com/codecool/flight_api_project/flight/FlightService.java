package com.codecool.flight_api_project.flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightService {

    List<Flight> findAll();

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flight_api_project.common.error.ResourceNotFoundException if absent
     */
    Flight findById(Long id);

    /** Flights whose departure/arrival airport names contain {@code from}/{@code to} on {@code date}. */
    List<Flight> search(String from, String to, LocalDate date);

    /** Creates a new flight; any client-supplied id is ignored. */
    Flight create(Flight flight);

    Flight update(Long id, Flight changes);

    void delete(Long id);
}

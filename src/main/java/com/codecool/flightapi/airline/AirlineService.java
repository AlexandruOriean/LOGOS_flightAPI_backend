package com.codecool.flightapi.airline;

import java.util.List;

public interface AirlineService {

    List<Airline> findAll();

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    Airline findById(Long id);

    /** Creates a new airline; any client-supplied id is ignored. */
    Airline create(Airline airline);

    Airline update(Long id, Airline changes);

    void delete(Long id);
}

package com.codecool.flight_api_project.airplane;

import java.util.List;

public interface AirplaneService {

    List<Airplane> findAll();

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flight_api_project.common.error.ResourceNotFoundException if absent
     */
    Airplane findById(Long id);

    /** Creates a new airplane; any client-supplied id is ignored. */
    Airplane create(Airplane airplane);

    Airplane update(Long id, Airplane changes);

    void delete(Long id);
}

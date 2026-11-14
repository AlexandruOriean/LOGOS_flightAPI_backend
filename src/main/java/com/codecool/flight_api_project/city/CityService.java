package com.codecool.flight_api_project.city;

import java.util.List;

public interface CityService {

    List<City> findAll();

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flight_api_project.common.error.ResourceNotFoundException if absent
     */
    City findById(Long id);

    /** Creates a new city; any client-supplied id is ignored. */
    City create(City city);

    City update(Long id, City changes);

    void delete(Long id);
}

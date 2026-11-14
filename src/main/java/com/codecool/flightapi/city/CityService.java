package com.codecool.flightapi.city;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CityService {

    private final CityRepository cityRepository;

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("City", id));
    }

    /** Creates a new city; any client-supplied id is ignored. */
    @Transactional
    public City create(City city) {
        city.setCityId(null); // never let a client-supplied id overwrite an existing row
        return cityRepository.save(city);
    }

    @Transactional
    public City update(Long id, City changes) {
        City city = findById(id);
        city.setCityName(changes.getCityName());
        return city;
    }

    @Transactional
    public void delete(Long id) {
        cityRepository.delete(findById(id));
    }
}

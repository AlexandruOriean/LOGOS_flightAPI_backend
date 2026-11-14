package com.codecool.flight_api_project.city;

import com.codecool.flight_api_project.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("City", id));
    }

    @Override
    @Transactional
    public City create(City city) {
        city.setCityId(null); // never let a client-supplied id overwrite an existing row
        return cityRepository.save(city);
    }

    @Override
    @Transactional
    public City update(Long id, City changes) {
        City city = findById(id);
        city.setCityName(changes.getCityName());
        return city;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        cityRepository.delete(findById(id));
    }
}

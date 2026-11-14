package com.codecool.flight_api_project.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }

    @Override
    public City updateCityById(Long id, City cityToUpdate) {
        City city = cityRepository.findById(id).get();
        city.setCityName(cityToUpdate.getCityName());
        return cityRepository.save(city);
    }
}

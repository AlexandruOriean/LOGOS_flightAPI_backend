package com.codecool.flight_api_project.city;

public interface CityService {

    City saveCity(City city);

    void deleteCityById(Long id);

    City updateCityById(Long id, City cityToUpdate);
}

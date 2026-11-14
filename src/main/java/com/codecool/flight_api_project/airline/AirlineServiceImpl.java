package com.codecool.flight_api_project.airline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirlineServiceImpl implements AirlineService {

    @Autowired
    private AirlineRepository airlineRepository;

    @Override
    public Airline saveAirline(Airline airline) {
        return airlineRepository.save(airline);
    }

    @Override
    public void deleteAirlineById(Long id) {
        airlineRepository.deleteById(id);
    }

    @Override
    public Airline updateAirlineById(Long id, Airline airlineToUpdate) {
        Airline airline = airlineRepository.findById(id).get();
        airline.setIso(airlineToUpdate.getIso());
        airline.setName(airlineToUpdate.getName());
        return airlineRepository.save(airline);
    }
}

package com.codecool.flight_api_project.airline;

public interface AirlineService {

    Airline saveAirline(Airline airline);

    void deleteAirlineById(Long id);

    Airline updateAirlineById(Long id, Airline airlineToUpdate);
}

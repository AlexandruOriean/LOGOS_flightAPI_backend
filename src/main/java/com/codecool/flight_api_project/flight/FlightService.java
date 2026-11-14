package com.codecool.flight_api_project.flight;

public interface FlightService {

    Flight saveFlight(Flight flight);

    void deleteFlightById(Long id);

    Flight updateFlightById(Long id, Flight flight);
}

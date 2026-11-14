package com.codecool.flightapi.flight;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(
            value =
                    "select f.flight_id, f.arrival_time, f.departure_time, f.price, f.departure_date, f.airline_id, f.arrival_airport_id, f.departure_airport_id , airport1.airport_name as departure_airport, airport2.airport_name as arrival_airport, airline.name as airline_name  from flight f\n"
                            + " join airport airport1 on f.departure_airport_id = airport1.airport_id\n"
                            + " join airport airport2 on f.arrival_airport_id = airport2.airport_id\n"
                            + " join airline on f.airline_id = airline.id "
                            + "where airport1.airport_name like %:from% and airport2.airport_name like %:to% and f.departure_date = :date",
            nativeQuery = true)
    List<Flight> getFlightByParams(
            @RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("date") LocalDate date);
}

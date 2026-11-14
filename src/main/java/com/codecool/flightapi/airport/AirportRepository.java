package com.codecool.flightapi.airport;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface AirportRepository extends JpaRepository<Airport, Long> {

    @Query(value = "select * from Airport ", nativeQuery = true)
    List<Airport> getAllAirports();

    @Query(value = "SELECT AIRPORT_NAME FROM Airport", nativeQuery = true)
    List<String> getAllAirportsNames();

    @Query(value = "select * from AIRPORT where AIRPORT_IATA_CODE = UPPER(:iata_code)", nativeQuery = true)
    Airport getAirportByIataCode(@Param("iata_code") String iata_code);
}

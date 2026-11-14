package com.codecool.flight_api_project.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    //    @Modifying
    @Query(value = "insert into AIRLINE (NAME) values (:nameAirline)", nativeQuery = true)
    void addNewAirline(@Param("nameAirline") String nameAirline);
}

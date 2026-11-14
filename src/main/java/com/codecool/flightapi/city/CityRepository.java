package com.codecool.flightapi.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface CityRepository extends JpaRepository<City, Long> {
    @Query(value = "insert into CITY(CITY_NAME) values (:nameCity)", nativeQuery = true)
    void addNewCity(@Param("nameCity") String nameCity);
}

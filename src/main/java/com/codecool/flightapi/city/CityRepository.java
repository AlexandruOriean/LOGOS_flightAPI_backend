package com.codecool.flightapi.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CityRepository extends JpaRepository<City, Long> {}

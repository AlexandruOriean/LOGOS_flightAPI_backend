package com.codecool.flightapi.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AirlineRepository extends JpaRepository<Airline, Long> {}

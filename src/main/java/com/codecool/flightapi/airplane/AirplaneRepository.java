package com.codecool.flightapi.airplane;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AirplaneRepository extends JpaRepository<Airplane, Long> {}

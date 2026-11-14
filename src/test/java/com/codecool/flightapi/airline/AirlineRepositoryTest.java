package com.codecool.flightapi.airline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/** Seed data produced by the real Flyway migrations (embedded H2). */
@DataJpaTest
class AirlineRepositoryTest {

    @Autowired
    private AirlineRepository airlineRepository;

    @Test
    @DisplayName("every seeded airline has its own ICAO designator")
    void seededAirlinesHaveTheirIcaoDesignators() {
        assertThat(airlineRepository.findAll())
                .extracting(Airline::getName, Airline::getIso)
                .containsExactlyInAnyOrder(
                        tuple("Tarom", "ROT"),
                        tuple("British Airways", "BAW"),
                        tuple("Air France", "AFR"),
                        tuple("Wizz Air", "WZZ"));
    }
}

package com.codecool.flight_api_project.airport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airports")
@CrossOrigin
public class AirportController {

    @Autowired
    private final AirportService airportService;

    @Autowired
    private final AirportRepository airportRepository;

    public AirportController(AirportService airportService, AirportRepository airportRepository) {
        this.airportService = airportService;
        this.airportRepository = airportRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Airport> getAirports() {
        return airportRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Optional<Airport> getAirportById(@PathVariable("id") Long id) {
        return airportRepository.findById(id);
    }

    @GetMapping(path = "/iata/{iata_code}", produces = MediaType.APPLICATION_JSON_VALUE)
    Airport getAirportByIataCode(@PathVariable("iata_code") String iata_code) {
        return airportRepository.getAirportByIataCode(iata_code);
    }

    @GetMapping(path = "/names", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> getAirportsNames() {
        List<String> airportNames = new ArrayList<>();
        for (Airport airport : airportRepository.findAll()) {
            airportNames.add(airport.getAirportName());
        }
        return airportNames;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Airport> addAirport(@RequestBody final Airport airport) {
        Airport savedAirport = airportService.saveAirport(airport);
        return new ResponseEntity<>(savedAirport, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteAirportById(@PathVariable("id") final Long id) {
        airportService.deleteAirportById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Airport> updateAirportById(
            @PathVariable("id") final Long id, @RequestBody final Airport airportToUpdate) {
        Airport updatedAirport = airportService.updateAirportById(id, airportToUpdate);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }
}

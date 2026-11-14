package com.codecool.flight_api_project.flight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/flights")
@CrossOrigin
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Optional<Flight> getFlightById(@PathVariable("id") Long id) {
        return flightRepository.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{from}/{to}/{date}")
    public List<Flight> getFlightByParams(
            @PathVariable("from") String from,
            @PathVariable("to") String to,
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightRepository.getFlightByParams(from, to, date);
    }

    @PostMapping
    public ResponseEntity<Flight> addFlight(@RequestBody final Flight flight) {
        Flight savedFlight = flightService.saveFlight(flight);
        return new ResponseEntity<>(savedFlight, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlightById(@PathVariable("id") final Long id) {
        flightService.deleteFlightById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlightById(
            @PathVariable("id") final Long id, @RequestBody final Flight flightToUpdate) {
        Flight updatedFlight = flightService.updateFlightById(id, flightToUpdate);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }
}

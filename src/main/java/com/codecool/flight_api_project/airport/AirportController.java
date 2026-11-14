package com.codecool.flight_api_project.airport;

import com.codecool.flight_api_project.common.web.Locations;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/airports")
@CrossOrigin
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    public ResponseEntity<List<Airport>> getAirports() {
        return ResponseEntity.ok(airportService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airport> getAirport(@PathVariable Long id) {
        return ResponseEntity.ok(airportService.findById(id));
    }

    @GetMapping("/iata/{iataCode}")
    public ResponseEntity<Airport> getAirportByIataCode(@PathVariable String iataCode) {
        return ResponseEntity.ok(airportService.findByIataCode(iataCode));
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAirportNames() {
        return ResponseEntity.ok(airportService.findAllNames());
    }

    @PostMapping
    public ResponseEntity<Airport> createAirport(@RequestBody Airport airport) {
        Airport created = airportService.create(airport);
        return ResponseEntity.created(Locations.ofCreated(created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airport> updateAirport(@PathVariable Long id, @RequestBody Airport changes) {
        return ResponseEntity.ok(airportService.update(id, changes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

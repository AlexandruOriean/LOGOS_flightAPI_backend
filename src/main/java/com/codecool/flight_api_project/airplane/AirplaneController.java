package com.codecool.flight_api_project.airplane;

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
@RequestMapping("/api/v1/airplanes")
@CrossOrigin
@RequiredArgsConstructor
public class AirplaneController {

    private final AirplaneService airplaneService;

    @GetMapping
    public ResponseEntity<List<Airplane>> getAirplanes() {
        return ResponseEntity.ok(airplaneService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Airplane> getAirplane(@PathVariable Long id) {
        return ResponseEntity.ok(airplaneService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Airplane> createAirplane(@RequestBody Airplane airplane) {
        Airplane created = airplaneService.create(airplane);
        return ResponseEntity.created(Locations.ofCreated(created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airplane> updateAirplane(@PathVariable Long id, @RequestBody Airplane changes) {
        return ResponseEntity.ok(airplaneService.update(id, changes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirplane(@PathVariable Long id) {
        airplaneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

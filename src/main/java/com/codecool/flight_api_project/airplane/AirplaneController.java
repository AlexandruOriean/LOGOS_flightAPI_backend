package com.codecool.flight_api_project.airplane;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airplanes")
@CrossOrigin
@Service
public class AirplaneController {

    @Autowired
    private final AirplaneRepository airplaneRepository;

    @Autowired
    private AirplaneService airplaneService;

    public AirplaneController(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Airplane> getAirplanes() {
        return airplaneRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Airplane getAirplaneBy(@PathVariable Long id) {
        return airplaneService.getAirplaneById(id);
    }

    @PostMapping
    public ResponseEntity<Airplane> addAirplane(@RequestBody final Airplane airplane) {
        Airplane newAirplane = airplaneService.addAirplane(airplane);
        return new ResponseEntity<>(newAirplane, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAirplaneById(@PathVariable("id") final Long id) {
        airplaneService.deleteAirplaneByID(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Airplane> updateAirplaneById(
            @PathVariable("id") final Long id, @RequestBody final Airplane airplaneToUpdate) {
        Airplane updatedAirplane = airplaneService.updateAirplaneById(id, airplaneToUpdate);
        return new ResponseEntity<>(updatedAirplane, HttpStatus.OK);
    }
}

package com.codecool.flight_api_project.city;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cities")
@CrossOrigin
@Service
public class CityController {

    @Autowired
    private final CityRepository cityRepository;

    @Autowired
    private CityService cityService;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<City> getCities() {
        return cityRepository.findAll();
    }

    @PostMapping()
    public ResponseEntity<City> addCity(@RequestBody final City city) {
        City savedCity = cityService.saveCity(city);
        return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCityById(@PathVariable("id") final Long id) {
        cityService.deleteCityById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCityById(
            @PathVariable("id") final Long id, @RequestBody final City cityToUpdate) {
        City updatedCity = cityService.updateCityById(id, cityToUpdate);
        return new ResponseEntity<>(updatedCity, HttpStatus.OK);
    }
}

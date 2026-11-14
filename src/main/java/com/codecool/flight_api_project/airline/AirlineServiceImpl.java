package com.codecool.flight_api_project.airline;

import com.codecool.flight_api_project.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    @Override
    public Airline findById(Long id) {
        return airlineRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Airline", id));
    }

    @Override
    @Transactional
    public Airline create(Airline airline) {
        airline.setId(null); // never let a client-supplied id overwrite an existing row
        return airlineRepository.save(airline);
    }

    @Override
    @Transactional
    public Airline update(Long id, Airline changes) {
        Airline airline = findById(id);
        airline.setName(changes.getName());
        airline.setIso(changes.getIso());
        return airline;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        airlineRepository.delete(findById(id));
    }
}

package com.codecool.flightapi.airline;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirlineService {

    private final AirlineRepository airlineRepository;

    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    public Airline findById(Long id) {
        return airlineRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Airline", id));
    }

    /** Creates a new airline; any client-supplied id is ignored. */
    @Transactional
    public Airline create(Airline airline) {
        airline.setId(null); // never let a client-supplied id overwrite an existing row
        return airlineRepository.save(airline);
    }

    @Transactional
    public Airline update(Long id, Airline changes) {
        Airline airline = findById(id);
        airline.setName(changes.getName());
        airline.setIso(changes.getIso());
        return airline;
    }

    @Transactional
    public void delete(Long id) {
        airlineRepository.delete(findById(id));
    }
}

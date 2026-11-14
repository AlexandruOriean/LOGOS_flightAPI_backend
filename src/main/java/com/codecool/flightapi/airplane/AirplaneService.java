package com.codecool.flightapi.airplane;

import com.codecool.flightapi.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirplaneService {

    private final AirplaneRepository airplaneRepository;

    public List<Airplane> findAll() {
        return airplaneRepository.findAll();
    }

    /**
     * Returns the entity with the given id.
     *
     * @throws com.codecool.flightapi.common.error.ResourceNotFoundException if absent
     */
    public Airplane findById(Long id) {
        return airplaneRepository.findById(id).orElseThrow(() -> ResourceNotFoundException.byId("Airplane", id));
    }

    /** Creates a new airplane; any client-supplied id is ignored. */
    @Transactional
    public Airplane create(Airplane airplane) {
        airplane.setId(null); // never let a client-supplied id overwrite an existing row
        return airplaneRepository.save(airplane);
    }

    @Transactional
    public Airplane update(Long id, Airplane changes) {
        Airplane airplane = findById(id);
        airplane.setManufacturer(changes.getManufacturer());
        airplane.setModel(changes.getModel());
        airplane.setNumberOfSeats(changes.getNumberOfSeats());
        airplane.setSpeed(changes.getSpeed());
        return airplane;
    }

    @Transactional
    public void delete(Long id) {
        airplaneRepository.delete(findById(id));
    }
}

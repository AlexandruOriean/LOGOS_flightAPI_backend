package com.codecool.flight_api_project.airplane;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class AirplaneServiceImpl implements AirplaneService {

    @Autowired
    private AirplaneRepository airplaneRepository;

    @Override
    public Airplane addAirplane(Airplane airplane) {
        return airplaneRepository.save(airplane);
    }

    public List<Airplane> getAllAirplanes() {
        return airplaneRepository.findAll();
    }

    @Override
    public Airplane getAirplaneById(Long id) {
        return airplaneRepository.findById(id);
    }

    @Override
    public void deleteAirplaneByID(Long id) {
        airplaneRepository.deleteById(id);
    }

    @Override
    public Airplane updateAirplaneById(Long id, Airplane airplaneToUpdate) {
        Airplane airplane = airplaneRepository.findById(id).get();

        airplane.setManufacturer(airplaneToUpdate.getManufacturer());
        airplane.setModel(airplaneToUpdate.getModel());
        airplane.setNumberOfSeats(airplaneToUpdate.getNumberOfSeats());
        airplane.setSpeed(airplaneToUpdate.getNumberOfSeats());

        return airplaneRepository.save(airplane);
    }
}

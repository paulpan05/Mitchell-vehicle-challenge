package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    List<Vehicle> getAllVehicles() {
        return vehicleRepository.getAllVehicles();
    }

    Vehicle getVehicleById(int id) {
        return vehicleRepository.getVehicleById(id);
    }


}

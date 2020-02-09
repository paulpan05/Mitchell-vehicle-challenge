package com.mitchell.challenge.vehicle;

import com.mitchell.challenge.vehicle.exceptions.IdTakenException;
import com.mitchell.challenge.vehicle.exceptions.InvalidRequestException;
import com.mitchell.challenge.vehicle.exceptions.VehicleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final String nonExistGetString = "Cannot get non-existent vehicle";
    private final String nonExistDeleteString = "Cannot delete non-existent vehicle";
    private final String vehicleYearInvalidString = "Vehicle year must be between 1950 and 2050";
    private final String missingValuesRequestString = "Cannot create vehicle with missing values in request body";
    private final String noIdRequestString = "Cannot change vehicle properties without ID";

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    private boolean isValidYear(Integer year) {
        return year >= 1950 && year <= 2050;
    }

    List<Vehicle> getAllVehicles() {
        return vehicleRepository.getAllVehicles();
    }

    Vehicle getVehicleById(Integer id) {
        try {
            return vehicleRepository.getVehicleById(id);
        } catch (DataAccessException e) {
            throw new VehicleNotFoundException(nonExistGetString);
        }
    }

    void createVehicle(Vehicle vehicle) {
        Integer vehicleId = vehicle.getId();
        Integer vehicleYear = vehicle.getYear();
        String vehicleMake = vehicle.getMake();
        String vehicleModel = vehicle.getModel();
        if (vehicleId == null ||
                vehicleYear == null ||
                vehicleMake == null ||
                vehicleModel == null) {
            throw new InvalidRequestException(missingValuesRequestString);
        } else if (!vehicleRepository.isIdTaken(vehicle.getId())) {
            if (!isValidYear(vehicleYear)) {
                throw new InvalidRequestException(vehicleYearInvalidString);
            }
            vehicleRepository.createVehicle(vehicle);
        } else {
            throw new IdTakenException();
        }
    }

    void updateVehicle(Vehicle vehicle) {
        Integer vehicleId = vehicle.getId();
        Integer vehicleYear = vehicle.getYear();
        String vehicleMake = vehicle.getMake();
        String vehicleModel = vehicle.getModel();
        if (vehicleId == null) {
            throw new InvalidRequestException(noIdRequestString);
        }
        Optional.ofNullable(vehicleYear)
                .ifPresent(year -> {
                    if (!isValidYear(year)) {
                        throw new InvalidRequestException(vehicleYearInvalidString);
                    }
                    vehicleRepository.updateVehicleYear(vehicleId, vehicleYear);
                });
        Optional.ofNullable(vehicleMake)
                .ifPresent(make -> vehicleRepository.updateVehicleMake(vehicleId, make));
        Optional.ofNullable(vehicleModel)
                .ifPresent(model -> vehicleRepository.updateVehicleModel(vehicleId, model));
    }

    void deleteVehicle(Integer id) {
        try {
            vehicleRepository.deleteVehicle(id);
        } catch (DataAccessException e) {
            throw new VehicleNotFoundException(nonExistDeleteString);
        }
    }

}

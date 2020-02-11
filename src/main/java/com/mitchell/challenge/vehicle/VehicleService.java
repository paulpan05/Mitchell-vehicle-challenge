package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final String nonExistGetString = "Cannot get non-existent vehicle";
    private final String nonExistDeleteString = "Cannot delete non-existent vehicle";
    private final String vehicleYearInvalidString = "Vehicle year must be between 1950 and 2050";
    private final String missingValuesRequestString = "Request body invalid, must be in the form" +
            "{id: int, year: int, make: string, model: string}";
    private final String noIdRequestString = "Cannot change vehicle properties without ID";
    private final String idTakenString = "ID of vehicle already exists in database";

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    private boolean isValidYear(Integer year) {
        return year >= 1950 && year <= 2050;
    }

    List<Vehicle> getVehicles(Integer year, String make, String model) {
        List<Vehicle> resultList = new ArrayList<>();
        boolean allExist = false;
        if (year == null && make == null && model == null) {
            allExist = true;
        }
        if (year != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByYear(year);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }
        if (make != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByMake(make);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }
        if (model != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByModel(model);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }
        if (allExist) {
            resultList.addAll(vehicleRepository.getAllVehicles());
        }
        return resultList;
    }

    Vehicle getVehicleById(Integer id) {
        try {
            return vehicleRepository.getVehicleById(id);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistGetString);
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingValuesRequestString);
        } else if (!vehicleRepository.isIdTaken(vehicle.getId())) {
            if (!isValidYear(vehicleYear)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, vehicleYearInvalidString);
            }
            vehicleRepository.createVehicle(vehicle);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, idTakenString);
        }
    }

    void updateVehicle(Vehicle vehicle) {
        Integer vehicleId = vehicle.getId();
        Integer vehicleYear = vehicle.getYear();
        String vehicleMake = vehicle.getMake();
        String vehicleModel = vehicle.getModel();
        if (vehicleId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, noIdRequestString);
        }
        Optional.ofNullable(vehicleYear)
                .ifPresent(year -> {
                    if (!isValidYear(year)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, vehicleYearInvalidString);
                    }
                    vehicleRepository.updateVehicleYear(vehicleId, vehicleYear);
                });
        Optional.ofNullable(vehicleMake)
                .ifPresent(make -> vehicleRepository.updateVehicleMake(vehicleId, make));
        Optional.ofNullable(vehicleModel)
                .ifPresent(model -> vehicleRepository.updateVehicleModel(vehicleId, model));
    }

    void deleteVehicle(Integer id) {
            int successStatus = vehicleRepository.deleteVehicle(id);
            if (successStatus == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistDeleteString);
            }
    }

}

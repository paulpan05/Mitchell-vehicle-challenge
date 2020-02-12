package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for vehicle application.
 *
 * Contains all the error handling logic, along with method filtering to see which queries should be performed based on
 * params passed in.
 */
@Service
public class VehicleService {

    // String constants for message in error cases
    private final String nonExistGetString = "Cannot get non-existent vehicle";
    private final String nonExistDeleteString = "Cannot delete non-existent vehicle";
    private final String vehicleYearInvalidString = "Vehicle year must be between 1950 and 2050";
    private final String missingValuesRequestString = "Request body invalid, must be in the form" +
            "{id: int, year: int, make: string, model: string}";
    private final String noIdRequestString = "Cannot change vehicle properties without ID";
    private final String idTakenString = "ID of vehicle already exists in database";
    private final String idNotExistString = "ID of vehicle does not exist in the database";

    // Variable for the vehicle repository which to make queries from
    private final VehicleRepository vehicleRepository;

    /**
     * Constructs the vehicle service with repository access.
     *
     * @param vehicleRepository the vehicle repository class dependency injected in
     */
    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Checks if the year passed in from the request is within allowable range
     *
     * @param year the integer representation of the year value of the request
     * @return whether or not the year falls within 1950 and 2050
     */
    private boolean isValidYear(Integer year) {
        return year >= 1950 && year <= 2050;
    }

    /**
     * Gets all the vehicles of the database, or filter the get by year, make, or model.
     *
     * If multiple parameters are passed in, the matching lists will conglomerate.
     *
     * @param year the year value of the vehicle
     * @param make the make of the vehicle (manufacturer)
     * @param model the model of the vehicle
     * @return the list of vehicles that matches the restrictions, or all vehicles if no
     * restrictions
     */
    List<Vehicle> getVehicles(Integer year, String make, String model) {
        List<Vehicle> resultList = new ArrayList<>();
        boolean allExist = false;

        // All vehicles exists flag
        if (year == null && make == null && model == null) {
            allExist = true;
        }

        // Append to list vehicles that are unique and contains the filtered year
        if (year != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByYear(year);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }

        // Append to list vehicles that are unique and contains the filtered make
        if (make != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByMake(make);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }

        // Append to list vehicles that are unique and contains the filtered model
        if (model != null) {
            List <Vehicle> tmpResult = vehicleRepository.getVehiclesByModel(model);
            for(Vehicle cur: tmpResult) {
                if(!resultList.contains(cur)) {
                    resultList.add(cur);
                }
            }
        }

        // Gets all vehicles from the database if no restrictions applied in the request
        if (allExist) {
            resultList.addAll(vehicleRepository.getAllVehicles());
        }
        return resultList;
    }

    /**
     * Gets the specific vehicle in the database by the id value
     *
     * @param id the identity of the vehicle in the database
     * @return the vehicle result
     */
    Vehicle getVehicleById(Integer id) {
        try {
            return vehicleRepository.getVehicleById(id);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistGetString);
        }
    }

    /**
     * Creates a vehicle and puts it in a database
     *
     * Can error out if incorrect request body is passed in, such as conflicting id, missing values, or invalid year
     *
     * @param vehicle the vehicle to be created in the database
     */
    void createVehicle(Vehicle vehicle) {
        Integer vehicleId = vehicle.getId();
        Integer vehicleYear = vehicle.getYear();
        String vehicleMake = vehicle.getMake();
        String vehicleModel = vehicle.getModel();

        // Ensure that no keys in the request body are missing
        if (vehicleId == null ||
                vehicleYear == null ||
                vehicleMake == null ||
                vehicleModel == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingValuesRequestString);

            // Ensure that the id is not taken
        } else if (!vehicleRepository.isIdTaken(vehicle.getId())) {

            // Ensure the year is valid based on the valid year method
            if (!isValidYear(vehicleYear)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, vehicleYearInvalidString);
            }
            vehicleRepository.createVehicle(vehicle);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, idTakenString);
        }
    }

    /**
     * Updates the vehicle in the database based on request body passed in
     *
     * id of the vehicle cannot be null since we need to find the vehicle in order to modify it. Other fields can
     * be null since not all fields have to be updated.
     *
     * @param vehicle the vehicle to be modified, along with its modified values
     */
    void updateVehicle(Vehicle vehicle) {
        Integer vehicleId = vehicle.getId();
        Integer vehicleYear = vehicle.getYear();
        String vehicleMake = vehicle.getMake();
        String vehicleModel = vehicle.getModel();

        // Ensure the vehicle id exists
        if (vehicleId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, noIdRequestString);
        }

        // Check if the id is taken, id must exist to modify values
        if (!vehicleRepository.isIdTaken(vehicleId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, idNotExistString);
        }

        // Update year if exists modified value in request body
        Optional.ofNullable(vehicleYear)
                .ifPresent(year -> {

                    // Check if the modified year is within range of valid years
                    if (!isValidYear(year)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, vehicleYearInvalidString);
                    }
                    vehicleRepository.updateVehicleYear(vehicleId, vehicleYear);
                });

        // Update the make of the vehicle if modified value exists
        Optional.ofNullable(vehicleMake)
                .ifPresent(make -> vehicleRepository.updateVehicleMake(vehicleId, make));

        // Update the model of the vehicle if modified value exists
        Optional.ofNullable(vehicleModel)
                .ifPresent(model -> vehicleRepository.updateVehicleModel(vehicleId, model));
    }

    /**
     * Delete the vehicle by the id
     *
     * Throws error if id does not exist in database
     *
     * @param id the id of the vehicle to be deleted
     */
    void deleteVehicle(Integer id) {
            int successStatus = vehicleRepository.deleteVehicle(id);
            if (successStatus == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistDeleteString);
            }
    }

}

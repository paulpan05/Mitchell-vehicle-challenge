package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The Controller class for the vehicles back-end application.
 *
 * The parent route for all the endpoints in this controller is /vehicles
 */
@RestController
@RequestMapping("vehicles")
public class VehicleController {

    // The reference to the service object of vehicles application
    private final VehicleService vehicleService;

    /**
     * Constructor for the vehicle controller class, initializing access to the vehicle service
     *
     * @param vehicleService the vehicle service dependency injected in
     */
    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Get route for retrieving vehicles by type, or all vehicles if no request params found.
     *
     * Conglomerates the result of different request params together
     * (i.e. the more request params, the greater the response list)
     * @param year the year restriction applied to the get request
     * @param make the make restriction applied to the get request
     * @param model the model restriction applied to the get request
     * @return the list of all vehicles in the database, or list of filtered vehicles if request params exist
     */
    @GetMapping
    public List<Vehicle> getVehicles(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model) {
        return vehicleService.getVehicles(year, make, model);
    }

    /**
     * Get request for one specific vehicle based on its id.
     *
     * @param id the id of the vehicle to get
     * @return the specific matching vehicle
     */
    @GetMapping("{id}")
    public Vehicle getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id);
    }

    /**
     * Post request to create vehicle in database
     *
     * @param vehicle the vehicle which to perform the creation
     */
    @PostMapping
    public void createVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.createVehicle(vehicle);
    }

    /**
     * Put request to update the vehicle properties of the vehicle with specific id in the database
     *
     * @param vehicle the request body, which contains the id of the vehicle to modify, along with values to modify
     */
    @PutMapping
    public void updateVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.updateVehicle(vehicle);
    }

    /**
     * Delete request for removing a specific vehicle from the database
     *
     * @param id the id of the vehicle to remove from the database
     */
    @DeleteMapping("{id}")
    public void deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
    }
}

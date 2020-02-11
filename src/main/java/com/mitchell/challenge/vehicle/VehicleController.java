package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getVehicles(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model) {
        return vehicleService.getVehicles(year, make, model);
    }

    @GetMapping("{id}")
    public Vehicle getVehicleById(@PathVariable Integer id) {
        return vehicleService.getVehicleById(id);
    }

    @PostMapping
    public void createVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.createVehicle(vehicle);
    }

    @PutMapping
    public void updateVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.updateVehicle(vehicle);
    }

    @DeleteMapping("{id}")
    public void deleteVehicle(@PathVariable Integer id) {
        vehicleService.deleteVehicle(id);
    }
}

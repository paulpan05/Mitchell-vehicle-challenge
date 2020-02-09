package com.mitchell.challenge.vehicle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Vehicle Not Found")
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String msg) {
        super(msg);
    }
}

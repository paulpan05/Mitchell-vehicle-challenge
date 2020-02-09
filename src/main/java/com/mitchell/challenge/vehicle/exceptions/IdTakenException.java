package com.mitchell.challenge.vehicle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "ID of Vehicle Already Exists")
public class IdTakenException extends RuntimeException {
}

package com.mitchell.challenge.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The application class for the vehicle application, which handles requests from the client regarding vehicle
 * collections in the database.
 */
@SpringBootApplication
public class VehicleApplication {

	/**
	 * The method which the application is run from
	 *
	 * @param args the list of command line arguments passed into the back-end service
	 */
	public static void main(String[] args) {
		SpringApplication.run(VehicleApplication.class, args);
	}

}

package com.mitchell.challenge.vehicle;

import javax.persistence.*;

/**
 * Class that defines the schema of the table called vehicle, which will be created in the in-memory SQL database H2
 * upon launch of the application
 *
 * This class is also used to define request body of POST and PUT requests to the '/vehicles' route, as well as objects
 * of the lists returned by GET requests on the API.
 */
@Entity
public class Vehicle {

    // The id for the vehicle which acts as the primary key in table, must be unique and non-nullable
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    // The year of the vehicle, must be non-nullable
    @Column(nullable = false)
    private Integer year;

    // The make of the vehicle, must be non-nullable
    @Column(nullable = false)
    private String make;

    // The model of the vehicle, must be non-nullable
    @Column(nullable = false)
    private String model;

    /**
     * Default constructor for Vehicle which must exist for entity class
     */
    public Vehicle() {}

    /**
     * Constructs a vehicle object based on passed in params
     *
     * @param id the id of the vehicle
     * @param year the year of the vehicle
     * @param make the make of the vehicle
     * @param model the model of the vehicle
     */
    public Vehicle(Integer id, Integer year, String make, String model) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
    }

    /**
     * Gets the id of the vehicle
     *
     * @return the vehicle id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Gets the year of the vehicle
     *
     * @return the vehicle year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Gets the make of the vehicle
     *
     * @return the vehicle make
     */
    public String getMake() {
        return make;
    }

    /**
     * Gets the model of the vehicle
     *
     * @return the vehicle model
     */
    public String getModel() {
        return model;
    }

    /**
     * The overwritten equals method for use in collection.contains
     *
     * Compares the field equality of the current vehicle object with other objects
     *
     * @param obj the object to compare the current to
     * @return whether the two object is equal in field values to each other
     */
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == Vehicle.class){
            Vehicle vehicleObj = (Vehicle) obj;
            return vehicleObj.getId().equals(id) &&
                    vehicleObj.getYear().equals(year) &&
                    vehicleObj.getMake().equals(make) &&
                    vehicleObj.getModel().equals(model);
        }
        return false;
    }
}

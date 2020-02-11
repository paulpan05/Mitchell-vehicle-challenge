package com.mitchell.challenge.vehicle;

import javax.persistence.*;

@Entity
public class Vehicle {
    @Id
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    public Vehicle() {}

    public Vehicle(Integer id, Integer year, String make, String model) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
    }

    public Integer getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    @Override
    public boolean equals(Object obj) {
        Vehicle vehicleObj = (Vehicle) obj;
        return vehicleObj.getId().equals(id) &&
                vehicleObj.getYear().equals(year) &&
                vehicleObj.getMake().equals(make) &&
                vehicleObj.getModel().equals(model);
    }
}

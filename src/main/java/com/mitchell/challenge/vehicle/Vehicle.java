package com.mitchell.challenge.vehicle;

import javax.persistence.*;

@Entity
public class Vehicle {
    @Id
    @Column(unique = true, nullable = false)
    private int id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    public Vehicle() {}

    public Vehicle(int id, int year, String make, String model) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}

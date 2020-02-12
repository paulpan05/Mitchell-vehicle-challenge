package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class containing all the SQL queries to the H2 in-memory database.
 * This class is primarily for data access.
 */
@Repository
public class VehicleRepository {

    // Variable that references the database driver for Spring
    private final JdbcTemplate jdbcTemplate;

    /**
     * The constructor of the repository where the reference to the database driver is injected.
     *
     * @param jdbcTemplate the driver wrapper object which to make request from
     */
    @Autowired
    public VehicleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Gets all the vehicles in database
     *
     * @return the list of all vehicles in the database
     */
    List<Vehicle> getAllVehicles() {
        String sql = "" +
                "SELECT " +
                "id, " +
                "year, " +
                "make, " +
                "model " +
                "FROM vehicle";
        return jdbcTemplate.query(sql, mapVehicleFromDB());
    }

    /**
     * Gets the vehicle with the specific id in the database
     *
     * @param id the id which to search for the vehicle
     * @return the vehicle retrieved
     */
    Vehicle getVehicleById(Integer id) {
        String sql = "" +
                "SELECT " +
                "* " +
                "FROM vehicle " +
                "WHERE id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapVehicleFromDB());
    }

    /**
     * Gets the list of vehicles that matches the specific year
     *
     * @param year the year of the vehicles to retrieve
     * @return the list of matching vehicles
     */
    List<Vehicle> getVehiclesByYear(Integer year) {
        String sql = "" +
                "SELECT " +
                "* " +
                "FROM vehicle " +
                "WHERE year=?";
        return jdbcTemplate.query(sql, new Object[]{year}, mapVehicleFromDB());
    }

    /**
     * Gets the list of vehicles that matches the specific make
     *
     * @param make the make of teh vehicles to retrieve
     * @return the list of matching vehicles
     */
    List<Vehicle> getVehiclesByMake(String make) {
        String sql = "" +
                "SELECT " +
                "* " +
                "FROM vehicle " +
                "WHERE make=?";
        return jdbcTemplate.query(sql, new Object[]{make}, mapVehicleFromDB());
    }

    /**
     * Gets the list of vehicles filtered by their model
     *
     * @param model the specific model which to fetch the list of vehicles
     * @return the list of matching vehicles
     */
    List<Vehicle> getVehiclesByModel(String model) {
        String sql = "" +
                "SELECT " +
                "* " +
                "FROM vehicle " +
                "WHERE model=?";
        return jdbcTemplate.query(sql, new Object[]{model}, mapVehicleFromDB());
    }

    /**
     * Creates a vehicle in the database based on the vehicle request body passed in
     *
     * @param vehicle the vehicle object representation of the request body
     * @return the success value of the creation, 0 for failure
     */
    int createVehicle(Vehicle vehicle) {
        String sql = "" +
                "INSERT INTO vehicle (" +
                "id, " +
                "year, " +
                "make, " +
                "model) " +
                "VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, vehicle.getId(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel());
    }

    /**
     * Updates the year of the vehicle with the specific id in the database
     *
     * @param id the id of the vehicle which to do the update
     * @param year the updated year of the vehicle
     * @return the success value of the update, 0 for failure
     */
    int updateVehicleYear(Integer id, Integer year) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET year = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, year, id);
    }

    /**
     * Updates the make of the vehicle with the specific id in the database
     *
     * @param id the id of the vehicle which to do the update
     * @param make the updated make of the vehicle
     * @return the success value of the update, 0 for failure
     */
    int updateVehicleMake(Integer id, String make) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET make = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, make, id);
    }

    /**
     * Updates the model of the vehicle with the specific id in the database
     *
     * @param id the id of the vehicle which to do the update
     * @param model the updated model of the vehicle
     * @return the success value of the update, 0 for failure
     */
    int updateVehicleModel(Integer id, String model) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET model = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, model, id);
    }

    /**
     * Checks if the id of the vehicle with the certain id is taken
     *
     * @param id the id of the vehicle which to do the checking
     * @return boolean representation of whether the id is taken
     */
    @SuppressWarnings("ConstantConditions")
    boolean isIdTaken(int id) {
        String sql = "" +
                "SELECT EXISTS (" +
                "SELECT 1 " +
                "FROM vehicle " +
                "WHERE id = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    /**
     * Delete the vehicle from the database by the given id
     *
     * @param id the id of the vehicle which to perform the deletion
     * @return the success value of the deletion, 0 for failure
     */
    int deleteVehicle(Integer id) {
        String sql = "" +
                "DELETE FROM vehicle " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Gives the lambda function for retrieving all vehicles from the database
     *
     * @return the mapper lambda function for retrieving all vehicles from the database
     */
    private RowMapper<Vehicle> mapVehicleFromDB() {
        return (resultSet, i) -> {
            int id = Integer.parseInt(resultSet.getString("id"));
            int year = Integer.parseInt(resultSet.getString("year"));
            String make = resultSet.getString("make");
            String model = resultSet.getString("model");
            return new Vehicle(id, year, make, model);
        };
    }
}

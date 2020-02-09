package com.mitchell.challenge.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VehicleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    Vehicle getVehicleById(Integer id) {
        String sql = "" +
                "SELECT " +
                "* " +
                "FROM vehicle " +
                "WHERE id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, mapVehicleFromDB());
    }

    int createVehicle(Vehicle vehicle) {
        String sql = "" +
                "INSERT INTO vehicle (" +
                "year, " +
                "make, " +
                "model) " +
                "VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, vehicle.getId(), vehicle.getYear(), vehicle.getMake(), vehicle.getModel());
    }

    int updateVehicleYear(Integer id, Integer year) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET vehicle = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, year, id);
    }

    int updateVehicleMake(Integer id, String make) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET make = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, make, id);
    }

    int updateVehicleModel(Integer id, String model) {
        String sql = "" +
                "UPDATE vehicle " +
                "SET model = ? " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, model, id);
    }

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

    int deleteVehicle(Integer id) {
        String sql = "" +
                "DELETE FROM vehicle " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

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

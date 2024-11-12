package com.homework01_sy.car_rental.mappers;

import com.homework01_sy.car_rental.entities.Car;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarMapper implements RowMapper<Car> {

    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {

        Car car = new Car();
        car.setId(rs.getInt("id"));
        car.setModel(rs.getString("model"));
        car.setCity(rs.getString("city"));
        car.setDailyRate(rs.getDouble("daily_rate"));
        car.setIsAvailable(rs.getBoolean("is_available"));

        return car;
    }
}
package com.homework01_sy.car_rental.services;

import com.homework01_sy.car_rental.entities.Car;
import com.homework01_sy.car_rental.entities.Customer;
import com.homework01_sy.car_rental.mappers.CarMapper;
import com.homework01_sy.car_rental.mappers.CustomerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final JdbcTemplate db;

    private UtilsService utilsService;

    public CarService(JdbcTemplate db, UtilsService utilsService) {this.db = db;
        this.utilsService = utilsService;}

    public Car getCarById(int id) {
        String query = "SELECT * FROM cars WHERE id = ? AND is_active = TRUE";
        List<Car> cars = this.db.query(query, new CarMapper(), id);

        return cars.isEmpty() ? null : cars.get(0);
    }

    public List<Car> getCarsByClientLocation(int customerId) {
        String customerQuery = "SELECT * FROM customers WHERE id = ? AND is_active = TRUE";
        List<Customer> customers = this.db.query(customerQuery, new CustomerMapper(), customerId);

        if (customers.isEmpty()) {
            throw new IllegalArgumentException("Customer not found or inactive.");
        }

        String customerAddress = customers.get(0).getAddress();
        utilsService.validateCity(customerAddress);

        String carQuery = "SELECT * FROM cars WHERE city = ? AND is_active = TRUE";
        return this.db.query(carQuery, new CarMapper(), customerAddress);
    }

    public boolean addCar(Car car) {
        utilsService.validateCity(car.getCity());

        String query = "INSERT INTO cars (model, city, daily_rate, is_available, is_active) " +
                "VALUES (?, ?, ?, ?, TRUE)";

        int result = this.db.update(query,
                car.getModel(),
                car.getCity(),
                car.getDailyRate(),
                car.getIsAvailable());

        return result == 1;
    }

    public boolean updateCar(Car car) {
        utilsService.validateCity(car.getCity()); // Ensure city is allowed

        String query = "UPDATE cars SET model = ?, city = ?, daily_rate = ?, is_available = ? " +
                "WHERE id = ? AND is_active = TRUE";

        int resultCount = this.db.update(query,
                car.getModel(),
                car.getCity(),
                car.getDailyRate(),
                car.getIsAvailable(),
                car.getId());

        return resultCount == 1;
    }

    public List<Car> getAllCars() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM CARS WHERE is_active = True");

        return this.db.query(query.toString(), new CarMapper());
    }

    // TODO implement delete
    public boolean removeCar(int id) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE cars ")
                .append("SET is_active = False ")
                .append("WHERE is_active = True ")
                .append(" AND id = ?");

        int resultCount = this.db.update(query.toString(), id);

        return resultCount == 1;
    }
}

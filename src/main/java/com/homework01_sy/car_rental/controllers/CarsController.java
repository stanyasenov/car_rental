package com.homework01_sy.car_rental.controllers;

import com.homework01_sy.car_rental.entities.Car;
import com.homework01_sy.car_rental.repositories.AppResponse;
import com.homework01_sy.car_rental.services.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarsController {

    private CarService carService;
    public CarsController(CarService carService) {this.carService = carService;}

    @GetMapping("/cars/{id}")
    public ResponseEntity<?> getCarById(@PathVariable int id) {
        Car car = carService.getCarById(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(car);
    }

    @GetMapping("/cars/client/{customerId}")
    public ResponseEntity<?> getCarsByClientLocation(@PathVariable int customerId) {
        try {
            List<Car> cars = carService.getCarsByClientLocation(customerId);
            return ResponseEntity.ok(cars);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cars")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        try {
            boolean isAdded = carService.addCar(car);
            if (isAdded) {
                return ResponseEntity.ok("Car added successfully.");
            } else {
                return ResponseEntity.status(500).body("Failed to add car.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {

        ArrayList<Car> collection = (ArrayList<Car>) this.carService.getAllCars();

        if (collection.isEmpty()) {
            return AppResponse.error()
                    .withMessage("Something went wrong with fetching all cars")
                    .build();
        }

        return AppResponse.success()
                .withData(collection)
                .build();
    }

    @PutMapping("/cars")
    public ResponseEntity<?> updateCar(@RequestBody Car car) {
        boolean isUpdateSuccessful =  this.carService.updateCar(car);

        if(!isUpdateSuccessful) {
            return AppResponse.error()
                    .withMessage("Customer data not found")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Update successful")
                .build();
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> removeCustomer(@PathVariable int id) {
        boolean isUpdateSuccessful =  this.carService.removeCar(id);

        if(!isUpdateSuccessful) {
            return AppResponse.error()
                    .withMessage("Car data not found")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Car remove successful")
                .build();
    }
}

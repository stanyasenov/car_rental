package com.homework01_sy.car_rental.controllers;

import com.homework01_sy.car_rental.entities.Customer;
import com.homework01_sy.car_rental.repositories.AppResponse;
import com.homework01_sy.car_rental.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class CustomersController {
    private CustomerService customerService;

    public CustomersController(CustomerService customerService) {
        this.customerService = customerService;
    }

        @PostMapping("/customers")
        public ResponseEntity<?> createNewCustomer(@RequestBody Customer customer) {

            try{
                if(this.customerService.createCustomer(customer)) {

                    return AppResponse.success()
                            .withMessage("Customer created")
                            .build();
                }

                return AppResponse.error()
                        .withMessage("Something went wrong with creating customer")
                        .build();
            }
            catch(Exception e) {
                return AppResponse.error().withMessage(e.getMessage()).build();
            }
        }

    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {

        ArrayList<Customer> collection = (ArrayList<Customer>) this.customerService.getAllCustomers();

        if (collection.isEmpty()) {
            return AppResponse.error()
                    .withMessage("Something went wrong with fetching all customers")
                    .build();
        }

        return AppResponse.success()
                .withData(collection)
                .build();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> fetchSingleCustomer(@PathVariable int id) {
        try{
            Customer responseCustomer =  this.customerService.getCustomer(id);

            if(responseCustomer == null) {
                return AppResponse.error()
                        .withMessage("Customer data not found")
                        .build();
            }

            return AppResponse.success()
                    .withDataAsArray(responseCustomer)
                    .build();
        }   catch (Exception e) {
            return AppResponse.error().withMessage(e.getMessage()).build();
        }
    }

    @PutMapping("/customers")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {
        try{
            boolean isUpdateSuccessful =  this.customerService.updateCustomer(customer);

            if(!isUpdateSuccessful) {
                return AppResponse.error()
                        .withMessage("Customer data not found")
                        .build();
            }

            return AppResponse.success()
                    .withMessage("Update successful")
                    .build();
        }
        catch (Exception e) {
            return AppResponse.error().withMessage(e.getMessage()).build();
        }
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> removeCustomer(@PathVariable int id) {
        boolean isUpdateSuccessful =  this.customerService.removeCustomer(id);

        if(!isUpdateSuccessful) {
            return AppResponse.error()
                    .withMessage("Customer data not found")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Customer remove successful")
                .build();
    }

}

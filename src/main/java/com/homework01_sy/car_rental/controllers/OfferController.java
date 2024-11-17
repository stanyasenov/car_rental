package com.homework01_sy.car_rental.controllers;

import com.homework01_sy.car_rental.DTO.OfferRequest;
import com.homework01_sy.car_rental.entities.Offer;
import com.homework01_sy.car_rental.repositories.AppResponse;
import com.homework01_sy.car_rental.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OfferController {

    private OfferService offersService;

    public OfferController(OfferService offerService) {
        this.offersService = offerService;
    }

    @GetMapping("/offers")
    public ResponseEntity<?> getAllOffers() {
        try{
            List<Offer> offers = offersService.getAllOffers();
            if (offers.isEmpty()) {
                return AppResponse.error()
                        .withMessage("Something went wrong with fetching all offers")
                        .build();
            }

            return AppResponse.success()
                    .withData(offers)
                    .build();
        }
        catch (Exception e) {
            return AppResponse.error().withMessage(e.getMessage()).build();
        }
    }

    @PostMapping("/offers")
    public ResponseEntity<?> createOffer(@RequestBody OfferRequest offerRequest) {

        try {
            Offer offer = offersService.createOffer(offerRequest.getCustomerId(),
                    offerRequest.getCarId(), offerRequest.getRentalDays());
            return AppResponse.success()
                    .withMessage("Offer created successfully")
                    .withData(offer)
                    .build();
        } catch (IllegalArgumentException e) {
            return AppResponse.error()
                    .withMessage("Failed to create offer: " + e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/offers/{id}")
    public ResponseEntity<?> removeCustomer(@PathVariable int id) {
        boolean isUpdateSuccessful =  this.offersService.removeOffer(id);

        if(!isUpdateSuccessful) {
            return AppResponse.error()
                    .withMessage("Offer data not found")
                    .build();
        }

        return AppResponse.success()
                .withMessage("Offer remove successful")
                .build();
    }
}

package com.homework01_sy.car_rental.controllers;

import com.homework01_sy.car_rental.DTO.OfferRequest;
import com.homework01_sy.car_rental.entities.Offer;
import com.homework01_sy.car_rental.repositories.AppResponse;
import com.homework01_sy.car_rental.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OfferController {

    private OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping("/offers")
    public ResponseEntity<?> getAllOffers() {
        try{
            List<Offer> offers = offerService.getAllOffers();
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
            Offer offer = offerService.createOffer(offerRequest.getCustomerId(),
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
}

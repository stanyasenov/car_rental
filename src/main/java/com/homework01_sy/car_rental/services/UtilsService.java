package com.homework01_sy.car_rental.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UtilsService {
    public ResponseEntity<Object> setResponseStatus (String message, int statusCode) {

        return new ResponseEntity<Object>(message, HttpStatusCode.valueOf(statusCode));
    }

    private static final Set<String> ALLOWED_CITIES = Set.of("Plovdiv", "Sofia", "Varna", "Burgas");

    public void validateCity(String city) {
        if (!ALLOWED_CITIES.contains(city)) {
            throw new IllegalArgumentException("Service is only available in Plovdiv, Sofia, Varna, and Burgas.");
        }
    }
}

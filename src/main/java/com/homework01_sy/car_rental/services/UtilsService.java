package com.homework01_sy.car_rental.services;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public double calculatePrice(double dailyRate, int rentalDays, boolean hasAccidents) {
        double total = dailyRate * rentalDays;

        int weekendDays = countWeekendDays(rentalDays);
        total += (dailyRate * 0.1 * weekendDays);

        if (hasAccidents) {
            total += 200;
        }

        return total;
    }

    public int countWeekendDays(int rentalDays) {
        LocalDate startDate = LocalDate.now();
        int weekendDays = 0;

        for (int i = 0; i < rentalDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekendDays++;
            }
        }

        return weekendDays;
    }
}

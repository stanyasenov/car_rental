package com.homework01_sy.car_rental.services;

import com.homework01_sy.car_rental.entities.Car;
import com.homework01_sy.car_rental.entities.Customer;
import com.homework01_sy.car_rental.entities.Offer;
import com.homework01_sy.car_rental.mappers.OfferMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OfferService {
    private final JdbcTemplate db;
    private final CarService carService;
    private final CustomerService customerService;
    private final UtilsService utilsService;

    public OfferService(JdbcTemplate db, CarService carService, CustomerService customerService, UtilsService utilsService) {
        this.db = db;
        this.carService = carService;
        this.customerService = customerService;
        this.utilsService = utilsService;
    }

    public Offer createOffer(int customerId, int carId, int rentalDays) {
        Customer customer = customerService.getCustomer(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found or inactive.");
        }

        Car car = carService.getCarById(carId);
        if (car == null || !car.getIsAvailable()) {
            throw new IllegalArgumentException("Car not found or unavailable.");
        }

        utilsService.validateCity(customer.getAddress());

        double estimatedPrice = utilsService.calculatePrice(car.getDailyRate(), rentalDays, customer.getHasAccidents());

        String disableOffersQuery = "UPDATE offers SET is_active = FALSE WHERE customer_id = ? AND is_active = TRUE";
        db.update(disableOffersQuery, customerId);

        String query = "INSERT INTO offers (customer_id, car_id, offer_date, rental_days, estimated_price, is_accepted, is_active) " +
                "VALUES (?, ?, ?, ?, ?, FALSE, TRUE)";

        db.update(query, customerId, carId, LocalDate.now(), rentalDays, estimatedPrice);

        return getLatestOfferForCustomer(customerId);
    }

    public List<Offer> getOffersByCustomer(int customerId) {
        String query = "SELECT * FROM offers WHERE customer_id = ?";
        return db.query(query, new OfferMapper(), customerId);
    }

    public boolean acceptOffer(int customerId, int offerId) {
        String checkQuery = "SELECT COUNT(*) FROM offers WHERE id = ? AND customer_id = ? AND is_active = TRUE";
        Integer count = db.queryForObject(checkQuery, Integer.class, offerId, customerId);

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Offer not found or does not belong to the specified customer.");
        }

        String updateQuery = "UPDATE offers SET is_accepted = TRUE WHERE id = ? AND is_active = TRUE";
        int result = db.update(updateQuery, offerId);

        return result == 1;
    }

    public List<Offer> getAllOffers() {
        String query = "SELECT * FROM offers WHERE is_active = TRUE";
        return db.query(query, new OfferMapper());
    }

    private Offer getLatestOfferForCustomer(int customerId) {
        String query = "SELECT * FROM offers WHERE customer_id = ? AND is_active = TRUE ORDER BY offer_date DESC LIMIT 1";
        List<Offer> offers = db.query(query, new OfferMapper(), customerId);

        return offers.isEmpty() ? null : offers.get(0);
    }

    // TODO Implement Soft Delete
    public boolean deleteOffer(int id) {
        StringBuilder query = new StringBuilder();
        return false;
    }

}

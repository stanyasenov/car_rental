package com.homework01_sy.car_rental.mappers;

import com.homework01_sy.car_rental.entities.Offer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OfferMapper implements RowMapper<Offer> {

    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Offer offer = new Offer();

        offer.setId(rs.getInt("id"));
        offer.setCustomerId(rs.getInt("customer_id"));
        offer.setCarId(rs.getInt("car_id"));
        offer.setOfferDate(rs.getObject("offer_date", LocalDate.class));
        offer.setRentalDays(rs.getInt("rental_days"));
        offer.setEstimatedPrice(rs.getDouble("estimated_price"));
        offer.setAccepted(rs.getBoolean("is_accepted"));
        offer.setActive(rs.getBoolean("is_active"));

        return offer;
    }
}
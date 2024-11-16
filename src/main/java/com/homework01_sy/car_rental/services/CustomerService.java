package com.homework01_sy.car_rental.services;

import com.homework01_sy.car_rental.entities.Customer;
import com.homework01_sy.car_rental.mappers.CustomerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private JdbcTemplate db;

    public CustomerService(JdbcTemplate jdbc) {
        this.db = jdbc;
    }

    public boolean createCustomer(Customer customer) {

        // Unique phone numbers per customer
        String checkQuery = "SELECT COUNT(*) FROM customers WHERE phone = ? AND is_active = TRUE";
        Integer count = db.queryForObject(checkQuery, Integer.class, customer.getPhone());

        if (count != null && count > 0) {
            throw new IllegalArgumentException("A customer with this phone number already exists.");
        }

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO customers ");
        query.append("(name, address, phone, age, has_accidents, is_active) ");
        query.append("VALUES ('");
        query.append(customer.getName()).append("', '");
        query.append(customer.getAddress()).append("', '");
        query.append(customer.getPhone()).append("', ");
        query.append(customer.getAge()).append(", ");
        query.append(customer.getHasAccidents()).append(", ");
        query.append("TRUE");  // Setting is_active to TRUE by default
        query.append(")");

        // Assuming `this.db.execute` executes the SQL query
        this.db.execute(query.toString());

        return true;
    }

    public List<Customer> getAllCustomers() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM CUSTOMERS WHERE is_active = True");

        return this.db.query(query.toString(), new CustomerMapper());
    }

    public Customer getCustomer(int id) {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM CUSTOMERS WHERE is_active = True AND id = " + id);
        ArrayList<Customer> collection = (ArrayList<Customer>) this.db.query(query.toString(), new CustomerMapper());

        if(collection.isEmpty()) {
            return null;
        }

        return collection.get(0);
    }

    public boolean updateCustomer(Customer customer) {

        StringBuilder query = new StringBuilder();
        query.append("UPDATE customers ")
                .append("SET name = ?, ")
                .append("address = ?, ")
                .append("phone = ?, ")
                .append("age = ?, ")
                .append("has_accidents = ? ")
                .append("WHERE is_active = TRUE AND id = ?");

        int resultCount = this.db.update(query.toString(),
                customer.getName(),
                customer.getAddress(),
                customer.getPhone(),
                customer.getAge(),
                customer.getHasAccidents(),
                customer.getId());

        if(resultCount > 1) {
            throw new RuntimeException("More than one customer with same id exists");
        }

        return resultCount == 1;
    }

    // TODO Implement Soft Delete
    public boolean deleteCustomer(int id) {
        StringBuilder query = new StringBuilder();
        return false;
    }
}

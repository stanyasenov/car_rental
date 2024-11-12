package com.homework01_sy.car_rental.entities;


public class Car {
    private int id;
    private String model;
    private Double dailyRate;
    private Boolean isAvailable;
    private String city;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIsAvailable() {  // Correct getter for Boolean field
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {  // Correct setter for Boolean field
        this.isAvailable = isAvailable;
    }

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

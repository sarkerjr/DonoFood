package com.rashed.donofood.Models;

import java.net.URL;

public class FoodDonation {
    private String foodType;
    private String foodName;
    private Float quantity;
    private String location;
    private String phone;
    private URL foodUrl;

    public FoodDonation(String foodType, String foodName, Float quantity, String location, String phone, URL foodUrl) {
        this.foodType = foodType;
        this.foodName = foodName;
        this.quantity = quantity;
        this.location = location;
        this.phone = phone;
        this.foodUrl = foodUrl;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getFoodName() {
        return foodName;
    }

    public Float getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public URL getFoodUrl() {
        return foodUrl;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setFoodUrl(URL foodUrl) {
        this.foodUrl = foodUrl;
    }
}

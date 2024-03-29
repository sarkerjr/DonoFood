package com.rashed.donofood.Models;

import java.io.Serializable;

public class FoodDonation implements Serializable {
    String uid;
    private String foodType;
    private String foodName;
    private int quantity;
    private String location;
    private String phone;
    private String imageFileName;

    //Need this non argument constructor to work adapter properly
    public FoodDonation() {}

    public FoodDonation(String uid, String foodName, String foodType, int quantity, String location, String phone, String imageFileName) {
        this.uid = uid;
        this.foodName = foodName;
        this.foodType = foodType;
        this.quantity = quantity;
        this.location = location;
        this.phone = phone;
        this.imageFileName = imageFileName;
    }

    public String getUid() {
        return uid;
    }

    public String getFoodType() {
        return foodType;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getLocation() {
        return location;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

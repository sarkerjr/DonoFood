package com.rashed.donofood.Models;

public class ClothDonation {
    String uid;
    private String clothName;
    private String clothType;
    private Float quantity;
    private String location;
    private String phone;
    private String imageFileName;

    public ClothDonation(String uid, String clothName, String clothType, Float quantity, String location, String phone, String imageFileName) {
        this.uid = uid;
        this.clothName = clothName;
        this.clothType = clothType;
        this.quantity = quantity;
        this.location = location;
        this.phone = phone;
        this.imageFileName = imageFileName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClothName() {
        return clothName;
    }

    public void setClothName(String clothName) {
        this.clothName = clothName;
    }

    public String getClothType() {
        return clothType;
    }

    public void setClothType(String clothType) {
        this.clothType = clothType;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

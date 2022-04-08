package com.rashed.donofood.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String role;

    public User() {
    }

    public User(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

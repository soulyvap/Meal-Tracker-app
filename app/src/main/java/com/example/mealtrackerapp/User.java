package com.example.mealtrackerapp;

import java.util.Date;

public class User {
    private String firstname;
    private String lastname;
    private Date birthdate;
    private int height;
    private int weight;

    public User(String firstname, String lastname, Date birthdate, int height, int weight) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

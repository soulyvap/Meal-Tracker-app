package com.example.mealtrackerapp;


import java.io.Serializable;

public class FoodLog implements Serializable {
    private int id;
    private String date;
    private String name;
    private String meal;
    private String time;
    private int calories;
    private double carbs;
    private double protein;
    private double fat;

    public FoodLog(int id, String date, String name, String meal, String time, int calories, double carbs, double protein, double fat) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.meal = meal;
        this.time = time;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    @Override
    public String toString() {
        return time + " | " + meal + " | " + name + " : " + calories + " kcal (" + Math.round(carbs*4) + "/" + Math.round(protein*4) + "/" + Math.round(fat*8) + ")";
    }
}

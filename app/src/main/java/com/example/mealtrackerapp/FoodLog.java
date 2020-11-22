package com.example.mealtrackerapp;

import java.io.Serializable;

public class FoodLog implements Serializable {
    private String name;
    private String meal;
    private String time;
    private int calories;
    private int carbs;
    private int protein;
    private int fat;

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

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public FoodLog(String name, String meal, String time, int calories, int carbs, int protein, int fat) {
        this.name = name;
        this.meal = meal;
        this.time = time;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;


    }
}

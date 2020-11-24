package com.example.mealtrackerapp;

import java.util.ArrayList;

public class FoodDairy {
    private ArrayList<FoodLog> foodLogs;

    public FoodDairy() {
        this.foodLogs = new ArrayList<>();
    }

    public void add(FoodLog foodLog) {
        this.foodLogs.add(foodLog);
    }

    public ArrayList<FoodLog> getFoodLogs() {
        return foodLogs;
    }
}

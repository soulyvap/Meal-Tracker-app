package com.example.mealtrackerapp;

public class DayData {
    private String date;
    private int weight;
    private int caloricGoal;
    private int carbsGoal;
    private int proteinGoal;
    private int fatGoal;
    private int waterGoal;
    private int caloricIntake;
    private int carbsIntake;
    private int proteinIntake;
    private int fatIntake;
    private int breakfastIntake;
    private int lunchIntake;
    private int dinnerIntake;
    private int extrasIntake;
    private int waterIntake;

    public DayData(String date, int weight, int caloricGoal, int carbGoal, int proteinGoal,
                   int fatGoal, int waterGoal, int caloricIntake, int carbsIntake, int proteinIntake,
                   int fatIntake, int breakfastIntake, int lunchIntake, int dinnerIntake,
                   int extrasIntake, int waterIntake) {
        this.date = date;
        this.weight = weight;
        this.caloricGoal = caloricGoal;
        this.carbsGoal = carbGoal;
        this.proteinGoal = proteinGoal;
        this.fatGoal = fatGoal;
        this.waterGoal = waterGoal;
        this.caloricIntake = caloricIntake;
        this.carbsIntake = carbsIntake;
        this.proteinIntake = proteinIntake;
        this.fatIntake = fatIntake;
        this.breakfastIntake = breakfastIntake;
        this.lunchIntake = lunchIntake;
        this.dinnerIntake = dinnerIntake;
        this.extrasIntake = extrasIntake;
        this.waterIntake = waterIntake;
    }

    public DayData(String date, int weight, int caloricGoal, int carbGoal, int proteinGoal, int fatGoal, int waterGoal) {
        this(date, weight, caloricGoal, carbGoal, proteinGoal, fatGoal, waterGoal, 0, 0, 0 , 0 , 0 , 0, 0, 0, 0);

    }

    public DayData(String date) {
        this(date, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCaloricGoal() {
        return caloricGoal;
    }

    public void setCaloricGoal(int caloricGoal) {
        this.caloricGoal = caloricGoal;
    }

    public int getCarbsGoal() {
        return carbsGoal;
    }

    public void setCarbsGoal(int carbsGoal) {
        this.carbsGoal = carbsGoal;
    }

    public int getProteinGoal() {
        return proteinGoal;
    }

    public void setProteinGoal(int proteinGoal) {
        this.proteinGoal = proteinGoal;
    }

    public int getFatGoal() {
        return fatGoal;
    }

    public void setFatGoal(int fatGoal) {
        this.fatGoal = fatGoal;
    }

    public int getWaterGoal() {
        return waterGoal;
    }

    public void setWaterGoal(int waterGoal) {
        this.waterGoal = waterGoal;
    }

    public int getCaloricIntake() {
        return caloricIntake;
    }

    public void setCaloricIntake(int caloricIntake) {
        this.caloricIntake = caloricIntake;
    }

    public int getCarbsIntake() {
        return carbsIntake;
    }

    public void setCarbsIntake(int carbsIntake) {
        this.carbsIntake = carbsIntake;
    }

    public int getProteinIntake() {
        return proteinIntake;
    }

    public void setProteinIntake(int proteinIntake) {
        this.proteinIntake = proteinIntake;
    }

    public int getFatIntake() {
        return fatIntake;
    }

    public void setFatIntake(int fatIntake) {
        this.fatIntake = fatIntake;
    }

    public int getBreakfastIntake() {
        return breakfastIntake;
    }

    public void setBreakfastIntake(int breakfastIntake) {
        this.breakfastIntake = breakfastIntake;
    }

    public int getLunchIntake() {
        return lunchIntake;
    }

    public void setLunchIntake(int lunchIntake) {
        this.lunchIntake = lunchIntake;
    }

    public int getDinnerIntake() {
        return dinnerIntake;
    }

    public void setDinnerIntake(int dinnerIntake) {
        this.dinnerIntake = dinnerIntake;
    }

    public int getExtrasIntake() {
        return extrasIntake;
    }

    public void setExtrasIntake(int extrasIntake) {
        this.extrasIntake = extrasIntake;
    }

    public int getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
    }
}

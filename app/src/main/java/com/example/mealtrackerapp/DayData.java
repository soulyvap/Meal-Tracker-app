package com.example.mealtrackerapp;

public class DayData {
    private String date;
    private int weight;
    private int caloricGoal;
    private int carbPercent;
    private int proteinPercent;
    private int fatPercent;
    private int waterGoal;
    private int caloricIntake;
    private int carbIntake;
    private int proteinIntake;
    private int fatIntake;
    private int breakfastIntake;
    private int lunchIntake;
    private int dinnerIntake;
    private int ExtrasIntake;

    public DayData(String date, int weight, int caloricGoal, int carbPercent, int proteinPercent, int fatPercent, int waterGoal, int caloricIntake, int carbIntake, int proteinIntake, int fatIntake, int breakfastIntake, int lunchIntake, int dinnerIntake, int extrasIntake) {
        this.date = date;
        this.weight = weight;
        this.caloricGoal = caloricGoal;
        this.carbPercent = carbPercent;
        this.proteinPercent = proteinPercent;
        this.fatPercent = fatPercent;
        this.waterGoal = waterGoal;
        this.caloricIntake = caloricIntake;
        this.carbIntake = carbIntake;
        this.proteinIntake = proteinIntake;
        this.fatIntake = fatIntake;
        this.breakfastIntake = breakfastIntake;
        this.lunchIntake = lunchIntake;
        this.dinnerIntake = dinnerIntake;
        ExtrasIntake = extrasIntake;
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

    public int getCarbPercent() {
        return carbPercent;
    }

    public void setCarbPercent(int carbPercent) {
        this.carbPercent = carbPercent;
    }

    public int getProteinPercent() {
        return proteinPercent;
    }

    public void setProteinPercent(int proteinPercent) {
        this.proteinPercent = proteinPercent;
    }

    public int getFatPercent() {
        return fatPercent;
    }

    public void setFatPercent(int fatPercent) {
        this.fatPercent = fatPercent;
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

    public int getCarbIntake() {
        return carbIntake;
    }

    public void setCarbIntake(int carbIntake) {
        this.carbIntake = carbIntake;
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
        return ExtrasIntake;
    }

    public void setExtrasIntake(int extrasIntake) {
        ExtrasIntake = extrasIntake;
    }
}

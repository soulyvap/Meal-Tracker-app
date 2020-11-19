package com.example.mealtrackerapp;

public class Day {
    private int day;
    private int month;
    private int year;
    private int weight;
    private int caloricGoal;
    private int carbPercent;
    private int proteinPercent;
    private int fatPercent;
    private int waterAmount;

    public Day(int day, int month, int year, int weight, int caloricGoal, int carbPercent, int proteinPercent, int fatPercent, int waterAmount) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.weight = weight;
        this.caloricGoal = caloricGoal;
        this.carbPercent = carbPercent;
        this.proteinPercent = proteinPercent;
        this.fatPercent = fatPercent;
        this.waterAmount = waterAmount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }
}

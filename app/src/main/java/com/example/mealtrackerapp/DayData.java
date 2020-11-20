package com.example.mealtrackerapp;

import java.util.Date;

public class DayData {
    private Date date;
    private int weight;
    private int caloricGoal;
    private int carbPercent;
    private int proteinPercent;
    private int fatPercent;
    private int waterAmount;

    public DayData(Date date, int weight, int caloricGoal, int carbPercent, int proteinPercent, int fatPercent, int waterAmount) {
        this.date = date;
        this.weight = weight;
        this.caloricGoal = caloricGoal;
        this.carbPercent = carbPercent;
        this.proteinPercent = proteinPercent;
        this.fatPercent = fatPercent;
        this.waterAmount = waterAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public int getWaterAmount() {
        return waterAmount;
    }

    public void setWaterAmount(int waterAmount) {
        this.waterAmount = waterAmount;
    }
}

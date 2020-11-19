package com.example.mealtrackerapp;

public class Counter {
    private int count;
    private int startingValue;
    private int minValue;
    private int maxValue;

    public Counter(int startingValue, int minValue, int maxValue) {
        this.startingValue = startingValue;
        this.count = startingValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.startingValue = startingValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
}
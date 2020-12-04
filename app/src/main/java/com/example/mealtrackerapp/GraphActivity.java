package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WATER_INTAKE;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WEIGHT;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_CALORIES;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_CARBS;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_FAT;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_PROTEIN;

public class GraphActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String CHOICE_CALORIES = "calories";
    public static final String CHOICE_CARBS = "carbs";
    public static final String CHOICE_PROTEIN = "protein";
    public static final String CHOICE_FAT = "fat";
    public static final String CHOICE_WATER = "water";
    public static final String CHOICE_WEIGHT = "weight";
    public static final String CHOICE_WEEKLY = "weekly";
    public static final String CHOICE_MONTHLY = "monthly";
    Calendar calendar;
    FoodLogDBH foodLogDBH = new FoodLogDBH(GraphActivity.this);
    DayDataDBH dayDataDBH = new DayDataDBH(GraphActivity.this);
    Spinner timeChoiceSpinner, dataChoiceSpinner;
    ArrayList<String> timeChoiceArray, dataChoiceArray, xLabels;
    int numberOfDays;
    String dataSelected;

// reference for MPAndroidChart: https://github.com/PhilJay/MPAndroidChart
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    ValueFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        //refer bar chart
        barChart = (BarChart) findViewById(R.id.barChart);

        //refer spinners
        timeChoiceSpinner = findViewById(R.id.spinnerTimeChoice);
        dataChoiceSpinner = findViewById(R.id.spinnerDataChoice);

        //set click listeners for spinners
        timeChoiceSpinner.setOnItemSelectedListener(this);
        dataChoiceSpinner.setOnItemSelectedListener(this);

        //creating ArrayList with time period choices
        timeChoiceArray = new ArrayList<>();
        timeChoiceArray.add(CHOICE_WEEKLY);
        timeChoiceArray.add(CHOICE_MONTHLY);

        //creating ArrayList with meal choices
        dataChoiceArray = new ArrayList<>();
        dataChoiceArray.add(CHOICE_CALORIES);
        dataChoiceArray.add(CHOICE_CARBS);
        dataChoiceArray.add(CHOICE_PROTEIN);
        dataChoiceArray.add(CHOICE_FAT);
        dataChoiceArray.add(CHOICE_WATER);
        dataChoiceArray.add(CHOICE_WEIGHT);

        //setting up ArrayAdapter for time choice spinner
        ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_center, timeChoiceArray);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeChoiceSpinner.setAdapter(timeSpinnerAdapter);

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_center, dataChoiceArray);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataChoiceSpinner.setAdapter(mealSpinnerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        numberOfDays = 30;
        dataSelected = CHOICE_CALORIES;
        barEntries = getBarEntries(COLUMN_CALORIES, numberOfDays);
        updateGraphDisplay();
    }

    private void updateGraphDisplay() {
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        //creation of x labels
        xLabels = new ArrayList<>();

        switch (numberOfDays){
            case 7:
                for (int i = -numberOfDays + 1; i <= 0; i++) {
                    xLabels.add(getFirstLetter(getDayOfWeek(i)));
                }
                break;
            case 30:
                for (int i = -numberOfDays + 1; i <= 0; i++) {
                    xLabels.add(getFirstLetter(getDayOfWeek(i)));

                }
        }
        Log.d("graph", xLabels.toString());
        formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return xLabels.get((int)value);
            }
        };

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(formatter);

        //bar chart customization
        barChart.setFitBars(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        barDataSet.setColor(Color.rgb(249, 155, 130));
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);
    }

    private ArrayList<BarEntry> getBarEntries(String column, int numberOfDays) {

        ArrayList<String> dates = new ArrayList<>();
        for (int i = 0; i > -numberOfDays; i--) {
            dates.add(getDateString(i));
        }

        barEntries = new ArrayList<>();

        if (column.equals(COLUMN_CALORIES)) {
            float xAxis = 0f;
            for (String date: dates) {
                barEntries.add(new BarEntry(xAxis,foodLogDBH.getSumByDate(column, date)));
                xAxis += 1;
            }
        } else if (column.equals(COLUMN_CARBS) || column.equals(COLUMN_PROTEIN)) {
            float xAxis = 0f;
            for (String date: dates) {
                barEntries.add(new BarEntry(xAxis, (foodLogDBH.getSumByDate(column, date))*4));
                xAxis += 1;
            }
        } else if (column.equals(COLUMN_FAT)) {
            float xAxis = 0f;
            for (String date: dates) {
                barEntries.add(new BarEntry(xAxis, (foodLogDBH.getSumByDate(column, date))*8));
                xAxis += 1;
            }
        } else if (column.equals(COLUMN_WATER_INTAKE) || column.equals(COLUMN_WEIGHT)) {
            float xAxis = 0f;
            for (String date: dates) {
                barEntries.add(new BarEntry(xAxis, dayDataDBH.getIntByDate(column, date)));
                xAxis += 1;
            }
        }
        Log.d("graph", String.valueOf(barEntries.size()));
        return barEntries;
    }

    public String getDateString(int daySwitch) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daySwitch);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return day + "-" + (month+1) + "-" + year;
    }

    public String getDayOfWeek(int daySwitch) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daySwitch);
        return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }

    public String getDayOfMonth(int daySwitch) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daySwitch);
        return cal.getDisplayName(Calendar.DAY_OF_MONTH, Calendar.LONG, Locale.getDefault());
    }

    public String getFirstLetter(String word){
        return String.valueOf(word.charAt(0));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemSelected = (String) parent.getItemAtPosition(position);

        switch (parent.getId()) {
            case R.id.spinnerDataChoice:
                switch (itemSelected) {
                    case CHOICE_CALORIES:
                        dataSelected = CHOICE_CALORIES;
                        barEntries = getBarEntries(COLUMN_CALORIES, numberOfDays);
                        updateGraphDisplay();
                        break;
                    case CHOICE_CARBS:
                        dataSelected = CHOICE_CARBS;
                        barEntries = getBarEntries(COLUMN_CARBS, numberOfDays);
                        updateGraphDisplay();
                        break;
                    case CHOICE_PROTEIN:
                        dataSelected = CHOICE_PROTEIN;
                        barEntries = getBarEntries(COLUMN_PROTEIN, numberOfDays);
                        updateGraphDisplay();
                        break;
                    case CHOICE_FAT:
                        dataSelected = CHOICE_FAT;
                        barEntries = getBarEntries(COLUMN_FAT, numberOfDays);
                        updateGraphDisplay();
                        break;
                    case CHOICE_WATER:
                        dataSelected = CHOICE_WATER;
                        barEntries = getBarEntries(COLUMN_WATER_INTAKE, numberOfDays);
                        updateGraphDisplay();
                        break;
                    case CHOICE_WEIGHT:
                        dataSelected = CHOICE_WEIGHT;
                        barEntries = getBarEntries(COLUMN_WEIGHT, numberOfDays);
                        updateGraphDisplay();
                        break;
                }
                break;
            case R.id.spinnerTimeChoice:
                switch (itemSelected) {
                    case CHOICE_WEEKLY:
                        numberOfDays = 7;
                        switchData(dataSelected);
                        break;
                    case CHOICE_MONTHLY:
                        numberOfDays = 30;
                        switchData(dataSelected);
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void switchData(String itemSelected) {
        switch (itemSelected) {
            case CHOICE_CALORIES:
                barEntries = getBarEntries(COLUMN_CALORIES, numberOfDays);
                updateGraphDisplay();
                break;
            case CHOICE_CARBS:
                barEntries = getBarEntries(COLUMN_CARBS, numberOfDays);
                updateGraphDisplay();
                break;
            case CHOICE_PROTEIN:
                barEntries = getBarEntries(COLUMN_PROTEIN, numberOfDays);
                updateGraphDisplay();
                break;
            case CHOICE_FAT:
                barEntries = getBarEntries(COLUMN_FAT, numberOfDays);
                updateGraphDisplay();
                break;
            case CHOICE_WATER:
                barEntries = getBarEntries(COLUMN_WATER_INTAKE, numberOfDays);
                updateGraphDisplay();
                break;
            case CHOICE_WEIGHT:
                barEntries = getBarEntries(COLUMN_WEIGHT, numberOfDays);
                updateGraphDisplay();
                break;
        }
    }

}
package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
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
    Calendar calendar;
    FoodLogDBH foodLogDBH = new FoodLogDBH(GraphActivity.this);
    DayDataDBH dayDataDBH = new DayDataDBH(GraphActivity.this);
    Spinner dataChoiceSpinner;
    ArrayList<String> dataChoiceArray;
    String dataSelected, database;

// reference for MPAndroidChart: https://github.com/PhilJay/MPAndroidChart
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        barChart = (BarChart) findViewById(R.id.barChart);

        //refer spinner spinner
        dataChoiceSpinner = findViewById(R.id.spinnerDataChoice);

        //set click listener
        dataChoiceSpinner.setOnItemSelectedListener(this);

        //creating arrayList with meal choice
        dataChoiceArray = new ArrayList<>();
        dataChoiceArray.add(CHOICE_CALORIES);
        dataChoiceArray.add(CHOICE_CARBS);
        dataChoiceArray.add(CHOICE_PROTEIN);
        dataChoiceArray.add(CHOICE_FAT);
        dataChoiceArray.add(CHOICE_WATER);
        dataChoiceArray.add(CHOICE_WEIGHT);

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_center, dataChoiceArray);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataChoiceSpinner.setAdapter(mealSpinnerAdapter);

        dataSelected = CHOICE_CALORIES;
        database = "foodlogDBH";

    }

    @Override
    protected void onStart() {
        super.onStart();

        getEntries(getColumnName(dataSelected), database);
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        final String[] days = new String[] {getFirstLetter(getDayOfWeek(-6)), getFirstLetter(getDayOfWeek(-5)),
                getFirstLetter(getDayOfWeek(-4)), getFirstLetter(getDayOfWeek(-3)),
                getFirstLetter(getDayOfWeek(-2)), getFirstLetter(getDayOfWeek(-1)),
                getFirstLetter(getDayOfWeek(-0))};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return days[(int) value];
            }
        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
    }

    private void getEntries(String column, String databaseHelper) {
        //get today's date
        String dateNow = getDateString(0);
        //get dates of 6 previous days
        String dateMin1 = getDateString(-1);
        String dateMin2 = getDateString(-2);
        String dateMin3 = getDateString(-3);
        String dateMin4 = getDateString(-4);
        String dateMin5 = getDateString(-5);
        String dateMin6 = getDateString(-6);

        barEntries = new ArrayList<>();

        if (databaseHelper.equals("foodlogDBH")) {
            barEntries.add(new BarEntry(0f,foodLogDBH.getSumByDate(column, dateMin6)));
            barEntries.add(new BarEntry(1f,foodLogDBH.getSumByDate(column, dateMin5)));
            barEntries.add(new BarEntry(2f,foodLogDBH.getSumByDate(column, dateMin4)));
            barEntries.add(new BarEntry(3f,foodLogDBH.getSumByDate(column, dateMin3)));
            barEntries.add(new BarEntry(4f,foodLogDBH.getSumByDate(column, dateMin2)));
            barEntries.add(new BarEntry(5f,foodLogDBH.getSumByDate(column, dateMin1)));
            barEntries.add(new BarEntry(6f,foodLogDBH.getSumByDate(column, dateNow)));
        } if (databaseHelper.equals("dayDataDBH")) {
            barEntries.add(new BarEntry(0f,dayDataDBH.getIntByDate(column, dateMin6)));
            barEntries.add(new BarEntry(1f,dayDataDBH.getIntByDate(column, dateMin5)));
            barEntries.add(new BarEntry(2f,dayDataDBH.getIntByDate(column, dateMin4)));
            barEntries.add(new BarEntry(3f,dayDataDBH.getIntByDate(column, dateMin3)));
            barEntries.add(new BarEntry(4f,dayDataDBH.getIntByDate(column, dateMin2)));
            barEntries.add(new BarEntry(5f,dayDataDBH.getIntByDate(column, dateMin1)));
            barEntries.add(new BarEntry(6f,dayDataDBH.getIntByDate(column, dateNow)));
        }

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

    public String getFirstLetter(String word){
        return String.valueOf(word.charAt(0));
    }

    private String getColumnName(String data) {
        if (data.equals(CHOICE_CALORIES)) {
            return COLUMN_CALORIES;
        } else if (data.equals(CHOICE_CARBS)) {
            return COLUMN_CARBS;
        } else if (data.equals(CHOICE_PROTEIN)) {
            return COLUMN_PROTEIN;
        } else if (data.equals(CHOICE_FAT)) {
            return COLUMN_FAT;
        } else if (data.equals(CHOICE_WATER)) {
            return COLUMN_WATER_INTAKE;
        } else if (data.equals(CHOICE_WEIGHT)) {
            return COLUMN_WEIGHT;
        } else {
         return "";
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dataSelected = parent.getItemAtPosition(position).toString();
        if (dataSelected.equals(CHOICE_CALORIES) || dataSelected.equals(CHOICE_CARBS) || dataSelected.equals(CHOICE_PROTEIN) || dataSelected.equals(CHOICE_FAT)) {
            database = "foodlogDBH";
        } else {
            database = "dayDataDBH";
        }
        onStart();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
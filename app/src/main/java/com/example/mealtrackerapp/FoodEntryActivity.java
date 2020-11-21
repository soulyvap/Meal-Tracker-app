package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText timeDisplay, foodName, foodCalories, foodCarbs, foodProtein, foodFat;
    Spinner mealSpinner;
    ImageView btnClock;
    int hour, minutes;
    String mealSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        //meal selection with spinner
        mealSpinner = findViewById(R.id.spinnerMeal);

        //creating arrayList with meal choice
        ArrayList<String> meals = new ArrayList<>();
        meals.add("Choose meal ...");
        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Extras");

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meals);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealSpinner.setAdapter(mealSpinnerAdapter);

        //set click listener
        mealSpinner.setOnItemSelectedListener(this);

//        mealSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });


        //time selection with time picker dialog
        timeDisplay = findViewById(R.id.etxtTime);

        Calendar calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        timeDisplay.setText(getTimeString(hour, minutes));


        btnClock = findViewById(R.id.btnClock);
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(FoodEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeDisplay.setText(getTimeString(hourOfDay, minute));
                    }
                }, hour, minutes, true);
                timePickerDialog.show();
            }
        });
    }
    public String getTimeString(int hour, int minutes) {
        if (minutes < 10){
             String timeString = hour + ":" + 0 + minutes;
             return timeString;
        } else {
            return hour + ":" + minutes;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //storing meal item selected to a string
        mealSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, mealSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
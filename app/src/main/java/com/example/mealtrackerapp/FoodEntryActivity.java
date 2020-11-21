package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String EXTRA_TIME = "extraTime";
    public static final String EXTRA_MEAL = "extraMeal";
    public static final String EXTRA_FOOD_NAME = "extraFoodName";
    public static final String EXTRA_FOOD_CALORIES = "extraFoodCalories";
    public static final String EXTRA_FOOD_CARBS = "extraFoodCarbs";
    public static final String EXTRA_FOOD_PROTEIN = "extraFoodProtein";
    public static final String EXTRA_FOOD_FAT = "extraFoodFat";
    public static final String MEAL_SPINNER_DEFAULT = "Choose meal ...";
    EditText timeEditTxt, foodEditTxt, foodCaloriesEditTxt, foodCarbsEditTxt, foodProteinEditTxt, foodFatEditTxt;
    Spinner mealSpinner;
    ImageView btnClock;
    int hour, minutes;
    String mealSelected, time;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        //refer EditText
        timeEditTxt = findViewById(R.id.etxtTime);
        foodEditTxt = findViewById(R.id.etxtFoodName);
        foodCaloriesEditTxt = findViewById(R.id.etxtFoodCalories);
        foodCarbsEditTxt = findViewById(R.id.etxtFoodProtein);
        foodProteinEditTxt = findViewById(R.id.etxtFoodProtein);
        foodFatEditTxt = findViewById(R.id.etxtFoodProtein);

        //meal selection with spinner
        mealSpinner = findViewById(R.id.spinnerMeal);

        //creating arrayList with meal choice
        ArrayList<String> meals = new ArrayList<>();
        meals.add(MEAL_SPINNER_DEFAULT);
        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Extras");

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_center, meals);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealSpinner.setAdapter(mealSpinnerAdapter);

        //time selection with time picker dialog
        Calendar calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        timeEditTxt.setText(getTimeString(hour, minutes));


        btnClock = findViewById(R.id.btnClock);
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(FoodEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEditTxt.setText(getTimeString(hourOfDay, minute));
                        time = getTimeString(hourOfDay, minute);
                    }
                }, hour, minutes, true);
                timePickerDialog.show();
            }
        });

        //when press add button, gather information and return to main activity with extras
        btnAdd = findViewById(R.id.btnAddFood);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time == MEAL_SPINNER_DEFAULT ||
                        editTextIsEmpty(foodEditTxt) ||
                        editTextIsEmpty(foodCaloriesEditTxt) ||
                        editTextIsEmpty(foodCarbsEditTxt) ||
                        editTextIsEmpty(foodProteinEditTxt) ||
                        editTextIsEmpty(foodFatEditTxt)) {
                    Toast.makeText(FoodEntryActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    String foodName = foodEditTxt.getText().toString();
                    int calories = Integer.parseInt(foodCaloriesEditTxt.getText().toString());
                    int carbs = Integer.parseInt(foodCarbsEditTxt.getText().toString());
                    int protein = Integer.parseInt(foodProteinEditTxt.getText().toString());
                    int fat = Integer.parseInt(foodFatEditTxt.getText().toString());

                    Intent intent = new Intent(FoodEntryActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_TIME, time);
                    intent.putExtra(EXTRA_MEAL, mealSelected);
                    intent.putExtra(EXTRA_FOOD_NAME, foodName);
                    intent.putExtra(EXTRA_FOOD_CALORIES, calories);
                    intent.putExtra(EXTRA_FOOD_CARBS, carbs);
                    intent.putExtra(EXTRA_FOOD_PROTEIN, protein);
                    intent.putExtra(EXTRA_FOOD_FAT, fat);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * get time in a string in the format hh:mm (add 0 in front of minute if <10)
     * @param hour
     * @param minutes
     * @return
     */
    public String getTimeString(int hour, int minutes) {
        if (minutes < 10){
             String timeString = hour + ":" + 0 + minutes;
             return timeString;
        } else {
            return hour + ":" + minutes;
        }
    }

    /**
     * store meal selection in string
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //storing meal item selected to a string
        mealSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, mealSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean editTextIsEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

}
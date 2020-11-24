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
    public static final String EXTRA_FOOD_LOG = "extraFoodlog";
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

        //set click listener
        mealSpinner.setOnItemSelectedListener(this);

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
        time = getTimeString(hour, minutes);
        timeEditTxt.setText(time);


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
                if (mealSelected == MEAL_SPINNER_DEFAULT) {
                    Toast.makeText(FoodEntryActivity.this, "Please select meal", Toast.LENGTH_SHORT).show();
                } if (editTextIsEmpty(foodEditTxt)) {
                    foodEditTxt.setError("Please enter food name");
                } if (editTextIsEmpty(foodCaloriesEditTxt)) {
                    foodCaloriesEditTxt.setError("Please enter calorie amount");
                } if (editTextIsEmpty(foodCarbsEditTxt)) {
                    foodCarbsEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                } if (editTextIsEmpty(foodProteinEditTxt)) {
                    foodProteinEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                } if (editTextIsEmpty(foodFatEditTxt)) {
                    foodFatEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                } else {
                    String foodName = foodEditTxt.getText().toString().trim();
                    int calories = Integer.parseInt(foodCaloriesEditTxt.getText().toString().trim());
                    int carbs = Integer.parseInt(foodCarbsEditTxt.getText().toString().trim());
                    int protein = Integer.parseInt(foodProteinEditTxt.getText().toString().trim());
                    int fat = Integer.parseInt(foodFatEditTxt.getText().toString().trim());

                    FoodLog foodLog = new FoodLog(foodName, mealSelected, time, calories, carbs, protein, fat);
                    DataBaseHelper dbHelper = new DataBaseHelper(FoodEntryActivity.this);
                    boolean success = dbHelper.addOne(foodLog);

//

                    Intent intent = new Intent(FoodEntryActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_FOOD_LOG, foodLog);
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public boolean editTextIsEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

}
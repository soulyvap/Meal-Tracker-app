package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_DATE;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_DAY;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_MONTH;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_YEAR;

public class FoodEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String MEAL_SPINNER_DEFAULT = "Choose meal ...";
    public static final String EXTRA_FOOD_LOG = "extraFoodlog";
    EditText timeEditTxt, foodCaloriesEditTxt, foodCarbsEditTxt, foodProteinEditTxt,
            foodFatEditTxt, foodQtyEditTxt;
    TextView dateDisplay;
    Spinner mealSpinner;
    ImageView btnClock;
    int hour, minutes, day, month, year;
    String mealSelected, time, date, hourString, minutesString;
    Button btnAdd;
    ImageButton btnDelete;
    FoodLog selectedFoodlog;
    FoodLogDBH foodLogDBH;
    ArrayList<String> meals;
    FoodNutrientDB foodNutrientDB;
    FoodNutrients foodNutrientsSelected;
    AutoCompleteTextView foodEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        //refer EditText
        timeEditTxt = findViewById(R.id.etxtTime);
        foodEditTxt = findViewById(R.id.etxtFoodName);
        foodCaloriesEditTxt = findViewById(R.id.etxtFoodCalories);
        foodCarbsEditTxt = findViewById(R.id.etxtFoodCarbs);
        foodProteinEditTxt = findViewById(R.id.etxtFoodProtein);
        foodFatEditTxt = findViewById(R.id.etxtFoodFat);
        foodQtyEditTxt = findViewById(R.id.editTextQuantity);

        //refer buttons
        btnDelete = findViewById(R.id.btnDelete);
        btnClock = findViewById(R.id.btnClock);
        btnAdd = findViewById(R.id.btnAddFood);

        //refer spinner spinner
        mealSpinner = findViewById(R.id.spinnerMeal);

        //set click listener
        mealSpinner.setOnItemSelectedListener(this);

        //creating arrayList with meal choice
        meals = new ArrayList<>();
        meals.add(MEAL_SPINNER_DEFAULT);
        meals.add("breakfast");
        meals.add("lunch");
        meals.add("dinner");
        meals.add("extras");

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_center, meals);
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealSpinner.setAdapter(mealSpinnerAdapter);

        //ArrayAdapter for food name autocomplete
        foodNutrientDB = new FoodNutrientDB(FoodEntryActivity.this);
        ArrayAdapter<FoodNutrients> foodNutrientsAdapter = new ArrayAdapter<FoodNutrients>(this, R.layout.list_item_small, foodNutrientDB.getFoodNutrients());
        foodEditTxt.setAdapter(foodNutrientsAdapter);

        foodEditTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodNutrientsSelected = (FoodNutrients) parent.getItemAtPosition(position);
                foodCaloriesEditTxt.setText(String.valueOf(foodNutrientsSelected.getCalories()));
                foodCarbsEditTxt.setText(String.valueOf(foodNutrientsSelected.getCarbs()));
                foodProteinEditTxt.setText(String.valueOf(foodNutrientsSelected.getProtein()));
                foodFatEditTxt.setText(String.valueOf(foodNutrientsSelected.getFat()));
            }
        });

        foodQtyEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double quantity = Double.parseDouble(foodQtyEditTxt.getText().toString().trim())/100;
                    foodCaloriesEditTxt.setText(String.valueOf(Math.round(foodNutrientsSelected.getCalories()*quantity)));
                    foodCarbsEditTxt.setText(String.valueOf(foodNutrientsSelected.getCarbs()*quantity));
                    foodProteinEditTxt.setText(String.valueOf(foodNutrientsSelected.getProtein()*quantity));
                    foodFatEditTxt.setText(String.valueOf(foodNutrientsSelected.getFat()*quantity));
                } catch (Exception e) {

                }

            }
        });

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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mealSelected.equals(MEAL_SPINNER_DEFAULT)) {
                    Toast.makeText(FoodEntryActivity.this, "Please select meal", Toast.LENGTH_SHORT).show();
                    mealSpinner.requestFocus();
                } if (actvIsEmpty(foodEditTxt)) {
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
                    String foodName = foodEditTxt.getText().toString();
                    int calories = editTextToInt(foodCaloriesEditTxt);
                    double carbs = editTextToDouble(foodCarbsEditTxt);
                    double protein = editTextToDouble(foodProteinEditTxt);
                    double fat = editTextToDouble(foodFatEditTxt);

                    foodLogDBH = new FoodLogDBH(FoodEntryActivity.this);
                    if (getIntent().hasExtra(EXTRA_FOOD_LOG)){
                        foodLogDBH.updateOne(selectedFoodlog, foodName, mealSelected, time, calories, carbs, protein, fat);
                    } else {
                        FoodLog foodLog = new FoodLog(-1, date, foodName, mealSelected, time, calories, carbs, protein, fat);
                        foodLogDBH.addOne(foodLog);
                    }

                    Intent intent = new Intent(FoodEntryActivity.this, MainActivity.class);
                    intent.putExtra(EXTRA_DISPLAYED_DATE, date);
                    intent.putExtra(EXTRA_DISPLAYED_DAY, day);
                    intent.putExtra(EXTRA_DISPLAYED_MONTH, month);
                    intent.putExtra(EXTRA_DISPLAYED_YEAR, year);
                    startActivity(intent);
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodLogDBH = new FoodLogDBH(FoodEntryActivity.this);
                foodLogDBH.deleteOne(selectedFoodlog);
                Intent intent = new Intent(FoodEntryActivity.this, MainActivity.class);
                intent.putExtra(EXTRA_DISPLAYED_DATE, date);
                intent.putExtra(EXTRA_DISPLAYED_DAY, day);
                intent.putExtra(EXTRA_DISPLAYED_MONTH, month);
                intent.putExtra(EXTRA_DISPLAYED_YEAR, year);
                Toast.makeText(FoodEntryActivity.this, "Food log deleted", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //hide delete button
        btnDelete.setVisibility(ImageButton.GONE);

        //get meal spinner value
        mealSelected = mealSpinner.getSelectedItem().toString();

        //get intent
        Intent intent = getIntent();

        //time selection with time picker dialog
        Calendar calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        time = getTimeString(hour, minutes);
        timeEditTxt.setText(time);

        //show delete button if existing entry selected
        if (intent.hasExtra(EXTRA_FOOD_LOG)) {
            selectedFoodlog = (FoodLog) intent.getSerializableExtra(EXTRA_FOOD_LOG);
            btnDelete.setVisibility(ImageButton.VISIBLE);
            btnAdd.setText("Save");
            hour = timeStringToInt(selectedFoodlog.getTime(), "hour");
            minutes = timeStringToInt(selectedFoodlog.getTime(),"minutes");
            timeEditTxt.setText(selectedFoodlog.getTime());
            mealSpinner.setSelection(meals.indexOf(selectedFoodlog.getMeal()));
            foodEditTxt.setText(selectedFoodlog.getName());
            foodCaloriesEditTxt.setText(String.valueOf(selectedFoodlog.getCalories()));
            foodCarbsEditTxt.setText(String.valueOf(selectedFoodlog.getCarbs()));
            foodProteinEditTxt.setText((String.valueOf(selectedFoodlog.getProtein())));
            foodFatEditTxt.setText(String.valueOf(selectedFoodlog.getFat()));
        }

        //get date
        date = intent.getStringExtra(EXTRA_DISPLAYED_DATE);
        day = intent.getIntExtra(EXTRA_DISPLAYED_DAY, 0);
        month = intent.getIntExtra(EXTRA_DISPLAYED_MONTH, 0);
        year = intent.getIntExtra(EXTRA_DISPLAYED_YEAR, 0);
        dateDisplay = findViewById(R.id.txtEntryDate);
        dateDisplay.setText(date);
    }

    /**
     * get time in a string in the format hh:mm (add 0 in front of minute if <10)
     * @param hour
     * @param minutes
     * @return
     */
    public String getTimeString(int hour, int minutes) {

        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = Integer.toString(hour);
        }
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = Integer.toString(minutes);
        }
        return hourString + ":" + minutesString;
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

    public boolean actvIsEmpty(AutoCompleteTextView editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private int editTextToInt (EditText editText) {
        return Integer.parseInt(editText.getText().toString().trim());
    }

    private double editTextToDouble (EditText editText) {
        return Double.parseDouble(editText.getText().toString().trim());
    }

    public int timeStringToInt (String time, String option) {
        String[] array = time.split(":");
        String hourExtracted = array[0];
        String minutesExtracted = array[1];

        if (option.equals("minutes")){
            return Integer.parseInt(minutesExtracted);
        } else {
            return Integer.parseInt(hourExtracted);
        }
    }
}
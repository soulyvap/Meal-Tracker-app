package com.example.mealtrackerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealtrackerapp.other.CustomFoodAdapter;
import com.example.mealtrackerapp.databases.FoodLog;
import com.example.mealtrackerapp.databases.FoodLogDBH;
import com.example.mealtrackerapp.databases.FoodNutrientDB;
import com.example.mealtrackerapp.databases.FoodNutrients;
import com.example.mealtrackerapp.R;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.mealtrackerapp.activities.MainActivity.EXTRA_DISPLAYED_DATE;
import static com.example.mealtrackerapp.activities.MainActivity.EXTRA_DISPLAYED_DAY;
import static com.example.mealtrackerapp.activities.MainActivity.EXTRA_DISPLAYED_MONTH;
import static com.example.mealtrackerapp.activities.MainActivity.EXTRA_DISPLAYED_YEAR;

/**
 * New food dairy log entry activity. The user makes a new log in their food diary.
 * The user can select the time and meal.
 * Then the user can either input the caloric value and quantity of each macronutrient in the foods they ate, or use an autocompletion feature.
 * That feature suggests values from the FINELI food nutrient database. The app then autocompletes the fields accordingly.
 * Each input is a new entry in an ArrayList displayed on the screen as they are added in a temporary ArrayList. Each item of the list can be deleted/modified.
 * Finally, when the user is satisfied with the list of new entries. They can save them to the food log database.
 * The logs can be deleted/modified afterwards from this same activity.
 * The activity implements AdapterView.OnItemSelectedListener to handle the selection of spinner options
 */
public class FoodEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //constants for default spinner value and foodlog object extra
    public static final String MEAL_SPINNER_DEFAULT = "Choose meal ...";
    public static final String EXTRA_FOOD_LOG = "extraFoodlog";

    //initiation of variables
    EditText timeEditTxt, foodCaloriesEditTxt, foodCarbsEditTxt, foodProteinEditTxt,
            foodFatEditTxt, foodQtyEditTxt;
    TextView dateDisplay;
    Spinner mealSpinner;
    ImageView btnClock;
    int hour, minutes, day, month, year;
    String mealSelected, time, date, hourString, minutesString;
    Button btnAdd;
    ImageButton btnDelete;
    ImageView btnAddLine;
    FoodLog selectedFoodlog, clickedFoodLog;
    FoodLogDBH foodLogDBH;
    ArrayList<String> meals;
    FoodNutrientDB foodNutrientDB;
    FoodNutrients foodNutrientsSelected;
    AutoCompleteTextView foodEditTxt;
    ArrayList<FoodLog> foodlogList;
    ListView lvFoodlogEntry;
    Boolean modifying = false, fromMain = false;
    Intent intentMain;

    /**
     * Reference to UI elements. Creation of temporary food log ArrayList that bundles multiples entries from the user.
     * Creation of an ArrayList for the options of the meal selection spinner. Adaptor setup for meal spinner.
     * Creation of an ArrayList for the autocomplete text view.
     * The list is the populated with all the information from the FINELI food database, containing the nutritional content of many foods.
     * Adaptor setup for the autocomplete list.
     * When an item of the food entry list view displaying ArrayList with the user inputs is clicked, the UI is modified to allow modifications to that specific item.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        //refer EditText
        timeEditTxt = findViewById(R.id.etxtTime);
        foodEditTxt = findViewById(R.id.actvFoodName);
        foodCaloriesEditTxt = findViewById(R.id.etxtFoodCalories);
        foodCarbsEditTxt = findViewById(R.id.etxtFoodCarbs);
        foodProteinEditTxt = findViewById(R.id.etxtFoodProtein);
        foodFatEditTxt = findViewById(R.id.etxtFoodFat);
        foodQtyEditTxt = findViewById(R.id.editTextQuantity);

        //refer buttons
        btnDelete = findViewById(R.id.btnDelete);
        btnClock = findViewById(R.id.btnClock);
        btnAdd = findViewById(R.id.btnAddFood);
        btnAddLine = findViewById(R.id.btnAddNewLine);

        //initialize foodlog array list
        foodlogList = new ArrayList<>();

        //food log listView
        lvFoodlogEntry = findViewById(R.id.lvEntryFoodlogs);

        //refer spinner spinner
        mealSpinner = findViewById(R.id.spinnerMeal);

        //set click listener for spinner
        mealSpinner.setOnItemSelectedListener(this);

        //creating arrayList with meal choice
        meals = new ArrayList<>();
        meals.add(MEAL_SPINNER_DEFAULT);
        meals.add("breakfast");
        meals.add("lunch");
        meals.add("dinner");
        meals.add("extras");

        //setting up arrayAdapter for spinner with elements of the arrayList and attaching adapter to spinner object
        //creation of the ArrayAdapter
        ArrayAdapter<String> mealSpinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_center, meals);
        //setting up the dropdown list view
        mealSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //attaching the spinner to the adapter
        mealSpinner.setAdapter(mealSpinnerAdapter);

        //calling the FINELI database from assets
        foodNutrientDB = new FoodNutrientDB(FoodEntryActivity.this);

        //creating an adapter for autocomplete text view and creating all the views for each element of the FINELI database
        ArrayAdapter<FoodNutrients> foodNutrientsAdapter = new ArrayAdapter<>(this, R.layout.list_item_small, foodNutrientDB.getFoodNutrients());
        //attaching autocomplete view to adapter
        foodEditTxt.setAdapter(foodNutrientsAdapter);

        //setting a click listener for the items of the autocomplete list.
        //when a food is selected, the calories the app suggests values for the caloric and macronutrient content of the selected food.
        //the corresponding fields are filled up automatically with the values for 100 grams of the selected food.
        foodEditTxt.setOnItemClickListener((parent, view, position, id) -> {
            foodNutrientsSelected = (FoodNutrients) parent.getItemAtPosition(position);
            foodCaloriesEditTxt.setText(String.valueOf(foodNutrientsSelected.getCalories()));
            foodCarbsEditTxt.setText(String.valueOf(foodNutrientsSelected.getCarbs()));
            foodProteinEditTxt.setText(String.valueOf(foodNutrientsSelected.getProtein()));
            foodFatEditTxt.setText(String.valueOf(foodNutrientsSelected.getFat()));
            foodQtyEditTxt.setText("100");
        });

        //listener for modifications in foodQtyEditText. as the user types values in that field,
        //the caloric value and macronutrient are adjusted accordingly
        foodQtyEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            /**
             * As the user inputs a value in grams for a given food, that value is divided by 100, since the values for nutrition facts are given per 100 grams in the database.
             * The values suggested for calories and macronutrients are then adjusted accordingly simply by multiplying them by the value obtained previously and displayed.
             * The try/catch clause is used here to prevent the app from crashing if the input value is empty.
             * @param s
             */
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

        //when the clock button is clicked, a time picker dialog pops up. the default value is the current time.
        //when a time is picked, it is displayed on the screen and is stored as a string in a variable.
        btnClock.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(FoodEntryActivity.this, (view, hourOfDay, minute) -> {
                timeEditTxt.setText(getTimeString(hourOfDay, minute));
                time = getTimeString(hourOfDay, minute);
            }, hour, minutes, true);
            timePickerDialog.show();
        });


        //Adding new food log in the temporary array. All the inputs are checked before being added and displayed.
        //Checking if time is in the right format, if no fields are empty and that the meal spinner displays something else than the default value.
        //If it is a new entry a new food log object is created.
        //If the user clicked an item of the list to modify it, the existing food log's values are modified.
        btnAddLine.setOnClickListener(v -> {

            //Checking if time is in the right format, if no fields are empty and that the meal spinner displays something else than the default value
            if (isWrongTimeFormat(time)) {
                timeEditTxt.setError("Please enter time as hh:mm");
                return;
            } if (mealSelected.equals(MEAL_SPINNER_DEFAULT)) {
                Toast.makeText(FoodEntryActivity.this, "Please select meal", Toast.LENGTH_SHORT).show();
                mealSpinner.requestFocus();
                return;
            } if (actvIsEmpty(foodEditTxt)) {
                foodEditTxt.setError("Please enter food name");
                return;
            } if (editTextIsEmpty(foodCaloriesEditTxt)) {
                foodCaloriesEditTxt.setError("Please enter calorie amount");
                return;
            } if (editTextIsEmpty(foodCarbsEditTxt)) {
                foodCarbsEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                return;
            } if (editTextIsEmpty(foodProteinEditTxt)) {
                foodProteinEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                return;
            } if (editTextIsEmpty(foodFatEditTxt)) {
                foodFatEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                return;
            } else {
                String foodName = foodEditTxt.getText().toString();
                int calories = editTextToInt(foodCaloriesEditTxt);
                double carbs = editTextToDouble(foodCarbsEditTxt);
                double protein = editTextToDouble(foodProteinEditTxt);
                double fat = editTextToDouble(foodFatEditTxt);

                //checking if it is a modification or a new input
                if (modifying) {
                    clickedFoodLog.setDate(date);
                    clickedFoodLog.setName(foodName);
                    clickedFoodLog.setMeal(mealSelected);
                    clickedFoodLog.setTime(time);
                    clickedFoodLog.setCalories(calories);
                    clickedFoodLog.setCarbs(carbs);
                    clickedFoodLog.setProtein(protein);
                    clickedFoodLog.setFat(fat);
                    modifying = false;
                } else {
                    FoodLog newFoodlog = new FoodLog(-1, date, foodName, mealSelected, time, calories, carbs, protein, fat);
                    foodlogList.add(newFoodlog);
                }

                //creating a custom adapter to show the food log toString + a delete button
                ArrayAdapter foodLogAdapter = new CustomFoodAdapter(FoodEntryActivity.this, R.layout.list_item_with_delete, foodlogList);
                //attaching the list view to the adapter
                lvFoodlogEntry.setAdapter(foodLogAdapter);

                //resetting the fields for new inputs
                foodEditTxt.setText("");
                foodQtyEditTxt.setText("");
                foodCaloriesEditTxt.setText("");
                foodCarbsEditTxt.setText("");
                foodProteinEditTxt.setText("");
                foodFatEditTxt.setText("");
            }
        });

        //when an item in the temporary list is clicked, the corresponding food log object is retrieved and the fields are re-populated with its values
        lvFoodlogEntry.setOnItemClickListener((parent, view, position, id) -> {
            modifying = true;
            clickedFoodLog = (FoodLog) parent.getItemAtPosition(position);
            foodEditTxt.setText(clickedFoodLog.getName());
            foodCaloriesEditTxt.setText(String.valueOf(clickedFoodLog.getCalories()));
            foodCarbsEditTxt.setText(String.valueOf(clickedFoodLog.getCarbs()));
            foodProteinEditTxt.setText(String.valueOf(clickedFoodLog.getProtein()));
            foodFatEditTxt.setText(String.valueOf(clickedFoodLog.getFat()));
        });

        /**
         * Adding the temporary array to the food log database or modifying a row in the database.
         * If the activity was started by clicking an item of the food diary on the main activity,
         * all the input are checked the same way as when adding a new entry in the temporary list.
         * Then the selected food log is modified in the food log database.
         * If the activity was started from the main activities's add button, the temporary array is added to the database if it is not empty.
         * A toast will prompt the user to add at least one item.
         * In all cases, the main activity is then started with the displayed date as intent extra.
         */
        btnAdd.setOnClickListener(v -> {
            foodLogDBH = new FoodLogDBH(FoodEntryActivity.this);
            time = timeEditTxt.getText().toString().trim();

            //if food diary item clicked. modify it in database
            if (fromMain) {
                if (isWrongTimeFormat(time)) {
                    timeEditTxt.setError("Please enter time as hh:mm");
                    return;
                } if (mealSelected.equals(MEAL_SPINNER_DEFAULT)) {
                    Toast.makeText(FoodEntryActivity.this, "Please select meal", Toast.LENGTH_SHORT).show();
                    mealSpinner.requestFocus();
                    return;
                } if (actvIsEmpty(foodEditTxt)) {
                    foodEditTxt.setError("Please enter food name");
                    return;
                } if (editTextIsEmpty(foodCaloriesEditTxt)) {
                    foodCaloriesEditTxt.setError("Please enter calorie amount");
                    return;
                } if (editTextIsEmpty(foodCarbsEditTxt)) {
                    foodCarbsEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                    return;
                } if (editTextIsEmpty(foodProteinEditTxt)) {
                    foodProteinEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                    return;
                } if (editTextIsEmpty(foodFatEditTxt)) {
                    foodFatEditTxt.setError("Please enter carbohydrate/protein/fat amount");
                    return;
                } else {
                    String foodName = foodEditTxt.getText().toString();
                    int calories = editTextToInt(foodCaloriesEditTxt);
                    double carbs = editTextToDouble(foodCarbsEditTxt);
                    double protein = editTextToDouble(foodProteinEditTxt);
                    double fat = editTextToDouble(foodFatEditTxt);
                    foodLogDBH.updateOne(selectedFoodlog, foodName, mealSelected, time, calories, carbs, protein, fat);
                    fromMain = false;
                    startActivity(intentMain);
                }
            //if new entries, they are added to the database after checking if the temporary array is not empty
            } else {
                if (foodlogList.size() == 0) {
                    Toast.makeText(FoodEntryActivity.this, "Please enter (+) at least one food", Toast.LENGTH_SHORT).show();
                } else {
                    foodLogDBH.addArray(foodlogList);
                    startActivity(intentMain);
                }
            }
        });

        //when the delete button is pressed (only possible if the food entry activity was started for modification),
        //the selected food log is removed from the database. The main activity is started with the displayed date as extra
        btnDelete.setOnClickListener(v -> {
            foodLogDBH = new FoodLogDBH(FoodEntryActivity.this);
            foodLogDBH.deleteOne(selectedFoodlog);
            Toast.makeText(FoodEntryActivity.this, "Food log deleted", Toast.LENGTH_SHORT).show();
            startActivity(intentMain);
        });
    }

    /**
     * By default the delete button is hidden. The meal spinner's default value is stored in a vairable.
     * If there is is a food log as an extra in the intent (meaning the activity is used for the modification of an existing food log),
     * the fields are pre-populated with the clicked food log's data and the delete button is made visible.
     */
    @Override
    protected void onStart() {
        super.onStart();

        //hide delete button
        btnDelete.setVisibility(ImageButton.GONE);

        //get meal spinner value
        mealSelected = mealSpinner.getSelectedItem().toString();

        //get intent
        Intent intent = getIntent();

        //retrieving current time for time picker dialog default value
        Calendar calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        time = getTimeString(hour, minutes);
        timeEditTxt.setText(time);

        //show delete button if existing entry selected and populating the fields with relevant info.
        if (intent.hasExtra(EXTRA_FOOD_LOG)) {
            fromMain = true;
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
            btnAddLine.setVisibility(View.GONE);
            lvFoodlogEntry.setVisibility(View.GONE);
            foodNutrientsSelected = foodNutrientDB.getFoodNutrientsByName(selectedFoodlog.getName());
        }
        //getting the date from the intent extra.
        date = intent.getStringExtra(EXTRA_DISPLAYED_DATE);
        day = intent.getIntExtra(EXTRA_DISPLAYED_DAY, 0);
        month = intent.getIntExtra(EXTRA_DISPLAYED_MONTH, 0);
        year = intent.getIntExtra(EXTRA_DISPLAYED_YEAR, 0);
        dateDisplay = findViewById(R.id.txtEntryDate);
        dateDisplay.setText(date);

        //prepare intent back to main activity
        intentMain = new Intent(FoodEntryActivity.this, MainActivity.class);
        intentMain.putExtra(EXTRA_DISPLAYED_DATE, date);
        intentMain.putExtra(EXTRA_DISPLAYED_DAY, day);
        intentMain.putExtra(EXTRA_DISPLAYED_MONTH, month);
        intentMain.putExtra(EXTRA_DISPLAYED_YEAR, year);
    }

    /**
     * On up button press (home), making sure that the date is sent back to the main
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(intentMain);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    /**
     * get time in a string in the format hh:mm (add 0 in front of hour or minute if <10)
     * @param hour Integer representing the time of the day
     * @param minutes Integer representing minutes
     * @return String with the time in hh:mm format
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
     * Store meal selection in string
     * @param parent Adapter view for the spinner
     * @param view SpinnerView
     * @param position Position in the array of the clicked item
     * @param id id of the item selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //storing meal item selected to a string
        mealSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Check if edit text is empty
     * @param editText edit text to be checked
     * @return true if edit text is empty
     */
    private boolean editTextIsEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    /**
     * Check if autocomplete text is empty
     * @param editText autocomplete text to be checked
     * @return true if edit text is empty
     */
    public boolean actvIsEmpty(AutoCompleteTextView editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    /**
     * Getting the content of an edit text as a trimmed string converted to an integer
     * @param editText edit text to be fetched
     * @return Integer of the trimmed content of the edit text
     */
    private int editTextToInt (EditText editText) {
        return Integer.parseInt(editText.getText().toString().trim());
    }

    /**
     * Getting the content of an edit text as a trimmed string converted to an double
     * @param editText edit text to be fetched
     * @return Double of the trimmed content of the edit text
     */
    private double editTextToDouble (EditText editText) {
        return Double.parseDouble(editText.getText().toString().trim());
    }

    /**
     * Convering date string to its corresponding date integers, according to option selected (minutes or hours)
     * @param time Dated String to be extracted from
     * @param option "hour" or "minutes"
     * @return Integer representing either the hour of the day or the minutes of an hour
     */
    public int timeStringToInt (String time, String option) {
        String[] array = time.split(":");
        String hourExtracted = array[0];
        String minutesExtracted = array[1];

        if (option.equals("minutes")){
            return Integer.parseInt(minutesExtracted);
        } else if (option.equals("hour")){
            return Integer.parseInt(hourExtracted);
        } else {
            return 0;
        }
    }

    /**
     * Checking if a time string is in the format hh:mm.
     * @param time Time String to be checked
     * @return true if the string is in hh:mm format
     */
    private Boolean isWrongTimeFormat(String time) {
        String trimmedTime = time.trim();
        String[] timeParts = trimmedTime.split(":");
        return timeParts[0].length() != 2 || timeParts[1].length() != 2 || timeParts.length != 2;
    }
}
package com.example.mealtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import static com.example.mealtrackerapp.DayDataDHB.COLUMN_CALORIC_GOAL;
import static com.example.mealtrackerapp.DayDataDHB.COLUMN_CARBS_GOAL;
import static com.example.mealtrackerapp.DayDataDHB.COLUMN_FAT_GOAL;
import static com.example.mealtrackerapp.DayDataDHB.COLUMN_PROTEIN_GOAL;
import static com.example.mealtrackerapp.DayDataDHB.COLUMN_WATER_GOAL;
import static com.example.mealtrackerapp.DayDataDHB.COLUMN_WATER_INTAKE;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_CARBS;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_FAT;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_PROTEIN;
import static com.example.mealtrackerapp.SetupActivity.PREF_CALORIC_GOAL;
import static com.example.mealtrackerapp.SetupActivity.PREF_CARBS;
import static com.example.mealtrackerapp.SetupActivity.PREF_FAT;
import static com.example.mealtrackerapp.SetupActivity.PREF_PROTEIN;
import static com.example.mealtrackerapp.SetupActivity.PREF_WATER_GOAL;
import static com.example.mealtrackerapp.SetupActivity.PREF_WEIGHT;
import static com.example.mealtrackerapp.SetupActivity.SETUP_PREF;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PREF_WATER = "pref_water";
    public static final String CALORIC_COUNTER_VALUE = "caloricCounterValue";
    public static final String PREF_BREAKFAST_CALORIES = "pref_breakfast_calories";
    public static final String PREF_LUNCH_CALORIES = "pref_lunch_calories";
    public static final String PREF_DINNER_CALORIES = "pref_dinner_calories";
    public static final String PREF_EXTRAS_CALORIES = "pref_extras_calories";
    public static final String FIRST_TIME_PREF = "first_time_pref";
    public static final String IS_FIRST_LAUNCH_PREF = "isFirstLaunch";
    public static final String EXTRA_DISPLAYED_DATE = "extra_displayed_date";
    public static final String EXTRA_DISPLAYED_DAY = "extra_displayed_day";
    public static final String EXTRA_DISPLAYED_MONTH = "extra_displayed_month";
    public static final String EXTRA_DISPLAYED_YEAR = "extra_displayed_year";
    //references to views on the layout
    TextView caloricGoalDisplay, eatenDisplay, caloriesLeftDisplay, breakfastDisplay, lunchDisplay,
            dinnerDisplay, extrasDisplay, waterDisplay, waterMinus, waterPlus, dateNowDisplay;
    ProgressBar circularPB, carbsPB, proteinPB, fatPB;
    FloatingActionButton fabAdd, fabDatePicker;
    int weightInt, caloricGoalInt, caloriesLeftInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, carbShare,
            proteinShare, fatShare, caloricIntake, carbsIntake, proteinIntake, fatIntake, breakfastIntake,
            lunchIntake, dinnerIntake, extrasIntake, waterIntake;
    ListView lvFoodLogs;

    //counters
    Counter caloricCounter, breakfastCounter, lunchCounter, dinnerCounter, extrasCounter,
            waterCounter, carbsCounter, proteinCounter, fatCounter;

    //shared preferences
    SharedPreferences setupPref;
    SharedPreferences.Editor prefEditor;

    //date picker variables
    Calendar calendar;
    int dayToday, monthToday, yearToday, dayDisplayed, monthDisplayed, yearDisplayed;
    String dateToday, dateDisplayed, datePicked;

    //database helpers
    DayDataDHB dayDataDHB;
    FoodLogDBH foodLogDBH;

    //dayData
    private DayData daydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first launch
        Boolean isFirstLaunch = getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).getBoolean(IS_FIRST_LAUNCH_PREF, true);
        if (isFirstLaunch) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).edit().putBoolean(IS_FIRST_LAUNCH_PREF, false).apply();

        //create databases for day data
        dayDataDHB = new DayDataDHB(MainActivity.this);
        foodLogDBH = new FoodLogDBH(MainActivity.this);

        //references to caloric counters display, date display and water count
        caloricGoalDisplay = findViewById(R.id.txtCaloricGoalValue);
        eatenDisplay = findViewById(R.id.txtCaloriesEatenValue);
        caloriesLeftDisplay = findViewById(R.id.txtCaloriesLeftValue);
        breakfastDisplay = findViewById(R.id.txtBreakfastValue);
        lunchDisplay = findViewById(R.id.txtLunchValue);
        dinnerDisplay = findViewById(R.id.txtDinnerValue);
        extrasDisplay = findViewById(R.id.txtExtrasValue);
        dateNowDisplay = findViewById(R.id.txtDateNow);
        waterDisplay = findViewById(R.id.txtWaterCountValue);

        //reference to list view
        lvFoodLogs = findViewById(R.id.lvFoodLogs);

        //reference to progress bars
        circularPB = findViewById(R.id.circularProgressionBar);
        carbsPB = findViewById(R.id.carbPB);
        proteinPB = findViewById(R.id.proteinPB);
        fatPB = findViewById(R.id.fatPB);

        //water counter buttons and functions
        waterMinus = findViewById(R.id.txtWaterMinus);
        waterPlus = findViewById(R.id.txtWaterPlus);

        //floating action button add to food entry
        fabAdd = findViewById(R.id.fabBtnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodEntryActivity.class);
                intent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_DAY, dayDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_MONTH, monthDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_YEAR, yearDisplayed);
                startActivity(intent);
            }
        });

        //date picker button and functions
        fabDatePicker = findViewById(R.id.fabBtnDate);
        fabDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayDataDHB.addOneReplace(dateDisplayed, weightInt, caloricGoalInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, waterIntake);
                showDateDialog();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //get intent
        Intent intent = getIntent();

        //get today's date and display it
        calendar = Calendar.getInstance();
        dayToday = calendar.get(Calendar.DAY_OF_MONTH);
        monthToday = calendar.get(Calendar.MONTH);
        yearToday = calendar.get(Calendar.YEAR);

        dayDisplayed = dayToday;
        monthDisplayed = monthToday;
        yearDisplayed = yearToday;

        dateToday = dayToday + "-" + (monthToday + 1) + "-" + yearToday;
        dateNowDisplay.setText(dateToday);
        dateDisplayed = dateToday;

        //retrieve date from intent
        if (intent.hasExtra(EXTRA_DISPLAYED_DATE)){
            dateDisplayed = intent.getStringExtra(EXTRA_DISPLAYED_DATE);
            dayDisplayed = intent.getIntExtra(EXTRA_DISPLAYED_DAY, dayToday);
            monthDisplayed = intent.getIntExtra(EXTRA_DISPLAYED_MONTH, monthToday);
            yearDisplayed = intent.getIntExtra(EXTRA_DISPLAYED_YEAR, yearToday);
            dateNowDisplay.setText(dateDisplayed);
        }
        

        //get shared preferences for profile and goals
        setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);

        //retrieve data from sharedPreferences
        weightInt = setupPref.getInt(PREF_WEIGHT, 0);
        caloricGoalInt = setupPref.getInt(PREF_CALORIC_GOAL, 0);
        carbGoalInt = setupPref.getInt(PREF_CARBS, 0);
        proteinGoalInt = setupPref.getInt(PREF_PROTEIN, 0);
        fatGoalInt = setupPref.getInt(PREF_FAT, 0);
        waterGoalInt = setupPref.getInt(PREF_WATER_GOAL, 0);

        //create waterCounter
        waterCounter = new Counter();

        //set water count
        waterCounter.setCount(dayDataDHB.getIntByDate(COLUMN_WATER_INTAKE, dateDisplayed));
        waterIntake = waterCounter.getCount();

        //store goals and weight to daydata.db only if there is no data yet
        dayDataDHB.addOneIgnore(dateDisplayed, weightInt, caloricGoalInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, 0);

        //set displayed caloric counter (goal, eaten and left) values
        //set calories per meal
        //set waterCount color
        //set progress bar max values
        //set food diary
        setValuesTo(dateDisplayed);

        waterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.remove(1);
                dayDataDHB.updateColumnWhereDateTo(COLUMN_WATER_INTAKE, dateDisplayed, waterCounter.getCount());
                updateWaterDisplay(dateDisplayed);
            }
        });
        waterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.add(1);
                dayDataDHB.updateColumnWhereDateTo(COLUMN_WATER_INTAKE, dateDisplayed, waterCounter.getCount());
                updateWaterDisplay(dateDisplayed);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save dayData into daydata.db
        dayDataDHB.addOneReplace(dateDisplayed, weightInt, caloricGoalInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, waterIntake);
    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void setValuesTo(String date) {
        updateCaloricCountersDisplays(date);
        updateMealDisplays(date);
        updateMacroPBs(date);
        updateWaterDisplay(date);
        updateFoodDiary(date);
    }

    private void updateCaloricCountersDisplays(String date) {
        //get caloric values from databases
        caloricIntake = foodLogDBH.getCaloriesByDate(date);
        caloricGoalInt = dayDataDHB.getIntByDate(COLUMN_CALORIC_GOAL, date);
        caloriesLeftInt = caloricGoalInt - caloricIntake;
        //set caloric values to TextViews
        eatenDisplay.setText(String.valueOf(caloricIntake));
        caloricGoalDisplay.setText(String.valueOf(caloricGoalInt));
        caloriesLeftDisplay.setText(String.valueOf(caloriesLeftInt));
        //set circular caloric progress bar progress and max values
        circularPB.setMax(caloricGoalInt);
        circularPB.setProgress(caloricIntake);
    }

    private void updateMacroPBs (String date) {
        //get macro goals from daydata.db
        carbGoalInt = dayDataDHB.getIntByDate(COLUMN_CARBS_GOAL, date);
        proteinGoalInt = dayDataDHB.getIntByDate(COLUMN_PROTEIN_GOAL, date);
        fatGoalInt = dayDataDHB.getIntByDate(COLUMN_FAT_GOAL, date);
        //compute macro shares as percentages of caloric goal
        carbShare = (caloricGoalInt * carbGoalInt)/100;
        proteinShare = (caloricGoalInt * proteinGoalInt)/100;
        fatShare = (caloricGoalInt * fatGoalInt)/100;
        //get sum of grams per macro from foodlog.db and convert to calories
        carbsIntake = foodLogDBH.getSumByDate(COLUMN_CARBS, date) * 4;
        proteinIntake = foodLogDBH.getSumByDate(COLUMN_PROTEIN, date) * 4;
        fatIntake = foodLogDBH.getSumByDate(COLUMN_FAT, date) * 8;
        //set macro progress bars
        carbsPB.setMax(carbShare);
        proteinPB.setMax(proteinShare);
        fatPB.setMax(fatShare);
        carbsPB.setProgress(carbsIntake);
        proteinPB.setProgress(proteinIntake);
        fatPB.setProgress(fatIntake);
        //show in log
        Log.d("macro", "carbs " + String.valueOf(carbShare));
        Log.d("macro", "protein " + String.valueOf(proteinShare));
        Log.d("macro", "fat " + String.valueOf(fatShare));
    }

    private void updateFoodDiary(String date) {
        //get FoodLog list from foodlog.db
        List<FoodLog> foodLogs = foodLogDBH.getFoodLog(date);
        //create adapter for FoodLog list
        ArrayAdapter<FoodLog> foodLogArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, foodLogs);
        //attach list to adapter
        lvFoodLogs.setAdapter(foodLogArrayAdapter);
    }

    private void updateMealDisplays(String date) {
        //get sum of calories per meal on a given date from foodlog.db
        breakfastIntake = foodLogDBH.getCaloriesByMealByDate("breakfast", date);
        lunchIntake = foodLogDBH.getCaloriesByMealByDate("lunch", date);
        dinnerIntake = foodLogDBH.getCaloriesByMealByDate("dinner", date);
        extrasIntake = foodLogDBH.getCaloriesByMealByDate("extras", date);
        //set calories per meal values to TextViews
        breakfastDisplay.setText(String.valueOf(breakfastIntake));
        lunchDisplay.setText(String.valueOf(lunchIntake));
        dinnerDisplay.setText(String.valueOf(dinnerIntake));
        extrasDisplay.setText(String.valueOf(extrasIntake));
    }

    private void updateWaterDisplay(String date) {
        //get water intake and goal from daydata.db
        waterIntake = dayDataDHB.getIntByDate(COLUMN_WATER_INTAKE, date);
        waterGoalInt = dayDataDHB.getIntByDate(COLUMN_WATER_GOAL, date);
        //switch color of water count according to completion of daily water consumption goal
        if (waterCounter.getCount() >= waterGoalInt) {
            waterDisplay.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
        } else {
            waterDisplay.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
        }
        //set water intake to TextView
        waterDisplay.setText(waterCounter.getCountString());
    }

    /**
     * Inflating menu object (reading xml and making menu visible in app)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Define action on menu item selection
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSetup:
                Intent intent = new Intent(MainActivity.this, SetupActivity.class);
                intent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_DAY, dayDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_MONTH, monthDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_YEAR, yearDisplayed);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDateDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePicked = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                        dateDisplayed = datePicked;
                        yearDisplayed = year;
                        monthDisplayed = monthOfYear;
                        dayDisplayed = dayOfMonth;
                        dateNowDisplay.setText(datePicked);
                        dayDataDHB.addOneIgnore(dateDisplayed, weightInt, caloricGoalInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, 0);
                        waterCounter.setCount(dayDataDHB.getIntByDate(COLUMN_WATER_INTAKE, dateDisplayed));
                        setValuesTo(dateDisplayed);
                    }
                }, yearDisplayed, monthDisplayed, dayDisplayed);
        datePickerDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabBtnDate:
                showDateDialog();
                break;
        }
    }
}
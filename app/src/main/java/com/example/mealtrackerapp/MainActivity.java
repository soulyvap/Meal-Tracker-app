package com.example.mealtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import static com.example.mealtrackerapp.DayDataDBH.COLUMN_CALORIC_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_CARBS_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_FAT_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_PROTEIN_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WATER_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WATER_INTAKE;
import static com.example.mealtrackerapp.FoodEntryActivity.EXTRA_FOOD_LOG;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_CARBS;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_FAT;
import static com.example.mealtrackerapp.FoodLogDBH.COLUMN_PROTEIN;
import static com.example.mealtrackerapp.SetupActivity.PREF_CALORIC_GOAL;
import static com.example.mealtrackerapp.SetupActivity.PREF_CARBS;
import static com.example.mealtrackerapp.SetupActivity.PREF_FAT;
import static com.example.mealtrackerapp.SetupActivity.PREF_FIRSTNAME;
import static com.example.mealtrackerapp.SetupActivity.PREF_PROTEIN;
import static com.example.mealtrackerapp.SetupActivity.PREF_WATER_GOAL;
import static com.example.mealtrackerapp.SetupActivity.PREF_WEIGHT;
import static com.example.mealtrackerapp.SetupActivity.SETUP_PREF;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FIRST_TIME_PREF = "first_time_pref";
    public static final String IS_FIRST_LAUNCH_PREF = "isFirstLaunch";
    public static final String EXTRA_DISPLAYED_DATE = "extra_displayed_date";
    public static final String EXTRA_DISPLAYED_DAY = "extra_displayed_day";
    public static final String EXTRA_DISPLAYED_MONTH = "extra_displayed_month";
    public static final String EXTRA_DISPLAYED_YEAR = "extra_displayed_year";
    //references to views on the layout
    TextView caloricGoalDisplay, eatenDisplay, caloriesLeftDisplay, breakfastDisplay, lunchDisplay,
            dinnerDisplay, extrasDisplay, waterDisplay, waterMinus, waterPlus, dateNowDisplay,
            txtCaloriesLeft, txtCarbs, txtProtein, txtFat, txtEatenDisplay;
    ProgressBar circularPB, carbsPB, proteinPB, fatPB;
    FloatingActionButton fabAdd, fabDatePicker;
    int weightInt, caloricGoalInt, caloriesLeftInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, carbShare,
            proteinShare, fatShare, caloricIntake, carbsIntake, proteinIntake, fatIntake, breakfastIntake,
            lunchIntake, dinnerIntake, extrasIntake, waterIntake;
    ListView lvFoodLogs;

    //counters
    Counter waterCounter;

    //shared preferences
    SharedPreferences setupPref;
    SharedPreferences.Editor prefEditor;

    //date picker variables
    Calendar calendar;
    int dayToday, monthToday, yearToday, dayDisplayed, monthDisplayed, yearDisplayed;
    String dateToday, dateDisplayed, datePicked;

    //database helpers
    DayDataDBH dayDataDHB;
    FoodLogDBH foodLogDBH;

    //dayData
    private DayData daydata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        //first launch
        Boolean isFirstLaunch = getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).getBoolean(IS_FIRST_LAUNCH_PREF, true);
        if (isFirstLaunch) {
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).edit().putBoolean(IS_FIRST_LAUNCH_PREF, false).apply();

        //create databases for day data
        dayDataDHB = new DayDataDBH(MainActivity.this);
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
        txtCaloriesLeft = findViewById(R.id.txtCaloriesLeft);
        txtCarbs = findViewById(R.id.txtCarbs);
        txtProtein = findViewById(R.id.txtProteins);
        txtFat = findViewById(R.id.txtFat);
        txtEatenDisplay = findViewById(R.id.txtCaloriesEaten);

        //reference to list view
        lvFoodLogs = findViewById(R.id.lvFoodLogs);

        //reference to progress bars
        circularPB = findViewById(R.id.circularProgressionBar);
        carbsPB = findViewById(R.id.carbPB);
        proteinPB = findViewById(R.id.proteinPB);
        fatPB = findViewById(R.id.fatPB);

        //create waterCounter
        waterCounter = new Counter();

        //water counter buttons and functions
        waterMinus = findViewById(R.id.txtWaterMinus);
        waterPlus = findViewById(R.id.txtWaterPlus);

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
        lvFoodLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FoodLog selectedFoodLog = (FoodLog) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, FoodEntryActivity.class);
                intent.putExtra(EXTRA_FOOD_LOG, selectedFoodLog);
                intent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_DAY, dayDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_MONTH, monthDisplayed);
                intent.putExtra(EXTRA_DISPLAYED_YEAR, yearDisplayed);
                startActivity(intent);
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
        if (caloricIntake <= caloricGoalInt) {
            txtCaloriesLeft.setText("Left");
            caloriesLeftDisplay.setText(String.valueOf(caloriesLeftInt));
            eatenDisplay.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
            txtEatenDisplay.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        } else {
            txtCaloriesLeft.setText("Over");
            caloriesLeftDisplay.setText(String.valueOf(Math.abs(caloriesLeftInt)));
            eatenDisplay.setTextColor(getResources().getColor(R.color.orange_pastel));
            txtEatenDisplay.setTextColor(getResources().getColor(R.color.orange_pastel));

        }
        caloricGoalDisplay.setText(String.valueOf(caloricGoalInt));
        eatenDisplay.setText(String.valueOf(caloricIntake));
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
        //change text color if daily limit reached
        if (carbsIntake > carbShare) {
            txtCarbs.setTextColor(getResources().getColor(R.color.orange_pastel));
        } else {
            txtCarbs.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        }
        if (proteinIntake > proteinShare) {
            txtProtein.setTextColor(getResources().getColor(R.color.orange_pastel));
        } else {
            txtProtein.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        }
        if (fatIntake > fatShare) {
            txtFat.setTextColor(getResources().getColor(R.color.orange_pastel));
        } else {
            txtFat.setTextColor(getResources().getColor(android.R.color.tab_indicator_text));
        }
        //set macro progress bars
        carbsPB.setMax(carbShare);
        proteinPB.setMax(proteinShare);
        fatPB.setMax(fatShare);
        carbsPB.setProgress(carbsIntake);
        proteinPB.setProgress(proteinIntake);
        fatPB.setProgress(fatIntake);
    }

    private void updateFoodDiary(String date) {
        //get FoodLog list from foodlog.db
        List<FoodLog> foodLogs = foodLogDBH.getFoodLog(date);
        //create adapter for FoodLog list
        ArrayAdapter<FoodLog> foodLogArrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item_small, foodLogs);
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
        int itemSelected = item.getItemId();
        if (itemSelected == R.id.menuItemSetup) {
            Intent intent = new Intent(MainActivity.this, SetupActivity.class);
            intent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
            intent.putExtra(EXTRA_DISPLAYED_DAY, dayDisplayed);
            intent.putExtra(EXTRA_DISPLAYED_MONTH, monthDisplayed);
            intent.putExtra(EXTRA_DISPLAYED_YEAR, yearDisplayed);
            startActivity(intent);
        } if (itemSelected == R.id.menuItemStats){
            Intent graphIntent = new Intent(MainActivity.this, GraphActivity.class);
            graphIntent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
            graphIntent.putExtra(EXTRA_DISPLAYED_DAY, dayDisplayed);
            graphIntent.putExtra(EXTRA_DISPLAYED_MONTH, monthDisplayed);
            graphIntent.putExtra(EXTRA_DISPLAYED_YEAR, yearDisplayed);
            startActivity(graphIntent);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
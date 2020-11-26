package com.example.mealtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static com.example.mealtrackerapp.FoodEntryActivity.EXTRA_FOOD_LOG;
import static com.example.mealtrackerapp.SetupActivity.PREF_CALORIC_GOAL;
import static com.example.mealtrackerapp.SetupActivity.PREF_CARBS;
import static com.example.mealtrackerapp.SetupActivity.PREF_FAT;
import static com.example.mealtrackerapp.SetupActivity.PREF_PROTEIN;
import static com.example.mealtrackerapp.SetupActivity.PREF_WATER_GOAL;
import static com.example.mealtrackerapp.SetupActivity.SETUP_PREF;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_WATER = "pref_water";
    public static final String CALORIC_COUNTER_VALUE = "caloricCounterValue";
    public static final String PREF_BREAKFAST_CALORIES = "pref_breakfast_calories";
    public static final String PREF_LUNCH_CALORIES = "pref_lunch_calories";
    public static final String PREF_DINNER_CALORIES = "pref_dinner_calories";
    public static final String PREF_EXTRAS_CALORIES = "pref_extras_calories";
    public static final String FIRST_TIME_PREF = "first_time_pref";
    public static final String IS_FIRST_LAUNCH_PREF = "isFirstLaunch";
    //references to views on the layout
    TextView caloricGoalDisplay, eatenDisplay, caloriesLeftDisplay, breakfastDisplay, lunchDisplay, dinnerDisplay, extrasDisplay, waterDisplay, waterMinus, waterPlus;
    ProgressBar circularPB, carbsPB, proteinPB, fatPB;
    FloatingActionButton fabAdd, fabUpdate;
    int caloricGoalInt, carbGoalInt, proteinGoalInt, fatGoalInt, waterGoalInt, carbShare, proteinShare, fatShare;
    ListView lvFoodLogs;

    //counters
    Counter caloricCounter, breakfastCounter, lunchCounter, dinnerCounter, extrasCounter, waterCounter, carbsCounter, proteinCounter, fatCounter;

    //shared preferences
    SharedPreferences setupPref;
    SharedPreferences.Editor prefEditor;

    //foodDairy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //first launch
        Boolean isFirstLaunch = getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).getBoolean(IS_FIRST_LAUNCH_PREF, true);
        if (isFirstLaunch) {
            Intent intent = new Intent(MainActivity.this, Welcome.class);
        }
        getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).edit().putBoolean(IS_FIRST_LAUNCH_PREF, false).commit();

        //references to caloric counters display
        caloricGoalDisplay = findViewById(R.id.txtCaloricGoalValue);
        eatenDisplay = findViewById(R.id.txtCaloriesEatenValue);
        caloriesLeftDisplay = findViewById(R.id.txtCaloriesLeftValue);
        breakfastDisplay = findViewById(R.id.txtBreakfastValue);
        lunchDisplay = findViewById(R.id.txtLunchValue);
        dinnerDisplay = findViewById(R.id.txtDinnerValue);
        extrasDisplay = findViewById(R.id.txtExtrasValue);
        
        //get shared preferences
        setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);
        prefEditor = setupPref.edit();

        //reference to list view
        lvFoodLogs = findViewById(R.id.lvFoodLogs);

        //reference to progress bars
        circularPB = findViewById(R.id.circularProgressionBar);
        carbsPB = findViewById(R.id.carbPB);
        proteinPB = findViewById(R.id.proteinPB);
        fatPB = findViewById(R.id.fatPB);

        //create counters
        caloricCounter = new Counter();
        waterCounter = new Counter();
        breakfastCounter = new Counter();
        lunchCounter = new Counter();
        dinnerCounter = new Counter();
        extrasCounter = new Counter();
        carbsCounter = new Counter();
        proteinCounter = new Counter();
        fatCounter = new Counter();


        //water counter
        waterDisplay = findViewById(R.id.txtWaterCountValue);

        waterMinus = findViewById(R.id.txtWaterMinus);
        waterPlus = findViewById(R.id.txtWaterPlus);
        updateWaterCount();


        waterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.remove(1);
                updateWaterCount();
                prefEditor.putInt(PREF_WATER, waterCounter.getCount());
                prefEditor.apply();
            }
        });
        waterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.add(1);
                updateWaterCount();
                prefEditor.putInt(PREF_WATER, waterCounter.getCount());
                prefEditor.apply();
            }
        });

        //floating action button to food entry
        fabAdd = findViewById(R.id.fabBtnAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodEntryActivity.class);
                startActivity(intent);
            }
        });


    }

    private void updateCaloricCountersDisplay() {
        String caloriesLeft = Integer.toString(caloricCounter.getMaxValue() - caloricCounter.getCount());
        eatenDisplay.setText(caloricCounter.getCountString());
        caloriesLeftDisplay.setText(caloriesLeft);
        circularPB.setProgress(caloricCounter.getCount());
    }

    @Override
    protected void onStart() {
        super.onStart();
        //retrieve shared preferences
        //set caloric goal
        if (setupPref.contains(PREF_CALORIC_GOAL)) {
            caloricGoalInt = setupPref.getInt(PREF_CALORIC_GOAL, 0);
            caloricCounter.setMaxValue(caloricGoalInt);
            caloricGoalDisplay.setText(Integer.toString(caloricCounter.getMaxValue()));
            circularPB.setMax(caloricGoalInt);
            Log.d("test", "caloric goal display success");
            //set calories left
            updateCaloricCountersDisplay();
            //retrieve caloric count
        } if (setupPref.contains(CALORIC_COUNTER_VALUE)){
            caloricCounter.setCount(setupPref.getInt(CALORIC_COUNTER_VALUE, 0));
            updateCaloricCountersDisplay();
        } else {
            Log.d("test", "onCreate: no value");
        }
        //retrieve meal counters
        if (setupPref.contains(PREF_BREAKFAST_CALORIES)) {
            breakfastCounter.setCount(setupPref.getInt(PREF_BREAKFAST_CALORIES, 0));
        }  if (setupPref.contains(PREF_BREAKFAST_CALORIES)) {
            breakfastCounter.setCount(setupPref.getInt(PREF_BREAKFAST_CALORIES, 0));
        }  if (setupPref.contains(PREF_BREAKFAST_CALORIES)) {
            breakfastCounter.setCount(setupPref.getInt(PREF_BREAKFAST_CALORIES, 0));
        }  if (setupPref.contains(PREF_BREAKFAST_CALORIES)) {
            breakfastCounter.setCount(setupPref.getInt(PREF_BREAKFAST_CALORIES, 0));
        }
        updateMealDisplays();

        //set macronutrient PBs
        if (setupPref.contains(PREF_CARBS)){
            carbGoalInt = setupPref.getInt(PREF_CARBS, 0);
            carbShare = (carbGoalInt * caloricCounter.getMaxValue())/100;
            carbsCounter.setMaxValue(carbShare);
            carbsPB.setMax(carbsCounter.getMaxValue());
            Log.d("PB", Integer.toString(carbShare));
        } if (setupPref.contains(PREF_PROTEIN)){
            proteinGoalInt = setupPref.getInt(PREF_PROTEIN, 0);
            proteinShare = (proteinGoalInt * caloricCounter.getMaxValue())/100;
            proteinCounter.setMaxValue(proteinShare);
            proteinPB.setMax(proteinCounter.getMaxValue());
            Log.d("PB", Integer.toString(proteinShare));
        } if (setupPref.contains(PREF_FAT)){
            fatGoalInt = setupPref.getInt(PREF_FAT, 0);
            fatShare = (fatGoalInt * caloricCounter.getMaxValue())/100;
            fatCounter.setMaxValue(fatShare);
            fatPB.setMax(fatCounter.getMaxValue());
            Log.d("PB", Integer.toString(fatShare));
        }
        //set water count
        if (setupPref.contains(PREF_WATER)) {
            waterCounter.setCount(setupPref.getInt(PREF_WATER, 0));
            updateWaterCount();
            waterGoalInt = setupPref.getInt(PREF_WATER_GOAL, 0);
        }
    }

    private void updateMealDisplays() {
        breakfastDisplay.setText(breakfastCounter.getCountString());
        lunchDisplay.setText(lunchCounter.getCountString());
        dinnerDisplay.setText(dinnerCounter.getCountString());
        extrasDisplay.setText(extrasCounter.getCountString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //save counters
        prefEditor = setupPref.edit();
        prefEditor.putInt(PREF_WATER, waterCounter.getCount());
        prefEditor.putInt(CALORIC_COUNTER_VALUE, caloricCounter.getCount());
        prefEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update caloric status if intent contains foodLog object
        Intent intent = getIntent();
        //check if intent has foodLog extra
        if (intent.hasExtra(EXTRA_FOOD_LOG)){
            //update caloric counter
            FoodLog foodLog = (FoodLog) intent.getSerializableExtra(EXTRA_FOOD_LOG);
            caloricCounter.add(foodLog.getCalories());
            updateCaloricCountersDisplay();
            prefEditor.putInt(CALORIC_COUNTER_VALUE, caloricCounter.getCount());
            //update meals counters
            updateMealCounters(foodLog);
            prefEditor.apply();
        }
        //update food diary
        FoodLogDBH dbHelper = new FoodLogDBH(MainActivity.this);
        List<FoodLog> foodLogs = dbHelper.getFoodLog();
        ArrayAdapter<FoodLog> foodLogArrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, foodLogs);
        lvFoodLogs.setAdapter(foodLogArrayAdapter);
    }

    private void updateMealCounters(FoodLog foodLog) {
        if (foodLog.getMeal().equals("breakfast")){
            int breakfastCalories = foodLog.getCalories();
            breakfastCounter.add(breakfastCalories);
            prefEditor.putInt(PREF_BREAKFAST_CALORIES, breakfastCounter.getCount());
        } if (foodLog.getMeal().equals("lunch")){
            int lunchCalories = foodLog.getCalories();
            lunchCounter.add(lunchCalories);
            prefEditor.putInt(PREF_LUNCH_CALORIES, lunchCounter.getCount());
        } if (foodLog.getMeal().equals("dinner")){
            int dinnerCalories = foodLog.getCalories();
            dinnerCounter.add(dinnerCalories);
            prefEditor.putInt(PREF_DINNER_CALORIES, dinnerCounter.getCount());
        } if (foodLog.getMeal().equals("extras")){
            int extrasCalories = foodLog.getCalories();
            extrasCounter.add(extrasCalories);
            prefEditor.putInt(PREF_EXTRAS_CALORIES, extrasCounter.getCount());
        }
        updateMealDisplays();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void updateWaterCount() {
        waterDisplay.setText(waterCounter.getCountString());
        if (setupPref.contains(PREF_WATER_GOAL)){
            if (waterCounter.getCount() >= waterGoalInt) {
                waterDisplay.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green));
            } else {
                waterDisplay.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
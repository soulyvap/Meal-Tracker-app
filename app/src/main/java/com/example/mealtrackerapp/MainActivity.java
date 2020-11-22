package com.example.mealtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.mealtrackerapp.FoodEntryActivity.EXTRA_FOOD_LOG;
import static com.example.mealtrackerapp.SetupActivity.PREF_CALORIC_GOAL;
import static com.example.mealtrackerapp.SetupActivity.SETUP_PREF;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_WATER = "pref_water";
    //references to views on the layout
    TextView caloricGoalDisplay, eatenDisplay, caloriesLeftDisplay, breakfastDisplay, lunchDisplay, dinnerDisplay, extrasDisplay, waterDisplay, waterMinus, waterPlus;
    ProgressBar circularPG, carbsPG, proteinPG, fatPG;
    FloatingActionButton fabAdd;
    int caloricGoalInt;

    //counters
    Counter caloricCounter, breakfastCounter, lunchCounter, dinnerCounter, extrasCounter, waterCounter;

    //shared preferences
    SharedPreferences setupPref;

    //foodDairy
    FoodDairy foodDairy = new FoodDairy();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //references to caloric counters display
        caloricGoalDisplay = findViewById(R.id.txtCaloricGoalValue);
        eatenDisplay = findViewById(R.id.txtCaloriesEatenValue);
        caloriesLeftDisplay = findViewById(R.id.txtCaloriesLeftValue);
        
        //get shared preferences
        setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);

        //set caloric goal
        circularPG = findViewById(R.id.circularProgressionBar);
        if (setupPref.contains(PREF_CALORIC_GOAL)) {
            caloricGoalInt = setupPref.getInt(PREF_CALORIC_GOAL, 0);
            caloricCounter = new Counter(0, 0, caloricGoalInt);
            caloricGoalDisplay.setText(Integer.toString(caloricCounter.getMaxValue()));
            circularPG.setMax(caloricGoalInt);
            Log.d("test", "caloric goal display success");
            //set calories left
            updateCaloricCountersDisplay();
        } else {
            Log.d("test", "onCreate: no value");
        }


        //water counter
        waterCounter = new Counter(0, 0, 100);
        if (setupPref.contains(PREF_WATER)) {
            waterCounter.setCount(setupPref.getInt(PREF_WATER, 0));
        }
        waterDisplay = findViewById(R.id.txtWaterCountValue);
        waterMinus = findViewById(R.id.txtWaterMinus);
        waterPlus = findViewById(R.id.txtWaterPlus);
        updateWaterCount();

        waterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.remove(1);
                updateWaterCount();
            }
        });
        waterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterCounter.add(1);
                updateWaterCount();
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

        //update caloric status if intent contains foodLog object + add to food diary
        Intent intent = getIntent();
        //check if intent has foodLog extra
        if (intent.hasExtra(EXTRA_FOOD_LOG)){
            FoodLog foodLog = (FoodLog) intent.getSerializableExtra(EXTRA_FOOD_LOG);
            caloricCounter.add(foodLog.getCalories());
            updateCaloricCountersDisplay();
//            foodDairy.add(foodLog);
            DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);
            boolean success = dbHelper.addOne(foodLog);

            Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCaloricCountersDisplay() {
        String caloriesLeft = Integer.toString(caloricCounter.getMaxValue() - caloricCounter.getCount());
        eatenDisplay.setText(caloricCounter.getCountString());
        caloriesLeftDisplay.setText(caloriesLeft);
        circularPG.setProgress(caloricCounter.getCount());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor prefEditor = setupPref.edit();
        prefEditor.putInt(PREF_WATER, waterCounter.getCount());
        prefEditor.commit();
    }

    private void updateWaterCount() {
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
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
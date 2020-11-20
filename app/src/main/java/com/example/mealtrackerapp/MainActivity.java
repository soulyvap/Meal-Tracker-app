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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.mealtrackerapp.SetupActivity.EXTRA_CALORIC_GOAL;
import static com.example.mealtrackerapp.SetupActivity.SETUP_PREF;

public class MainActivity extends AppCompatActivity {

    //references to views on the layout
    TextView caloricGoalDisplay, eatenDisplay, caloriesLeftDisplay, breakfastDisplay, lunchDisplay, dinnerDisplay, extrasDisplay, waterDisplay, waterMinus, waterPlus;
    ProgressBar circularPG, carbsPG, proteinPG, fatPG;
    FloatingActionButton fabAdd;
    int caloricGoalInt;

    //counters
    Counter caloricCounter, breakfastCounter, lunchCounter, dinnerCounter, extrasCounter, waterCounter;

    //shared preferences
    SharedPreferences setupPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //get shared preferences
        setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);

        //set caloric goal
        caloricGoalDisplay = findViewById(R.id.txtCaloricGoalValue);
        circularPG = findViewById(R.id.circularProgressionBar);
        if (setupPref.contains(EXTRA_CALORIC_GOAL)) {
            caloricGoalInt = setupPref.getInt(EXTRA_CALORIC_GOAL, 0);
            caloricCounter = new Counter(0, 0, caloricGoalInt);
            caloricGoalDisplay.setText(Integer.toString(caloricCounter.getMaxValue()));
            circularPG.setMax(caloricGoalInt);
            Log.d("test", "caloric goal display success");
        } else {
            Log.d("test", "onCreate: no value");
        }

        //set calories left
        caloriesLeftDisplay = findViewById(R.id.txtCaloriesLeftValue);
        int caloriesLeftInt = caloricCounter.getMaxValue() - caloricCounter.getCount();
        caloriesLeftDisplay.setText(Integer.toString(caloriesLeftInt));

        //water counter
        waterCounter = new Counter(0, 0, 100);

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
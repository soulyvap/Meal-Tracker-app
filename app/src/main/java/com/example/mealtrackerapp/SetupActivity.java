package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.text.TextUtils.isEmpty;

public class SetupActivity extends AppCompatActivity {
    public static final String EXTRA_CALORIC_GOAL = "extra_caloricGoal";
    public static final String EXTRA_CARBS = "extra_carbs";
    public static final String EXTRA_PROTEIN = "extra_protein";
    public static final String EXTRA_FAT = "extra_fat";
    public static final String EXTRA_WATER = "extra_water";
    public static final String SETUP_PREF = "setupPref";
    EditText firstName, LastName, height, weight, caloricGoal, carbsPercent, proteinPercent,
            fatPercent, waterGoal;
    Button btnCalendar, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btnSave = findViewById(R.id.btnSaveSetup);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetupActivity.this, MainActivity.class);

                SharedPreferences setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = setupPref.edit();

                caloricGoal = findViewById(R.id.etxtSetCaloricValue);
                carbsPercent = findViewById(R.id.etxtSetCarbs);
                proteinPercent = findViewById(R.id.etxtSetProtein);
                fatPercent = findViewById(R.id.etxtSetFat);
                waterGoal = findViewById(R.id.etxtSetWater);

                try {
                    prefEditor.putInt(EXTRA_CALORIC_GOAL, Integer.parseInt(caloricGoal.getText().toString().trim()));
//                prefEditor.putInt(EXTRA_CARBS, Integer.parseInt(carbsPercent.getText().toString()));
//                prefEditor.putInt(EXTRA_PROTEIN, Integer.parseInt(proteinPercent.getText().toString()));
//                prefEditor.putInt(EXTRA_FAT, Integer.parseInt(fatPercent.getText().toString()));
//                prefEditor.putInt(EXTRA_WATER, Integer.parseInt(waterGoal.getText().toString()));
                    prefEditor.commit();
                    Log.d("test", "adding sharedPref successful");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SetupActivity.this, "Please fill in compulsory fields :)", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREF_CALORIC_GOAL = "extra_caloricGoal";
    public static final String PREF_CARBS = "extra_carbs";
    public static final String PREF_PROTEIN = "extra_protein";
    public static final String PREF_FAT = "extra_fat";
    public static final String PREF_WATER = "extra_water";
    public static final String SETUP_PREF = "setupPref";
    EditText firstName, LastName, height, weight, caloricGoal, carbsPercent, proteinPercent,
            fatPercent, waterGoal;
    Button btnCalendar, btnSave;
    ImageView btnBirthdateCalendar;

    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView txtBirthdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btnBirthdateCalendar = findViewById(R.id.btnBirthdateCalendar);
        txtBirthdate = findViewById(R.id.txtBirthdate);
        btnBirthdateCalendar.setOnClickListener(this);

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
                    prefEditor.putInt(PREF_CALORIC_GOAL, Integer.parseInt(caloricGoal.getText().toString().trim()));
                    prefEditor.putInt(PREF_CARBS, Integer.parseInt(carbsPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_PROTEIN, Integer.parseInt(proteinPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_FAT, Integer.parseInt(fatPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_WATER, Integer.parseInt(waterGoal.getText().toString().trim()));
                    prefEditor.commit();
                    Log.d("test", "adding sharedPref successful");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SetupActivity.this, "Please fill in mandatory fields :)", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBirthdateCalendar:
                ShowDateDialog();
                break;
        }
    }

    private void ShowDateDialog() {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        txtBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }
}
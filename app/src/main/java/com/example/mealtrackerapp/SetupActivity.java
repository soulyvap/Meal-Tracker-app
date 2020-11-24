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

import java.util.Calendar;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREF_CALORIC_GOAL = "extra_caloricGoal";
    public static final String PREF_CARBS = "extra_carbs";
    public static final String PREF_PROTEIN = "extra_protein";
    public static final String PREF_FAT = "extra_fat";
    public static final String PREF_WATER = "extra_water";
    public static final String SETUP_PREF = "setupPref";
    EditText firstName, LastName, height, weight, caloricGoal, carbsPercent, proteinPercent,
            fatPercent, waterGoal,etxtSetFirstname,etxtSetLastname,txtBirthdate,etxtSetHeight,etxtSetWightValue,etxtSetCaloricValue,etxtSetCarbs,etxtSetProtein,etxtSetFat,etxtSetWater;
    Button btnCalendar, btnSave;
    ImageView btnBirthdateCalendar;

    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView textBirthdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        btnBirthdateCalendar = findViewById(R.id.btnBirthdateCalendar);
        btnBirthdateCalendar.setOnClickListener(this);
        etxtSetFirstname = findViewById(R.id.etxtSetFirstname);
        etxtSetLastname = findViewById(R.id.etxtSetLastname);
        etxtSetHeight = findViewById(R.id.etxtSetHeight);
        etxtSetWightValue = findViewById(R.id.etxtSetWightValue);
        etxtSetCaloricValue = findViewById(R.id.etxtSetCaloricValue);
        etxtSetCarbs = findViewById(R.id.etxtSetCarbs);
        etxtSetProtein = findViewById(R.id.etxtSetProtein);
        etxtSetFat = findViewById(R.id.etxtSetFat);
        etxtSetWater= findViewById(R.id.etxtSetWater);




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



                if (editTextIsEmpty(etxtSetFirstname)) {
                    etxtSetFirstname.setError("Please enter first name");
                } if (editTextIsEmpty(etxtSetLastname)) {
                    etxtSetLastname.setError("Please enter last name");
                } if (editTextIsEmpty(txtBirthdate)) {
                    txtBirthdate.setError("Please enter Birth date");
                } if (editTextIsEmpty(etxtSetHeight)) {
                    etxtSetHeight.setError("Please enter Height");
                } if (editTextIsEmpty(etxtSetWightValue)) {
                    etxtSetWightValue.setError("Please enter weight");
                } if (editTextIsEmpty(etxtSetCaloricValue)) {
                    etxtSetCaloricValue.setError("Please enter Daily caloric goal");
                } if (editTextIsEmpty(etxtSetCarbs)) {
                    etxtSetCarbs.setError("Please enter Carbs amount");
                } if (editTextIsEmpty(etxtSetProtein)) {
                    etxtSetProtein.setError("Please enter Protein amount");
                } if (editTextIsEmpty(etxtSetFat)) {
                    etxtSetFat.setError("Please enter Fat amount");
                } if (editTextIsEmpty(etxtSetWater)) {
                    etxtSetWater.setError("Please enter water glass");
                } else{
                    prefEditor.putInt(PREF_CALORIC_GOAL, Integer.parseInt(caloricGoal.getText().toString().trim()));
                    prefEditor.putInt(PREF_CARBS, Integer.parseInt(carbsPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_PROTEIN, Integer.parseInt(proteinPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_FAT, Integer.parseInt(fatPercent.getText().toString().trim()));
                    prefEditor.putInt(PREF_WATER, Integer.parseInt(waterGoal.getText().toString().trim()));
                    prefEditor.commit();
                    Log.d("test", "adding sharedPref successful");
                    startActivity(intent);

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
        public boolean editTextIsEmpty(EditText editText) {
            return editText.getText().toString().trim().length() == 0;
        }
}



package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    public static final String PREF_CALORIC_GOAL = "pref_caloricGoal";
    public static final String PREF_CARBS = "pref_carbs";
    public static final String PREF_PROTEIN = "pref_protein";
    public static final String PREF_FAT = "pref_fat";
    public static final String PREF_WATER = "pref_water";
    public static final String SETUP_PREF = "sharedpref_setupPref";
    public static final String PREF_FIRSTNAME = "pref_firstname";
    public static final String PREF_LASTNAME ="pref_lastname";
    public static final String PREF_BIRTHDATE = "pref_birthdate";
    public static final String PREF_HEIGHT ="pref_height";
    public static final String PREF_WEIGHT = "pref_weight";
    EditText etxtSetFirstname, etxtSetLastname, etxtSetHeight, etxtSetWeightValue, etxtSetCaloricValue,
            etxtSetCarbs, etxtSetProtein, etxtSetFat, etxtSetWater;
    Button btnSave;
    ImageView btnBirthdateCalendar;
    TextView txtSetBirthdate;
    String firstname, lastname, birthdate = " ", today;
    int height, weight, caloricGoal, carbsGoal, proteinGoal, fatGoal, waterGoal;

    private int mYear, mMonth, mDay;

    SharedPreferences setupPref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //reference to layout elements
        btnBirthdateCalendar = findViewById(R.id.btnBirthdateCalendar);
        btnBirthdateCalendar.setOnClickListener(this);
        etxtSetFirstname = findViewById(R.id.etxtSetFirstname);
        etxtSetLastname = findViewById(R.id.etxtSetLastname);
        etxtSetHeight = findViewById(R.id.etxtSetHeight);
        etxtSetWeightValue = findViewById(R.id.etxtSetWightValue);
        etxtSetCaloricValue = findViewById(R.id.etxtSetCaloricValue);
        etxtSetCarbs = findViewById(R.id.etxtSetCarbs);
        etxtSetProtein = findViewById(R.id.etxtSetProtein);
        etxtSetFat = findViewById(R.id.etxtSetFat);
        etxtSetWater= findViewById(R.id.etxtSetWater);
        txtSetBirthdate = findViewById(R.id.txtBirthdate);

        //create shared preferences
        setupPref = getSharedPreferences(SETUP_PREF, Activity.MODE_PRIVATE);
        prefEditor = setupPref.edit();


        btnSave = findViewById(R.id.btnSaveSetup);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SetupActivity.this, MainActivity.class);


                if (birthdate.equals(" ")) {
                    Toast.makeText(SetupActivity.this, "Please enter birth date", Toast.LENGTH_SHORT).show();
                    txtSetBirthdate.setTextColor(ContextCompat.getColor(SetupActivity.this, R.color.red));
                } if (editTextIsEmpty(etxtSetFirstname)) {
                    etxtSetFirstname.setError("Please enter first name");
                } if (editTextIsEmpty(etxtSetLastname)) {
                    etxtSetLastname.setError("Please enter last name");
                } if (editTextIsEmpty(etxtSetHeight)) {
                    etxtSetHeight.setError("Please enter height");
                } if (editTextIsEmpty(etxtSetWeightValue)) {
                    etxtSetWeightValue.setError("Please enter weight");
                } if (editTextIsEmpty(etxtSetCaloricValue)) {
                    etxtSetCaloricValue.setError("Please enter daily caloric goal");
                } if (editTextIsEmpty(etxtSetCarbs)) {
                    etxtSetCarbs.setError("Please enter carbs amount");
                } if (editTextIsEmpty(etxtSetProtein)) {
                    etxtSetProtein.setError("Please enter protein amount");
                } if (editTextIsEmpty(etxtSetFat)) {
                    etxtSetFat.setError("Please enter fat amount");
                } if (editTextIsEmpty(etxtSetWater)) {
                    etxtSetWater.setError("Please enter water glass");
                } else if (!editTextIsEmpty(etxtSetCarbs) &&
                        !editTextIsEmpty(etxtSetCarbs) &&
                        !editTextIsEmpty(etxtSetCarbs)) {
                    int carbs = Integer.parseInt(etxtSetCarbs.getText().toString());
                    int protein = Integer.parseInt(etxtSetProtein.getText().toString());
                    int fat = Integer.parseInt(etxtSetFat.getText().toString());
                    if (carbs + protein + fat != 100) {
                        Toast.makeText(SetupActivity.this, "sum of macro should be 100", Toast.LENGTH_SHORT).show();
                    } else {
                        prefEditor.putString(PREF_FIRSTNAME, etxtSetFirstname.getText().toString());
                        prefEditor.putString(PREF_LASTNAME, etxtSetLastname.getText().toString());
                        prefEditor.putString(PREF_BIRTHDATE, birthdate);
                        prefEditor.putInt(PREF_HEIGHT, Integer.parseInt(etxtSetHeight.getText().toString().trim()));
                        prefEditor.putInt(PREF_WEIGHT, Integer.parseInt(etxtSetWeightValue.getText().toString().trim()));
                        prefEditor.putInt(PREF_CALORIC_GOAL, Integer.parseInt(etxtSetCaloricValue.getText().toString().trim()));
                        prefEditor.putInt(PREF_CARBS, Integer.parseInt(etxtSetCarbs.getText().toString().trim()));
                        prefEditor.putInt(PREF_PROTEIN, Integer.parseInt(etxtSetProtein.getText().toString().trim()));
                        prefEditor.putInt(PREF_FAT, Integer.parseInt(etxtSetFat.getText().toString().trim()));
                        prefEditor.putInt(PREF_WATER, Integer.parseInt(etxtSetWater.getText().toString().trim()));
                        prefEditor.commit();
                        Log.d("test", "adding sharedPref successful");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //retrieve shared preferences
        if (setupPref.contains(PREF_CALORIC_GOAL)) {
            firstname = setupPref.getString(PREF_FIRSTNAME, "");
            lastname = setupPref.getString(PREF_LASTNAME, "");
            birthdate = setupPref.getString(PREF_BIRTHDATE, "");
            height = setupPref.getInt(PREF_HEIGHT, 0);
            weight = setupPref.getInt(PREF_WEIGHT, 0);
            caloricGoal = setupPref.getInt(PREF_CALORIC_GOAL, 0);
            carbsGoal = setupPref.getInt(PREF_CARBS, 0);
            proteinGoal = setupPref.getInt(PREF_PROTEIN, 0);
            fatGoal = setupPref.getInt(PREF_FAT, 0);
            waterGoal = setupPref.getInt(PREF_WATER, 0);

            etxtSetFirstname.setText(firstname);
            etxtSetLastname.setText(lastname);
            txtSetBirthdate.setText(birthdate);
            etxtSetHeight.setText(Integer.toString(height));
            etxtSetWeightValue.setText(Integer.toString(weight));
            etxtSetCaloricValue.setText(Integer.toString(caloricGoal));
            etxtSetCarbs.setText(Integer.toString(carbsGoal));
            etxtSetProtein.setText(Integer.toString(proteinGoal));
            etxtSetFat.setText(Integer.toString(fatGoal));
            etxtSetWater.setText(Integer.toString(waterGoal));
        }
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

        today = mDay + "-" + mMonth + "-" + mYear;


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        birthdate = dayOfMonth + "-" + monthOfYear + "-" + year;
                        txtSetBirthdate.setText(birthdate);
                        txtSetBirthdate.setTextColor(ContextCompat.getColor(SetupActivity.this, R.color.black));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }
        public boolean editTextIsEmpty(EditText editText) {
            return editText.getText().toString().trim().length() == 0;
        }
}



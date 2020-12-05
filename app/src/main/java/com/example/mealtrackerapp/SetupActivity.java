package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.mealtrackerapp.DayDataDBH.COLUMN_CALORIC_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_CARBS_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_FAT_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_PROTEIN_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WATER_GOAL;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WATER_INTAKE;
import static com.example.mealtrackerapp.DayDataDBH.COLUMN_WEIGHT;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_DATE;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_DAY;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_MONTH;
import static com.example.mealtrackerapp.MainActivity.EXTRA_DISPLAYED_YEAR;
import static com.example.mealtrackerapp.MainActivity.FIRST_TIME_PREF;
import static com.example.mealtrackerapp.MainActivity.IS_FIRST_LAUNCH_PREF;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PREF_CALORIC_GOAL = "pref_caloricGoal";
    public static final String PREF_CARBS = "pref_carbs";
    public static final String PREF_PROTEIN = "pref_protein";
    public static final String PREF_FAT = "pref_fat";
    public static final String PREF_WATER_GOAL = "pref_watergoal";
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
    String firstname, lastname, birthdate = " ", today, dateDisplayed;
    int height, weight, caloricGoal, carbsGoal, proteinGoal, fatGoal, waterGoal, waterIntake;
    Calendar c;
    TextView txtDate;

    private int yearToday, monthToday, dayToday, year, month, day;

    DayDataDBH dayDataDHB;

    SharedPreferences setupPref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //get current date and display it
        c = Calendar.getInstance();
        yearToday = c.get(Calendar.YEAR);
        monthToday = c.get(Calendar.MONTH);
        dayToday = c.get(Calendar.DAY_OF_MONTH);

        day = dayToday;
        month = monthToday;
        year = yearToday;

        today = dayToday + "-" + (monthToday + 1) + "-" + yearToday;
        dateDisplayed = today;

        //reference to layout elements
        btnBirthdateCalendar = findViewById(R.id.btnBirthdateCalendar);
        btnBirthdateCalendar.setOnClickListener(this);
        etxtSetFirstname = findViewById(R.id.etxtSetFirstname);
        etxtSetLastname = findViewById(R.id.etxtSetLastname);
        etxtSetHeight = findViewById(R.id.etxtSetHeight);
        etxtSetWeightValue = findViewById(R.id.etxtSetWeightValue);
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
                    return;
                } if (editTextIsEmpty(etxtSetFirstname)) {
                    etxtSetFirstname.setError("Please enter first name");
                    return;
                } if (editTextIsEmpty(etxtSetLastname)) {
                    etxtSetLastname.setError("Please enter last name");
                    return;
                } if (editTextIsEmpty(etxtSetHeight)) {
                    etxtSetHeight.setError("Please enter height");
                    return;
                } if (editTextIsEmpty(etxtSetWeightValue) || editTextIsZero(etxtSetWeightValue)) {
                    etxtSetWeightValue.setError("Please enter weight");
                    return;
                } if (editTextIsEmpty(etxtSetCaloricValue) || editTextIsZero(etxtSetCaloricValue)) {
                    etxtSetCaloricValue.setError("Please enter daily caloric goal");
                    return;
                } if (editTextIsEmpty(etxtSetCarbs)) {
                    etxtSetCarbs.setError("Please enter carbs amount");
                    return;
                } if (editTextIsEmpty(etxtSetProtein)) {
                    etxtSetProtein.setError("Please enter protein amount");
                    return;
                } if (editTextIsEmpty(etxtSetFat)) {
                    etxtSetFat.setError("Please enter fat amount");
                    return;
                } if (editTextIsEmpty(etxtSetWater)) {
                    etxtSetWater.setError("Please enter number of water glasses");
                    return;
                } else if (!editTextIsEmpty(etxtSetCarbs) &&
                        !editTextIsEmpty(etxtSetCarbs) &&
                        !editTextIsEmpty(etxtSetCarbs)) {
                    int carbs = Integer.parseInt(etxtSetCarbs.getText().toString());
                    int protein = Integer.parseInt(etxtSetProtein.getText().toString());
                    int fat = Integer.parseInt(etxtSetFat.getText().toString());
                    if (carbs + protein + fat != 100) {
                        Toast.makeText(SetupActivity.this, "Sum of macro should be 100", Toast.LENGTH_SHORT).show();
                    } else {
                        weight = editTextToInt(etxtSetWeightValue);
                        caloricGoal = editTextToInt(etxtSetCaloricValue);
                        carbsGoal = editTextToInt(etxtSetCarbs);
                        proteinGoal = editTextToInt(etxtSetProtein);
                        fatGoal = editTextToInt(etxtSetFat);
                        waterGoal = editTextToInt(etxtSetWater);

                        prefEditor.putString(PREF_FIRSTNAME, etxtSetFirstname.getText().toString());
                        prefEditor.putString(PREF_LASTNAME, etxtSetLastname.getText().toString());
                        prefEditor.putString(PREF_BIRTHDATE, birthdate);
                        prefEditor.putInt(PREF_HEIGHT, editTextToInt(etxtSetHeight));
                        prefEditor.putInt(PREF_WEIGHT, weight);
                        prefEditor.putInt(PREF_CALORIC_GOAL, caloricGoal);
                        prefEditor.putInt(PREF_CARBS, carbsGoal);
                        prefEditor.putInt(PREF_PROTEIN, proteinGoal);
                        prefEditor.putInt(PREF_FAT, fatGoal);
                        prefEditor.putInt(PREF_WATER_GOAL, waterGoal);
                        prefEditor.apply();

                        Boolean isFirstLaunch = getSharedPreferences(FIRST_TIME_PREF, MODE_PRIVATE).getBoolean(IS_FIRST_LAUNCH_PREF, true);
                        if (!isFirstLaunch) {
                            intent.putExtra(EXTRA_DISPLAYED_DATE, dateDisplayed);
                            intent.putExtra(EXTRA_DISPLAYED_DAY, day);
                            intent.putExtra(EXTRA_DISPLAYED_MONTH, month);
                            intent.putExtra(EXTRA_DISPLAYED_YEAR, year);
                        }


                        dayDataDHB.addOneReplace(dateDisplayed, weight, caloricGoal, carbsGoal, proteinGoal, fatGoal, waterGoal, waterIntake);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //call daydata.db helper
        dayDataDHB = new DayDataDBH(SetupActivity.this);

        //get date displayed on main activity (date of today if first launch of the app) and display it
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_DISPLAYED_DATE)) {
            dateDisplayed = intent.getStringExtra(EXTRA_DISPLAYED_DATE);
            day = intent.getIntExtra(EXTRA_DISPLAYED_DAY, dayToday);
            month = intent.getIntExtra(EXTRA_DISPLAYED_MONTH, monthToday);
            year = intent.getIntExtra(EXTRA_DISPLAYED_YEAR, yearToday);
        }
        txtDate = findViewById(R.id.txtDateForSetup);
        txtDate.setText(dateDisplayed);

        //retrieve shared preferences
        if (setupPref.contains(PREF_FIRSTNAME)) {
            firstname = setupPref.getString(PREF_FIRSTNAME, "");
            lastname = setupPref.getString(PREF_LASTNAME, "");
            birthdate = setupPref.getString(PREF_BIRTHDATE, "");
            height = setupPref.getInt(PREF_HEIGHT, 0);

            etxtSetFirstname.setText(firstname);
            etxtSetLastname.setText(lastname);
            txtSetBirthdate.setText(birthdate);
            etxtSetHeight.setText(String.valueOf(height));
        }

        weight = dayDataDHB.getIntByDate(COLUMN_WEIGHT, dateDisplayed);
        caloricGoal = dayDataDHB.getIntByDate(COLUMN_CALORIC_GOAL, dateDisplayed);
        carbsGoal = dayDataDHB.getIntByDate(COLUMN_CARBS_GOAL, dateDisplayed);
        proteinGoal = dayDataDHB.getIntByDate(COLUMN_PROTEIN_GOAL, dateDisplayed);
        fatGoal = dayDataDHB.getIntByDate(COLUMN_FAT_GOAL, dateDisplayed);
        waterGoal = dayDataDHB.getIntByDate(COLUMN_WATER_GOAL, dateDisplayed);
        waterIntake = dayDataDHB.getIntByDate(COLUMN_WATER_INTAKE, dateDisplayed);

        if (caloricGoal == 0) {

        } else {
            etxtSetWeightValue.setText(String.valueOf(weight));
            etxtSetCaloricValue.setText(String.valueOf(caloricGoal));
            etxtSetCarbs.setText(String.valueOf(carbsGoal));
            etxtSetProtein.setText(String.valueOf(proteinGoal));
            etxtSetFat.setText(String.valueOf(fatGoal));
            etxtSetWater.setText(String.valueOf(waterGoal));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBirthdateCalendar:
                showDateDialog();
                break;
        }
    }

    private void showDateDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        birthdate = dayOfMonth + "-" + (monthOfYear+1) + "-" + year;
                        txtSetBirthdate.setText(birthdate);
                        txtSetBirthdate.setTextColor(ContextCompat.getColor(SetupActivity.this, R.color.black));
                        yearToday = year;
                        monthToday = monthOfYear;
                        dayToday = dayOfMonth;
                    }
                }, yearToday, monthToday, dayToday);
        datePickerDialog.show();


    }
    public boolean editTextIsEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
    public boolean editTextIsZero(EditText editText) {
        return editText.getText().toString().trim().equals("0");
    }
    private int editTextToInt (EditText editText) {
        return Integer.parseInt(editText.getText().toString().trim());
    }

}



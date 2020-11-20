package com.example.mealtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;

public class FoodEntryActivity extends AppCompatActivity {

    EditText timeDisplay;
    ImageView btnClock;
    int hour, minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry);

        timeDisplay = findViewById(R.id.etxtTime);

        Calendar calendar = Calendar.getInstance();

        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        timeDisplay.setText(getTimeString(hour, minutes));


        btnClock = findViewById(R.id.btnClock);
        btnClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(FoodEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeDisplay.setText(getTimeString(hourOfDay, minute));
                    }
                }, hour, minutes, true);
                timePickerDialog.show();
            }
        });
    }
    public String getTimeString(int hour, int minutes) {
        if (minutes < 10){
             String timeString = hour + ":" + 0 + minutes;
             return timeString;
        } else {
            return hour + ":" + minutes;
        }
    }

}
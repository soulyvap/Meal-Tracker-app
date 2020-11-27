package com.example.mealtrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DayDataDHB extends SQLiteOpenHelper {
    public static final String DAYDATA_TABLE = "DAYDATA_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_WEIGHT = "WEIGHT";
    public static final String COLUMN_CALORIC_GOAL = "CALORIC_GOAL";
    public static final String COLUMN_CARBS_GOAL = "CARBS_GOAL";
    public static final String COLUMN_PROTEIN_GOAL = "PROTEIN_GOAL";
    public static final String COLUMN_FAT_GOAL = "FAT_GOAL";
    public static final String COLUMN_WATER_GOAL = "WATER_GOAL";
    public static final String COLUMN_CALORIC_INTAKE = "CALORIC_INTAKE";
    public static final String COLUMN_CARBS_INTAKE = "CARBS_INTAKE";
    public static final String COLUMN_PROTEIN_INTAKE = "PROTEIN_INTAKE";
    public static final String COLUMN_FAT_INTAKE = "FAT_INTAKE";
    public static final String COLUMN_BREAKFAST_INTAKE = "BREAKFAST_INTAKE";
    public static final String COLUMN_LUNCH_INTAKE = "LUNCH_INTAKE";
    public static final String COLUMN_DINNER_INTAKE = "DINNER_INTAKE";
    public static final String COLUMN_EXTRAS_INTAKE = "EXTRAS_INTAKE";
    public static final String COLUMN_WATER_INTAKE = "WATER_INTAKE";

    public DayDataDHB(@Nullable Context context) {
        super(context, "daydata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + DAYDATA_TABLE + " (" + COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_WEIGHT + " INTEGER, " + COLUMN_CALORIC_GOAL + " INTEGER, " + COLUMN_CARBS_GOAL + " INTEGER, " +
                COLUMN_PROTEIN_GOAL + " INTEGER, " + COLUMN_FAT_GOAL + " INTEGER, " + COLUMN_WATER_GOAL + " INTEGER, " +
                COLUMN_CALORIC_INTAKE + " INTEGER, " + COLUMN_CARBS_INTAKE + " INTEGER, " + COLUMN_PROTEIN_INTAKE + " INTEGER, " +
                COLUMN_FAT_INTAKE + " INTEGER, " + COLUMN_BREAKFAST_INTAKE + " INTEGER, " + COLUMN_LUNCH_INTAKE + " INTEGER, " +
                COLUMN_DINNER_INTAKE + " INTEGER, " + COLUMN_EXTRAS_INTAKE + " INTEGER, " + COLUMN_WATER_INTAKE + " INTEGER)";

        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(DayData dayData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, dayData.getDate());
        cv.put(COLUMN_WEIGHT, dayData.getWeight());
        cv.put(COLUMN_CALORIC_GOAL, dayData.getCaloricGoal());
        cv.put(COLUMN_CARBS_GOAL, dayData.getCarbsGoal());
        cv.put(COLUMN_PROTEIN_GOAL, dayData.getProteinGoal());
        cv.put(COLUMN_FAT_GOAL, dayData.getFatGoal());
        cv.put(COLUMN_WATER_GOAL, dayData.getWaterGoal());
        cv.put(COLUMN_CALORIC_INTAKE, dayData.getCaloricIntake());
        cv.put(COLUMN_CARBS_INTAKE, dayData.getCarbsIntake());
        cv.put(COLUMN_PROTEIN_INTAKE, dayData.getProteinIntake());
        cv.put(COLUMN_FAT_INTAKE, dayData.getFatIntake());
        cv.put(COLUMN_BREAKFAST_INTAKE, dayData.getBreakfastIntake());
        cv.put(COLUMN_LUNCH_INTAKE, dayData.getLunchIntake());
        cv.put(COLUMN_DINNER_INTAKE, dayData.getDinnerIntake());
        cv.put(COLUMN_EXTRAS_INTAKE, dayData.getExtrasIntake());
        cv.put(COLUMN_WATER_INTAKE, dayData.getWaterIntake());

        long insert = db.insert(DAYDATA_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public DayData getDayData(String dateDisplayed) {
        DayData dayData;
        //get data from db
        String query = "SELECT * FROM " + DAYDATA_TABLE  + " WHERE " + COLUMN_DATE + " LIKE \'" + dateDisplayed + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //loop through cursor (result set) and create new foodLog object. then add them to the return array
                String date = cursor.getString(0);
                int weight = cursor.getInt(1);
                int caloricGoal = cursor.getInt(2);
                int carbGoal = cursor.getInt(3);
                int proteinGoal = cursor.getInt(4);
                int fatGoal = cursor.getInt(5);
                int waterGoal = cursor.getInt(6);
                int caloricIntake = cursor.getInt(7);
                int carbsIntake = cursor.getInt(8);
                int proteinIntake = cursor.getInt(9);
                int fatIntake = cursor.getInt(10);
                int breakfastIntake = cursor.getInt(11);
                int lunchIntake = cursor.getInt(12);
                int dinnerIntake = cursor.getInt(13);
                int extrasIntake = cursor.getInt(14);
                int waterIntake = cursor.getInt(15);

                dayData = new DayData(date, weight, caloricGoal, carbGoal, proteinGoal,
                        fatGoal, waterGoal, caloricIntake, carbsIntake, proteinIntake, fatIntake,
                        breakfastIntake, lunchIntake, dinnerIntake, extrasIntake, waterIntake);
        } else {
            dayData = new DayData(dateDisplayed);
        }

        cursor.close();
        db.close();

        return dayData;
    }
}

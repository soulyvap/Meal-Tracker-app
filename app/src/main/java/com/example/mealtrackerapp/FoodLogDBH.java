package com.example.mealtrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodLogDBH extends SQLiteOpenHelper {
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_FOOD = "FOOD";
    public static final String FOODLOGS_TABLE = "FOODLOGS_TABLE";
    public static final String COLUMN_MEAL = "MEAL";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_CARBS = "CARBS";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_FAT = "FAT";

    public FoodLogDBH(@Nullable Context context) {
        super(context, "foodLogs.db", null, 1);
    }

    //called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + FOODLOGS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " + COLUMN_FOOD + " TEXT, " + COLUMN_MEAL + " TEXT, " +
                COLUMN_TIME + " TEXT, " + COLUMN_CALORIES + " INTEGER, " + COLUMN_CARBS + " REAL, " +
                COLUMN_PROTEIN + " REAL, " + COLUMN_FAT + " REAL)";

        db.execSQL(createTableString);
    }

    //called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(FoodLog foodLog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, foodLog.getDate());
        cv.put(COLUMN_FOOD, foodLog.getName());
        cv.put(COLUMN_MEAL, foodLog.getMeal());
        cv.put(COLUMN_TIME, foodLog.getTime());
        cv.put(COLUMN_CALORIES, foodLog.getCalories());
        cv.put(COLUMN_CARBS, foodLog.getCarbs());
        cv.put(COLUMN_PROTEIN, foodLog.getProtein());
        cv.put(COLUMN_FAT, foodLog.getFat());

        long insert = db.insert(FOODLOGS_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void updateOne(FoodLog foodLog, String name, String meal, String time, int calories, double carbs, double protein, double fat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FOOD, name);
        cv.put(COLUMN_MEAL, meal);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_CALORIES, calories);
        cv.put(COLUMN_CARBS, carbs);
        cv.put(COLUMN_PROTEIN, protein);
        cv.put(COLUMN_FAT, fat);

        db.update(FOODLOGS_TABLE, cv, COLUMN_ID + " = " + foodLog.getId(), null);
    }

    public void deleteOne(FoodLog foodlog) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(FOODLOGS_TABLE, COLUMN_ID + " = " + foodlog.getId(), null);
    }

    public void addArray(ArrayList<FoodLog> foodlogList) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (FoodLog foodLog: foodlogList) {
            cv.put(COLUMN_DATE, foodLog.getDate());
            cv.put(COLUMN_FOOD, foodLog.getName());
            cv.put(COLUMN_MEAL, foodLog.getMeal());
            cv.put(COLUMN_TIME, foodLog.getTime());
            cv.put(COLUMN_CALORIES, foodLog.getCalories());
            cv.put(COLUMN_CARBS, foodLog.getCarbs());
            cv.put(COLUMN_PROTEIN, foodLog.getProtein());
            cv.put(COLUMN_FAT, foodLog.getFat());
            db.insert(FOODLOGS_TABLE, null, cv);
        }
    }

    public List<FoodLog> getFoodLog(String dateDisplayed) {
        List<FoodLog> foodLogList = new ArrayList<>();

        //get data from db
        String query = "SELECT * FROM " + FOODLOGS_TABLE  + " WHERE " + COLUMN_DATE + " LIKE \'" + dateDisplayed + "\' ORDER BY " + COLUMN_TIME + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //loop through cursor (result set) and create new foodLog object. then add them to the return array
            do {
                int id = cursor.getInt(0);
                String date = cursor.getString(1);
                String food = cursor.getString(2);
                String meal = cursor.getString(3);
                String time = cursor.getString(4);
                int calories = cursor.getInt(5);
                double carbs = cursor.getInt(6);
                double protein = cursor.getInt(7);
                double fat = cursor.getInt(8);

                FoodLog foodLog = new FoodLog(id, date, food, meal, time, calories, carbs, protein, fat);
                foodLogList.add(foodLog);

            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();

        return foodLogList;
    }

    public int getCaloriesByDate(String date) {
        int sumOfCalories;

        String query = "SELECT SUM(" + COLUMN_CALORIES + ") FROM " + FOODLOGS_TABLE + " WHERE " + COLUMN_DATE + " LIKE \'" + date + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            sumOfCalories = cursor.getInt(0);
        } else {
            sumOfCalories = 0;
        }
        cursor.close();

        return sumOfCalories;
    }

    public int getCaloriesByMealByDate(String meal, String date) {
        int sumOfCalories;

        String query = "SELECT SUM(" + COLUMN_CALORIES + ") FROM " + FOODLOGS_TABLE + " WHERE " + COLUMN_MEAL + " LIKE \'" + meal + "\' AND " + COLUMN_DATE + " LIKE \'" + date + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            sumOfCalories = cursor.getInt(0);
        } else {
            sumOfCalories = 0;
        }
        cursor.close();

        return sumOfCalories;
    }

    public int getSumByDate(String column, String date) {
        int returnSum;

        String query = "SELECT SUM(" + column + ") FROM " + FOODLOGS_TABLE + " WHERE " + COLUMN_DATE + " LIKE \'" + date + "\'" ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            returnSum = cursor.getInt(0);
        } else {
            returnSum = 0;
        }
        cursor.close();
        db.close();

        return returnSum;
    }
}

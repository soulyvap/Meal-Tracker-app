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
    public static final String COLUMN_FOOD = "FOOD";
    public static final String FOODLOGS_TABLE = COLUMN_FOOD + "LOGS_TABLE";
    public static final String COLUMN_MEAL = "MEAL";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_CALORIES = "CALORIES";
    public static final String COLUMN_CARBS = "CARBS";
    public static final String COLUMN_PROTEIN = "PROTEIN";
    public static final String COLUMN_FAT = "FAT";

    public FoodLogDBH(@Nullable Context context) {
        super(context, "test1.db", null, 1);
    }

    //called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + FOODLOGS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FOOD + " TEXT, " + COLUMN_MEAL + " TEXT, " + COLUMN_TIME + " TEXT, " + COLUMN_CALORIES + " INTEGER, " + COLUMN_CARBS + " INTEGER, " + COLUMN_PROTEIN + " INTEGER, " + COLUMN_FAT + " INTEGER)";

        db.execSQL(createTableString);
    }

    //called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(FoodLog foodLog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

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
    public List<FoodLog> getFoodLog() {
        List<FoodLog> foodLogList = new ArrayList<>();

        //get data from db
        String query = "SELECT * FROM " + FOODLOGS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //loop through cursor (result set) and create new foodLog object. then add them to the return array
            do {
                String food = cursor.getString(1);
                String meal = cursor.getString(2);
                String time = cursor.getString(3);
                int calories = cursor.getInt(4);
                int carbs = cursor.getInt(5);
                int protein = cursor.getInt(6);
                int fat = cursor.getInt(7);

                FoodLog foodLog = new FoodLog(food, meal, time, calories, carbs, protein, fat);
                foodLogList.add(foodLog);

            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return foodLogList;
    }
}

package com.example.mealtrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DayDataDBH extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "ID";
    public static final String DAYDATA_TABLE = "DAYDATA_TABLE";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_WEIGHT = "WEIGHT";
    public static final String COLUMN_CALORIC_GOAL = "CALORIC_GOAL";
    public static final String COLUMN_CARBS_GOAL = "CARBS_GOAL";
    public static final String COLUMN_PROTEIN_GOAL = "PROTEIN_GOAL";
    public static final String COLUMN_FAT_GOAL = "FAT_GOAL";
    public static final String COLUMN_WATER_GOAL = "WATER_GOAL";
    public static final String COLUMN_WATER_INTAKE = "WATER_INTAKE";

    public DayDataDBH(@Nullable Context context) {
        super(context, "daydata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + DAYDATA_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT UNIQUE NOT NULL, " +
                COLUMN_WEIGHT + " INTEGER, " + COLUMN_CALORIC_GOAL + " INTEGER, " + COLUMN_CARBS_GOAL + " INTEGER, " +
                COLUMN_PROTEIN_GOAL + " INTEGER, " + COLUMN_FAT_GOAL + " INTEGER, " + COLUMN_WATER_GOAL + " INTEGER, " +
                COLUMN_WATER_INTAKE + " INTEGER)";

        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOneReplace(String date, int weight, int caloricGoal, int carbsGoal, int proteinGoal, int fatGoal, int waterGoal, int waterIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_CALORIC_GOAL, caloricGoal);
        cv.put(COLUMN_CARBS_GOAL, carbsGoal);
        cv.put(COLUMN_PROTEIN_GOAL, proteinGoal);
        cv.put(COLUMN_FAT_GOAL, fatGoal);
        cv.put(COLUMN_WATER_GOAL, waterGoal);
        cv.put(COLUMN_WATER_INTAKE, waterIntake);

        long insert = db.insertWithOnConflict(DAYDATA_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        if (insert == -1) {
            return false;
        } else {

            return true;
        }

    }

    public boolean updateGoals(String date, int weight, int caloricGoal, int carbsGoal, int proteinGoal, int fatGoal, int waterGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_CALORIC_GOAL, caloricGoal);
        cv.put(COLUMN_CARBS_GOAL, carbsGoal);
        cv.put(COLUMN_PROTEIN_GOAL, proteinGoal);
        cv.put(COLUMN_FAT_GOAL, fatGoal);
        cv.put(COLUMN_WATER_GOAL, waterGoal);

        long insert = db.insertWithOnConflict(DAYDATA_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addOneIgnore(String date, int weight, int caloricGoal, int carbsGoal, int proteinGoal, int fatGoal, int waterGoal, int waterIntake) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_CALORIC_GOAL, caloricGoal);
        cv.put(COLUMN_CARBS_GOAL, carbsGoal);
        cv.put(COLUMN_PROTEIN_GOAL, proteinGoal);
        cv.put(COLUMN_FAT_GOAL, fatGoal);
        cv.put(COLUMN_WATER_GOAL, waterGoal);
        cv.put(COLUMN_WATER_INTAKE, waterIntake);

        long insert = db.insertWithOnConflict(DAYDATA_TABLE, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int getIntByDate(String column, String date) {
        int returnInt;

        String query = "SELECT " + column + " FROM " + DAYDATA_TABLE + " WHERE " + COLUMN_DATE + " LIKE \'" + date + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            returnInt = cursor.getInt(cursor.getColumnIndex(column));
        } else {
            returnInt = 0;
        }
        cursor.close();

        return returnInt;
    }

    public String getStringByDate(String column, String date) {
        String returnString;

        String query = "SELECT " + column + " FROM " + DAYDATA_TABLE + " WHERE " + COLUMN_DATE + " LIKE \'" + date + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            returnString = cursor.getString(cursor.getColumnIndex(column));
        } else {
            returnString = "";
        }
        cursor.close();

        return returnString;
    }

    public void updateColumnWhereDateTo(String column, String date, int newValue) {
        String query = "UPDATE " + DAYDATA_TABLE + " SET " + column + " = " + newValue + " WHERE " + COLUMN_DATE + " LIKE \'" + date + "\'";

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(query);
    }

}

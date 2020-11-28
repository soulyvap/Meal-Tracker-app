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
    public static final String COLUMN_WATER_INTAKE = "WATER_INTAKE";

    public DayDataDHB(@Nullable Context context) {
        super(context, "daydata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableString = "CREATE TABLE " + DAYDATA_TABLE + " (" + COLUMN_DATE + " TEXT PRIMARY KEY, " +
                COLUMN_WEIGHT + " INTEGER, " + COLUMN_CALORIC_GOAL + " INTEGER, " + COLUMN_CARBS_GOAL + " INTEGER, " +
                COLUMN_PROTEIN_GOAL + " INTEGER, " + COLUMN_FAT_GOAL + " INTEGER, " + COLUMN_WATER_GOAL + " INTEGER, " +
                COLUMN_WATER_INTAKE + " INTEGER)";

        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(String date, int weight, int caloricGoal, int carbsGoal, int proteinGoal, int fatGoal, int waterGoal, int waterIntake) {
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

        long insert = db.insert(DAYDATA_TABLE, null, cv);
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
        return returnInt;
    }


}

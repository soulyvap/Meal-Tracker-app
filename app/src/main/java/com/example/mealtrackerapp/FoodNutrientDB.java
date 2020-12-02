package com.example.mealtrackerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class FoodNutrientDB extends SQLiteAssetHelper {
    public static final String FOODNUTRIENTS_DB = "foodnutrients.db";
    public static final int FOODNUTRIENTS_DB_VERSION = 1;

    public FoodNutrientDB(Context context) {
        super(context, FOODNUTRIENTS_DB, null, FOODNUTRIENTS_DB_VERSION);
    }

    public List<FoodNutrients> getFoodNutrients() {
        List<FoodNutrients> foodNutrientsList = new ArrayList<>();

        //get data from db
        String query = "SELECT * FROM " + "foodnutrients" + " ORDER BY FOODNAME ASC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //loop through cursor (result set) and create new foodLog object. then add them to the return array
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int calories = cursor.getInt(2);
                double carbs = cursor.getDouble(3);
                double protein = cursor.getDouble(4);
                double fat = cursor.getDouble(5);


                FoodNutrients foodNutrients = new FoodNutrients(id, name, calories, carbs, protein, fat);
                foodNutrientsList.add(foodNutrients);

            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();
        db.close();

        return foodNutrientsList;
    }
}

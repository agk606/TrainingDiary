package com.hfad.trainingdairy;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TrainingDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "trainings";
    private static final int DB_VERSION = 1;

    TrainingDatabaseHelper (Context context) {
        super(context,DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        updateMyDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void insertTraining (SQLiteDatabase db, String date, String exercise, String description){
        ContentValues trainingValues = new ContentValues();
        trainingValues.put("DATE", date);
        trainingValues.put("EXERCISE", exercise);
        trainingValues.put("DESCRIPTION", description);
        db.insert("PLANS", null, trainingValues);
    }

    private void updateMyDatabase (SQLiteDatabase db) {

        db.execSQL("CREATE TABLE PLANS (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DATE TEXT, " + "EXERCISE TEXT, " + "DESCRIPTION TEXT);");
        /*insertTraining(db, "Тренировка 01.01.2018", "Присед", "3*12");
        insertTraining(db, "Тренировка 01.01.2018", "Жим лежа", "3*12");
        insertTraining(db, "Тренировка 01.01.2018", "Становая тяга", "3*12");
        insertTraining(db, "Тренировка 01.02.2018", "Присед", "4*10");
        insertTraining(db, "Тренировка 01.02.2018", "Становая тяга", "2*6");*/
        }

}
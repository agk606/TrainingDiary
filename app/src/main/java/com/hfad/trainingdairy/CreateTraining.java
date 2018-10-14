package com.hfad.trainingdairy;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import android.widget.Toast;


public class CreateTraining extends AppCompatActivity {

    EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickSaveDate (View view) {
        etDate = findViewById(R.id.et_training_date);
        String strDate = etDate.getText().toString();

        SQLiteOpenHelper trainingDatabaseHelper = new TrainingDatabaseHelper(this);
        try {
            SQLiteDatabase db = trainingDatabaseHelper.getWritableDatabase();
            insertTraining(db,"Тренировка " + strDate, "", "");
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent(CreateTraining.this, TrainingPlans.class);
        startActivity(intent);
    }

    private static void insertTraining (SQLiteDatabase db, String date, String exercise, String description){
        ContentValues trainingValues = new ContentValues();
        trainingValues.put("DATE", date);
        trainingValues.put("EXERCISE", exercise);
        trainingValues.put("DESCRIPTION", description);
        db.insert("PLANS", null, trainingValues);
    }


}


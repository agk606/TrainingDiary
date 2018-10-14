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

public class CreateActivity extends AppCompatActivity {

    public static final String EXTRA_DATE_2 = "trDate";
    private EditText etExc;
    private EditText etSets;
    private String trDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        trDate = (String) getIntent().getExtras().get(EXTRA_DATE_2);
    }

    public void onClickSaveExc (View view) {
        etExc = findViewById(R.id.et_exc);
        String strExc = etExc.getText().toString();
        etSets = findViewById(R.id.et_sets);
        String strSets = etSets.getText().toString();

        SQLiteOpenHelper trainingDatabaseHelper = new TrainingDatabaseHelper(this);
        try {
            SQLiteDatabase db = trainingDatabaseHelper.getWritableDatabase();
            insertTraining(db,trDate, strExc, strSets);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent(CreateActivity.this, TrainingActivity.class);
        intent.putExtra(TrainingActivity.EXTRA_DATE, trDate);
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

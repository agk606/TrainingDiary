package com.hfad.trainingdairy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onClickTraining(View view) {
        Intent intent = new Intent(StartScreen.this, TrainingPlans.class);
        startActivity(intent);
    }

    /*public void onClickExercises(View view) {
        Intent intent = new Intent(StartScreen.this, Exercises.class);
        startActivity(intent);
    }*/
}
package com.hfad.trainingdairy;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TrainingActivity extends AppCompatActivity{

    private ShareActionProvider shareActionProvider;
    public static final String EXTRA_DATE = "trainingDate";
    private String trainingDate;
    private TextView exercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Получаем данные из интента, записанные в переменную EXTRA_DATE
        //Записываем эти данные в переменную trainingDate
        trainingDate = (String) getIntent().getExtras().get(EXTRA_DATE);
        //Выводим данные из переменной trainingDate в TextView с иденификатором training_data
        TextView tDate = findViewById(R.id.training_data);
        tDate.setText(trainingDate + "\n");

        //Создаем помощника
        SQLiteOpenHelper trainingDatabaseHelper = new TrainingDatabaseHelper(this);
        try {
            //Получаем ссылку на базу данных для чтения информации
            SQLiteDatabase db = trainingDatabaseHelper.getWritableDatabase();

            //Создаем курсор и выбираем данные из столбцов EXERCISE и DESCRIPTION по признаку DATE
            Cursor cursor = db.query("PLANS",
                    new String[] {"EXERCISE", "DESCRIPTION"},
                    "DATE = ?",
                    new String[] {trainingDate},
                    null, null, null);

            //Начинаем считывать данные из курсора, начиная с первой строки
            if(cursor.moveToFirst()) {

                //Для того, чтобы считались данные из каждой строки курсора, запускаем цикл do-while
                do {
                    String exerciseText = cursor.getString(0);
                    String descriptionText = cursor.getString(1);

                    //Выводим данные из переменных в объект TextView с идентификатором exercise
                    exercise = (TextView)findViewById(R.id.exercise);
                    exercise.append(exerciseText + "\t");
                    exercise.append(descriptionText + "\n");
                } while(cursor.moveToNext());
            }

            //Закрываем курсор и базу данных
            cursor.close();
            db.close();

            //В случае невозможности чтения данных из базы выводим исключение с текстом Database unavailable
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        FloatingActionButton fab = findViewById(R.id.add_training);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainingActivity.this, CreateActivity.class);
                intent.putExtra(CreateActivity.EXTRA_DATE_2, trainingDate);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_send);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        String strExercise = exercise.getText().toString();
        setShareActionIntent(trainingDate + "\n" + strExercise);
        return super.onCreateOptionsMenu(menu);
    }

    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }
}
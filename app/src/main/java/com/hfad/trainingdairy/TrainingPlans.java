package com.hfad.trainingdairy;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;

import org.w3c.dom.Text;

public class TrainingPlans extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private static final int CM_DELETE_ID = 1;
    private String TrainingData;
    String bbb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_plans);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listTrainings = (ListView) findViewById(R.id.list_trainings);

        registerForContextMenu(listTrainings);

        SQLiteOpenHelper trainingDatabaseHelper = new TrainingDatabaseHelper(this);

        try{
            db = trainingDatabaseHelper.getReadableDatabase();

            cursor = db.query("PLANS",
                    new String[] {"_id", "DATE"},
                    null, null, "DATE", null, null);

            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[] {"DATE"},
                    new int[] {android.R.id.text1},
                    0);

            listTrainings.setAdapter(listAdapter);

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listTrainings, View itemView, int position, long id) {
                TextView itemTraining = (TextView) itemView.findViewById(android.R.id.text1);
                TrainingData = itemTraining.getText().toString();
                Intent intent = new Intent (TrainingPlans.this, TrainingActivity.class);
                intent.putExtra(TrainingActivity.EXTRA_DATE, TrainingData);
                startActivity(intent);
            }
        };

        listTrainings.setOnItemClickListener(itemClickListener);

        FloatingActionButton fab = findViewById(R.id.add_training);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrainingPlans.this, CreateTraining.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();

            View vItem = acmi.targetView;
            TextView txt = (TextView) vItem.findViewById(android.R.id.text1);
            String sItem = txt.getText().toString();

            SQLiteOpenHelper trainingDatabaseHelper = new TrainingDatabaseHelper(this);

            try {
                db = trainingDatabaseHelper.getWritableDatabase();
                Cursor cursor = db.query("PLANS",
                        new String[]{"DATE"},
                        null,
                        null,
                        null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        db.delete("PLANS", "DATE = ?", new String[]{sItem});
                    } while (cursor.moveToNext());
                }


            } catch (SQLiteException e) {
                Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }

            onRestart();

            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Cursor newCursor = db.query("PLANS",
                new String[]{"_id", "DATE"},
                null, null, "DATE", null, null);
        ListView listTrainings = (ListView) findViewById(R.id.list_trainings);
        CursorAdapter adapter = (CursorAdapter) listTrainings.getAdapter();
        adapter.changeCursor(newCursor);
        cursor = newCursor;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}

package com.assignment.cameron.healthapp;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * AUTHOR: Cameron Fry
 *
 * This class acts as the results entry activity. It instantiates a writable database as well as
 * gui elements allowing the user to write to the DB.
 * Created by Cameron on 18/03/2015.
 */
public class resultsEntry extends Activity implements OnItemSelectedListener, OnClickListener {
    String TAG = new String("restultsEntry: ");
    String exerciseTypeItems[] = {"Intensity", "Moderate", "Vigorous"};

    healthAppDB healthDB;
    SQLiteDatabase db;


    int tableCount = 1;
    int submitCount;
    String temp;
    ContentValues values = new ContentValues();

    private SimpleDateFormat dateFormatter;

    //DATE PICKER VIEWS
    private EditText[] dateText = new EditText[5];
    private DatePickerDialog[] dateDialog = new DatePickerDialog[5];


    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "enters onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_entry);
        healthDB = new healthAppDB(this);
        db = healthDB.getWritableDatabase();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


        initPage();
    }

    public void initPage() {
        //VARIABLE FOR FIRST TABLE AND TABLEROWS
        final TableRow[] tableRows = new TableRow[5];
        TableLayout table = (TableLayout) findViewById(R.id.resultsEntryTable);

        //EDITTEXT VIEWS
        final EditText[] startText = new EditText[5];
        final EditText[] finishText = new EditText[5];

        //INSTANTIATES EDITTEXT VIEWS
        dateText[0] = (EditText) findViewById(R.id.date1);
        dateText[1] = (EditText) findViewById(R.id.date2);
        dateText[2] = (EditText) findViewById(R.id.date3);
        dateText[3] = (EditText) findViewById(R.id.date4);
        dateText[4] = (EditText) findViewById(R.id.date5);

        startText[0] = (EditText) findViewById(R.id.start1);
        startText[1] = (EditText) findViewById(R.id.start2);
        startText[2] = (EditText) findViewById(R.id.start3);
        startText[3] = (EditText) findViewById(R.id.start4);
        startText[4] = (EditText) findViewById(R.id.start5);

        finishText[0] = (EditText) findViewById(R.id.finish1);
        finishText[1] = (EditText) findViewById(R.id.finish2);
        finishText[2] = (EditText) findViewById(R.id.finish3);
        finishText[3] = (EditText) findViewById(R.id.finish4);
        finishText[4] = (EditText) findViewById(R.id.finish5);


        //INSTANTIATES ALL TABLE ROWS
        tableRows[0] = (TableRow) findViewById(R.id.rowOne);
        tableRows[1] = (TableRow) findViewById(R.id.rowTwo);
        tableRows[2] = (TableRow) findViewById(R.id.rowThree);
        tableRows[3] = (TableRow) findViewById(R.id.rowFour);
        tableRows[4] = (TableRow) findViewById(R.id.rowFive);
        Log.d(TAG, "instantiatesRows");

        //SETS THE LAST 4 ROWS TO BE INVISIBLE UPON LOAD
        tableRows[1].setVisibility(View.INVISIBLE);
        tableRows[2].setVisibility(View.INVISIBLE);
        tableRows[3].setVisibility(View.INVISIBLE);
        tableRows[4].setVisibility(View.INVISIBLE);
        Log.d(TAG, "removes rows");

        //CREATES ARRAY VARIABLE FOR SPINNERS AND INSTANTIATES IT
        final Spinner[] excerciseTypes = new Spinner[5];
        excerciseTypes[0] = (Spinner) findViewById(R.id.excerciseType1);
        excerciseTypes[1] = (Spinner) findViewById(R.id.excerciseType2);
        excerciseTypes[2] = (Spinner) findViewById(R.id.excerciseType3);
        excerciseTypes[3] = (Spinner) findViewById(R.id.excerciseType4);
        excerciseTypes[4] = (Spinner) findViewById(R.id.excerciseType5);


        //ASSOCIATES ALL SPINNERS WITH AN ADAPTER AS WELL AS ADDS A LISTENER TO ALL SPINNERS
        excerciseTypes[0].setOnItemSelectedListener(this);
        ArrayAdapter<String> excerciseTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exerciseTypeItems);
        excerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        excerciseTypes[0].setAdapter(excerciseTypeAdapter);

        excerciseTypes[1].setOnItemSelectedListener(this);
        excerciseTypes[1].setAdapter(excerciseTypeAdapter);

        excerciseTypes[2].setOnItemSelectedListener(this);
        excerciseTypes[2].setAdapter(excerciseTypeAdapter);

        excerciseTypes[3].setOnItemSelectedListener(this);
        excerciseTypes[3].setAdapter(excerciseTypeAdapter);

        excerciseTypes[4].setOnItemSelectedListener(this);
        excerciseTypes[4].setAdapter(excerciseTypeAdapter);

        //BUTTONS
        Button submitEntry = (Button) findViewById(R.id.submit);
        Button addEntry = (Button) findViewById(R.id.addEntry);
        Button removeEntry = (Button) findViewById(R.id.removeEntry);

        //TEMP BUTTON FOR DEBUGGING
        Button resetDB = (Button) findViewById(R.id.resetDB);

        //ADDS LISTENERS TO THE DATE VIEWS
        dateText[0].setInputType(InputType.TYPE_NULL);
        dateText[0].setOnClickListener(this);
        dateText[1].setInputType(InputType.TYPE_NULL);
        dateText[1].setOnClickListener(this);
        dateText[2].setInputType(InputType.TYPE_NULL);
        dateText[2].setOnClickListener(this);
        dateText[3].setInputType(InputType.TYPE_NULL);
        dateText[3].setOnClickListener(this);
        dateText[4].setInputType(InputType.TYPE_NULL);
        dateText[4].setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        dateDialog[0] = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText[0].setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateDialog[1] = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText[1].setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateDialog[2] = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText[2].setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateDialog[3] = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText[3].setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dateDialog[4] = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateText[4].setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        //ADDS AN ADDITIONAL ENTRY ROW IF THE USER CLICKS "ADD ENTRY"
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableCount <= 4) {
                    Log.d(TAG, "attempting to add a row at " + tableCount);
                    //table.addView(tableRows[tableCount], tablePosition);
                    tableRows[tableCount].setVisibility(View.VISIBLE);
                    Log.d(TAG, "row added!");
                    tableCount++;
                } else {
                    Log.d(TAG, "added all the rows!");
                }

            }
        });

        //REMOVES AN ADDITIONAL ENTRY ROW IF THE USER CLICKS "REMOVE ENTRY"
        removeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        if (tableCount >= 2) {
            Log.d(TAG, "attempting to remove a row at " + tableCount);
            // table.removeView(tableRows[tableCount-1]);
            tableRows[tableCount - 1].setVisibility(View.INVISIBLE);
            Log.d(TAG, "row removed!");
            tableCount--;
        } else {
            Log.d(TAG, "all the rows removed!");
        }
    }
});
        //WRITES DATA TO THE DATABASE ON PRESSING SUBMIT
        submitEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "submit pressed!");


                submitCount = tableCount;

                for (int i = 0; i < submitCount; i++) {
                    Log.d(TAG, "retrieving data from row " + i);
                    values.put(dbContract.dbEntry.COLUMN_NAME_START_TIME, startText[i].getText().toString());
                    values.put(dbContract.dbEntry.COLUMN_NAME_FINISH_TIME, finishText[i].getText().toString());
                    values.put(dbContract.dbEntry.COLUMN_NAME_DATE_TIME, dateText[i].getText().toString());
                    values.put(dbContract.dbEntry.COLUMN_NAME_MET_DATA, excerciseTypes[i].getSelectedItem().toString()); //unused table collumn
                    long newRow = db.insert(dbContract.dbEntry.TABLE_NAME, null, values);


                    Log.d(TAG, "data sent: " + startText[i].getText().toString());
                    Log.d(TAG, "data sent: " + finishText[i].getText().toString());
                    Log.d(TAG, "data sent: " + excerciseTypes[i].getSelectedItem().toString());

                }
                Intent i = new Intent(resultsEntry.this, resultsSummary.class);
                startActivity(i);

            }
        });

        resetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(dbContract.dbEntry.TABLE_NAME, null, null);
                Log.d(TAG, "Database cleared!");
            }
        });

    }


    public void onItemSelected(AdapterView<?> parent, View v, int position,
                               long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onClick(View view) {
        if (view == dateText[0]) {
            dateDialog[0].show();

        }
        else if (view == dateText[1])
        {
            dateDialog[1].show();
        }

        else if (view == dateText[2])
        {
            dateDialog[2].show();
        }

        else if (view == dateText[3])
        {
            dateDialog[3].show();
        }

        else if (view == dateText[4])
        {
            dateDialog[4].show();
        }


    }

    public void onRestart() {
        Log.d(TAG, "Entered onResume");
        super.onRestart();

    }


}

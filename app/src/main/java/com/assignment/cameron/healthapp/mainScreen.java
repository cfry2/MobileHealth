package com.assignment.cameron.healthapp;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import java.util.Random;

/**
 * AUTHOR: Cameron Fry
 * Main screen for the App, acts as the main activity and used as a navigation portal for the app
 */

public class mainScreen extends Activity {
    String TAG = new String("main: ");
    healthAppDB healthDB;
    SQLiteDatabase db;
    Cursor c;
    TextView tip = (TextView)findViewById(R.id.randomTip);
    TextView today = (TextView)findViewById(R.id.excerciseReminder);

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Entered onCreate()");

        setContentView(R.layout.activity_main_screen);
        healthDB = new healthAppDB(this);

        db = healthDB.getReadableDatabase();
        //Checks to see if there is an entry for today.
        c = db.rawQuery("select count(*) from resultsSummary where date(date) == date('now')", null);

        initResultsEntry();




    }

    /**
     * Initializes the screen. This method sets up the two buttons as well as the strings that act as
     * as the random tip and titles
     */
    public void initResultsEntry()
    {


        tip.setText(randomTip()); //Grabs randomized string from randomTip()
        today.setText(checkToday()); //Checks to see if a result was entered today

        //BUTTONS
        Button resultsEntry = (Button)findViewById(R.id.enterResultsButton);
        resultsEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "resultsEntry clicked!");
                Intent i = new Intent(mainScreen.this, resultsEntry.class);
                startActivity(i);
            }
        });

        Button resultsSummary = (Button)findViewById(R.id.viewResultsButton);
        resultsSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "resultsSummary clicked!");
                Intent i = new Intent(mainScreen.this, resultsSummary.class);
                startActivity(i);
            }
        });
    }

    /**
     *  5 random tips with one being returned using an RNG, gets called in the initResults() method.
     *
     */
    public String randomTip()
    {
        String[] tips = new String[5];
        tips[0] = "Exercising with friends is a great way to socialize!";
        tips[1] = "Be sure to get the 450 MET's this week!";
        tips[2] = "MET is a method of combining both moderate and vigorous activity.";
        tips[3] = "Be sure to take vigorous activity in moderation";
        tips[4] = "Be sure to measure your pulse after each workout.";
        Random rng = new Random();
        int number = rng.nextInt(5);

        return tips[number];
    }

    /**
     * Looks through cursor to see if count(*) returned any results if it didn't it reminds
     * the user to exercise today!
     *
     */
    public String checkToday()
    {
        c.moveToFirst();
        if (c.getInt(0) == 0)
        {
            return "Make sure to exercise today!";
        }
        else
        {
            return "You have an entry for today awesome!";
        }

    }

    @Override
    public void onRestart() {
        Log.d(TAG, "Entered onResume");
        super.onRestart();
        tip.setText(randomTip()); //Grabs randomized string from randomTip()
        today.setText(checkToday()); //Checks to see if a result was entered today

    }
}

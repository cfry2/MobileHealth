package com.assignment.cameron.healthapp;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * AUTHOR: Cameron Fry
 * Instantiates a list view and acts as the results summary activity. Also creates a readable
 * database and grabs all the entries from the table for the given week. Converts all the data
 * retrieved to be displayed in a meaningful way as well as calculates the weekly MET score.
 *
 * Created by Cameron on 18/03/2015.
 */
public class resultsSummary extends Activity {
   String TAG = new String("restultsEntry: ");
   ContentValues values = new ContentValues();
    List<entry> entryList = new ArrayList<entry>();
    entryListAdapter entryListA = null;
    ListView resultsList;
    healthAppDB healthDB;
    SQLiteDatabase db;
    Cursor c;

    public static String MET;
    public int metSummary;



    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "enters create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_summary);

        resultsList = (ListView)findViewById(R.id.entryItem);

        healthDB = new healthAppDB(this);
        db = healthDB.getReadableDatabase();

        c = db.rawQuery("select * from resultsSummary where strftime('%W', date) == strftime('%W', 'now') order by date(date) ASC", null);

        entryListA = new entryListAdapter();
        readDatabase();
        resultsList.setAdapter(entryListA);
        initWeeklySummary();
        Log.d(TAG, "leaves create");

    }


    public void testDB() //Tests the functionality of the database for debugging purposes only
    {
        healthAppDB healthDB = new healthAppDB(this);
        SQLiteDatabase db = healthDB.getWritableDatabase();
        values.put(dbContract.dbEntry.COLUMN_NAME_START_TIME, "13:00");
        values.put(dbContract.dbEntry.COLUMN_NAME_FINISH_TIME, "14:00");
        values.put(dbContract.dbEntry.COLUMN_NAME_DATE_TIME, "23/03/2015");
        values.put(dbContract.dbEntry.COLUMN_NAME_MET_DATA, "6");

        long newRow = db.insert(dbContract.dbEntry.TABLE_NAME, null, values);

        Cursor c = db.rawQuery("select * from resultsSummary", null);
        c.moveToFirst();

        if (c.moveToFirst())
        {
            do {
                String ID = c.getString(0);
                String start = c.getString(2);

                Log.d(TAG, "ID is " + ID);
                Log.d(TAG, "start is " + start);
            }
            while (c.moveToNext());
        }


    }

    public void initWeeklySummary()
    {
        Log.d(TAG, "generates summary");
        TextView metScore, toast;
        metScore = (TextView)findViewById(R.id.metValue);
        toast = (TextView)findViewById(R.id.summaryToast);
        String metSummaryString;

        metSummaryString = String.valueOf(metSummary);
        metScore.setText(metSummaryString);
        Log.d(TAG, "metSummary is " + metSummary);

        if(metSummary >= 450)
        {
            toast.setText("Well done!");
        }
        else
        {
            toast.setText("C'mon you can do it!");
        }
    }

    /**
     * reads data from the cursor and then adds them to a link list of "entry" datatypes.
     */
    public void readDatabase()
    {
        Log.d(TAG, "enters readDatabase");
        if (c.moveToFirst())
        {
            do {
                entry e = createEntry(c);

                entryListA.add(e);

            }
            while (c.moveToNext());
        }
    }

    /**
     * gets called in readDatabase(), adds data from the curser to an entry
     */
    public entry createEntry(Cursor c)
    {
        Log.d(TAG, "enters createEntry");
        if (c == null || c.isAfterLast() || c.isBeforeFirst())
        {
            return null;
        }

        entry e = new entry();

        e.setStart(c.getString(2));
        e.setFinish(c.getString(3));
        e.setDate(c.getString(4));
        e.setMet(c.getString(5));
        MET = calculateMet(c.getString(2), c.getString(3), c.getString(5));


        Log.d(TAG, e.getStart());
        Log.d(TAG, e.getFinish());
        Log.d(TAG, e.getDate());
        Log.d(TAG, e.getMet());

        Log.d(TAG, "leaves createEntry");
        return e;

    }

    //Calculates the MET score for the given week, stores it in metSummary
    public String calculateMet(String start, String finish, String intensity)
    {
        int hour1, hour2, min1, min2, score;

        String metValue;

        hour1 = Integer.parseInt(start.substring(0, 2));
        hour2 = Integer.parseInt(finish.substring(0, 2));

        min1 = Integer.parseInt(start.substring(3, 5));
        min2 = Integer.parseInt(finish.substring(3, 5));

        min1 = min1 + (hour1 * 60);
        min2 = min2 + (hour2 * 60);

        score = min2 - min1;

        if (intensity.contains("Moderate"))
        {
            score = 3 * score;
        }
        else
        {
            score = 6 * score;
        }

        metValue = String.valueOf(score);

        metSummary += score;
        Log.d(TAG, "metSummary at calc is " + metSummary);

        return metValue;



    }


    /**
     * Custom adapted for the List view. Creates a table view with two rows, one row acts as a title
     * row with the second one having 5 columns for start, finish, date, intensity and the MET
     * score.
     */
    class entryListAdapter extends ArrayAdapter<entry>
    {        public entryListAdapter()
        {
            super(resultsSummary.this, android.R.layout.simple_list_item_1, entryList);
        }

        public View getView(int position, View row, ViewGroup parent)
        {
            LinearLayout rowLayout = null;
            TextView start = null;
            TextView finish = null;
            TextView date = null;
            TextView met = null;
            TextView intensity = null;
            String MET;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE", Locale.US);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date temp;


            LayoutInflater inflater = getLayoutInflater();
            row = inflater.inflate(R.layout.item_row, null);
            entry e = entryList.get(position);


            start = (TextView) row.findViewById(R.id.start);
            finish = (TextView) row.findViewById(R.id.finish);
            date = (TextView) row.findViewById(R.id.date);
            intensity = (TextView) row.findViewById(R.id.intensity);
            met = (TextView) row.findViewById(R.id.MET);
            MET = calculateMet(e.getStart(), e.getFinish(), e.getMet());

            try{
                temp = sdf.parse(e.getDate());
                date.setText(dateFormatter.format(temp));
            }
            catch (ParseException b) {
                // TODO Auto-generated catch block
                b.printStackTrace();
            }



            start.setText(e.getStart());
            finish.setText(e.getFinish());
            intensity.setText(e.getMet());

            met.setText(MET);




            return(row);
        }



    }

}


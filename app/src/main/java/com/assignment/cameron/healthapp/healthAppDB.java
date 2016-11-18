package com.assignment.cameron.healthapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * AUTHOR: Cameron Fry
 *  creates and instantiates the database to be used on the results summary screen
 * last edited on 22/03/2015.
 */

public class healthAppDB extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1; //needed to be incremented if an update to the db schema as to occur
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + dbContract.dbEntry.TABLE_NAME + " (" + dbContract.dbEntry._ID + " INTEGER PRIMARY KEY," + dbContract.dbEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP + dbContract.dbEntry.COLUMN_NAME_START_TIME + TEXT_TYPE + COMMA_SEP + dbContract.dbEntry.COLUMN_NAME_FINISH_TIME + TEXT_TYPE + COMMA_SEP + dbContract.dbEntry.COLUMN_NAME_DATE_TIME + TEXT_TYPE + COMMA_SEP + dbContract.dbEntry.COLUMN_NAME_MET_DATA + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + dbContract.dbEntry.TABLE_NAME;


    public healthAppDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    //gets run once DATA_VERSION gets updated
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);


    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

}
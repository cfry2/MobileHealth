package com.assignment.cameron.healthapp;

import android.provider.BaseColumns;

/**
 * AUTHOR: Cameron Fry
 * Defines the layout of the database for healthApp
 * last edited 22/03/2015.
 */
public class dbContract {
    public dbContract() {}//Empty constructor so that dbContract can't be instantiated by accident

    public static abstract class dbEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "resultsSummary";
        public static final String COLUMN_NAME_ENTRY_ID  = "entryid";
        public static final String COLUMN_NAME_START_TIME  = "start";
        public static final String COLUMN_NAME_FINISH_TIME  = "finish";
        public static final String COLUMN_NAME_DATE_TIME  = "date";
        public static final String COLUMN_NAME_MET_DATA  = "MET";
    }
}

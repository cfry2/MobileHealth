package com.assignment.cameron.healthapp;

import android.content.Context;

/**
 * AUTHOR: Cameron Fry
 * Data type to store data from the DB in memory
 * Last edited on 22/03/2015.
 */
public class entry
{
    private String start, finish, date, met;
    public entry()
    {

    }

    //Creates a new entry and then sets the Strings to the method parameters.
    public static entry addEntry(Context ctx, String start, String finish, String date, String met)
    {
        entry e = new entry();

        e.setStart(start);
        e.setFinish(finish);
        e.setDate(date);
        e.setMet(met);

        return e;
    }

    //Datatype getters and setters
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMet() {
        return met;
    }

    public void setMet(String met) {
        this.met = met;
    }
}

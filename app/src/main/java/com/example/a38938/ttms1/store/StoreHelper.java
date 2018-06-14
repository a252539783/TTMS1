package com.example.a38938.ttms1.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by LQF on 2018/5/28.
 */

public class StoreHelper extends SQLiteOpenHelper {
    public StoreHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SCHEDULE);
        db.execSQL(SQL_CREATE_PLAY);
        db.execSQL(SQL_CREATE_STUDIO);
        db.execSQL(SQL_CREATE_TICKET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static final String SQL_CREATE_SCHEDULE = "CREATE TABLE IF NOT EXISTS schedule(id integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "play integer,\n" +
            "start text,\n" +
            "end text,\n" +
            "studio integer,\n" +
            "price integer," +
            "sold text);";
    private static final String SQL_CREATE_PLAY = "CREATE TABLE IF NOT EXISTS play(id integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "name text,\n" +
            "director text,\n" +
            "actors text,\n" +
            "intro text,\n" +
            "score float," +
            "imgpath text," +
            "length integer);";
    private static final String SQL_CREATE_STUDIO = "CREATE TABLE IF NOT EXISTS studio(id integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "name text,\n" +
            "seat integer,\n" +
            "rows integer,\n" +
            "columns integer,\n" +
            "invalid text);";

    private static final String SQL_CREATE_TICKET = "CREATE TABLE IF NOT EXISTS ticket(id integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "schedule integer,\n" +
            "seatx integer,\n" +
            "seaty integer);";
}

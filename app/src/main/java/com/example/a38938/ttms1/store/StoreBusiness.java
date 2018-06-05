package com.example.a38938.ttms1.store;

import android.database.sqlite.SQLiteDatabase;

import com.example.a38938.ttms1.data.Data;

import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

/**
 * TODO 如果不分库，那么将没有必要
 */
public class StoreBusiness {
    public static final String ROW_ID = "id";

    public static final String ROW_PLAY = "play";
    public static final String ROW_START = "start";
    public static final String ROW_END = "end";
    public static final String ROW_STUDIO = "studio";
    public static final String ROW_SEAT = "seat";
    public static final String ROW_PRICE = "price";

    public static final String ROW_NAME = "name";
    public static final String ROW_SCORE = "score";
    public static final String ROW_INTRO = "intro";
    public static final String ROW_ACTORS = "actors";
    public static final String ROW_DIRECTOR = "director";
    public static final String ROW_IMGPATH = "imgpath";
    public static final String ROW_LENGTH = "length";

    public static final String ROW_ROW = "rows";
    public static final String ROW_COLUMN = "columns";
    public static final String ROW_INVALID = "invalid";

    public static final String TABLE_SCHEDULE = "schedule";
    public static final String TABLE_PLAY = "play";
    public static final String TABLE_STUDIO = "studio";

    private StoreHelper mHelper;

    public StoreBusiness() {
        this("ttms");
    }

    /**
     * @param name database name
     */
    private StoreBusiness(String name) {
        mHelper = new StoreHelper(StoreManager.get().mAppContext, name, 1);
    }

    public long insert(Data data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return AccessHelper.insert(data, db);
    }

    public void update(Data data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        AccessHelper.update(data, db);
    }

    public void delete(Data data) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        AccessHelper.delete(data, db);
    }

    public <T extends Data> List<T> query(Class type, String where) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return AccessHelper.query(type, where, db);
    }
}

package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/6/14.
 */

public class UserData extends Data {

    public String usrname;
    public String passwd;
    public String name;
    public int permission = 0;

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_NAME, name);
        cv.put(StoreBusiness.ROW_USR, usrname);
        cv.put(StoreBusiness.ROW_PWD, passwd);
        return cv;
    }
}

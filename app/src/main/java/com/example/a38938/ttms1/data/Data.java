package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/5/28.
 */

public abstract class Data {
    public static int TYPE_SCHEDULE = 0;
    public static int TYPE_PLAY = 1;
    public static int TYPE_STUDIO = 2;

    public int type = 0;    //type不存储
    public long id = -1;

    public abstract ContentValues contentValues();
}

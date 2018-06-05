package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/5/28.
 */

public class ScheduleData extends Data {

    public long play = 0;
    public String start = "00:00", end = "00:00";
    public int studio = 0, seat = 0;
    public int price = 0;

    public ScheduleData() {
        type = TYPE_SCHEDULE;
    }

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_PLAY, play);
        cv.put(StoreBusiness.ROW_END, end);
        cv.put(StoreBusiness.ROW_START, start);
        cv.put(StoreBusiness.ROW_SEAT, seat);
        cv.put(StoreBusiness.ROW_STUDIO, studio);
        cv.put(StoreBusiness.ROW_PRICE, price);
        return cv;
    }
}

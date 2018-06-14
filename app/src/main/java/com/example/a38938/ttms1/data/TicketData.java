package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.AccessHelper;
import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/6/14.
 */

public class TicketData extends Data {

    public long schedule;
    public int seat_x;
    public int seat_y;

    public TicketData () {
    }

    public ScheduleData getSchedule() {
        return AccessHelper.sSchedules.get(schedule);
    }

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_SCHEDULE, schedule);
        cv.put(StoreBusiness.ROW_SEAT_X, seat_x);
        cv.put(StoreBusiness.ROW_SEAT_Y, seat_y);
        return cv;
    }
}

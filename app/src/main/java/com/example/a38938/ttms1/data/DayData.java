package com.example.a38938.ttms1.data;

import java.util.ArrayList;

/**
 * Created by LQF on 2018/6/11.
 */

public class DayData extends ArrayList<ScheduleData> {
    @Override
    public boolean add(ScheduleData scheduleData) {
        int index = 0;
        while (index < size()) {
            index ++;
        }
        return super.add(scheduleData);
    }
}

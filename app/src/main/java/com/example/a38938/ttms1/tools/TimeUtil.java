package com.example.a38938.ttms1.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LQF on 2018/6/8.
 */

public class TimeUtil {

    static SimpleDateFormat sFull = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static SimpleDateFormat sDay = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat sHour = new SimpleDateFormat("HH:mm");

    public static String getHour(long time) {
        return sHour.format(new Date(Long.valueOf(time)));
    }

    public static String getFull(long time) {
        return sFull.format(new Date(Long.valueOf(time)).getHours());
    }

    public static long getTime(int year, int month, int day, int hour, int min) {
        try {
            return sFull.parse(year + "-" + month + "-" + day + " " + hour + ":" + min).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int dateToWeek(String datetime) {
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = sDay.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return w;
    }

    public static void main(String []args) {
        System.out.println(getHour(getTime(2,12,12,12,12)));
    }
}

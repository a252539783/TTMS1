package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.AccessHelper;
import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/5/28.
 */

public class ScheduleData extends Data {

    public long play = 0;
    public long start = 0, end = 0;
    public long studio = 0;
    public float price = 0;
    private boolean[][] sold = null;
    public int soldNum = 0;

    public ScheduleData() {
        type = TYPE_SCHEDULE;
    }

    public void addSold(int row, int column) {
        if (sold == null) {
            sold = new boolean[getStudio().rows()][getStudio().columns()];
        }

        sold[row][column] = !sold[row][column];
        if (sold[row][column]) {
            soldNum++;
        } else {
            soldNum--;
        }
    }

    public boolean isSold(int r, int c) {
        if (sold == null) {
            return false;
        }

        if (sold.length <= r) {
            return false;
        }

        if (sold[0].length <= c) {
            return false;
        }

        return sold[r][c];
    }

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_PLAY, play);
        cv.put(StoreBusiness.ROW_END, end);
        cv.put(StoreBusiness.ROW_START, start);
        cv.put(StoreBusiness.ROW_STUDIO, studio);
        cv.put(StoreBusiness.ROW_PRICE, price);

        StringBuilder res = new StringBuilder("");
        if (sold != null) {
            for (int i = 0; i < sold.length; i++) {
                for (int j = 0; j < sold[0].length; j++) {
                    if (sold[i][j]) {
                        res.append(i).append(',').append(j).append(',');
                    }
                }
            }
        }
        cv.put(StoreBusiness.ROW_SOLD, res.toString());
        return cv;
    }

    public ScheduleData copy() {
        ScheduleData sd = new ScheduleData();
        sd.id = id;
        sd.play = play;
        sd.studio = studio;
        sd.start = start;
        sd.end = end;
        sd.price = price;

        if (sold != null) {
            for (int i = 0; i < sold.length; i++) {
                for (int j = 0;j < sold[0].length; j++) {
                    if (sold[i][j]) {
                        sd.addSold(i, j);
                    }
                }
            }
        }

        return sd;
    }

    public void set(ScheduleData sd) {
        id = sd.id;
        play = sd.play;
        studio = sd.studio;
        start = sd.start;
        end = sd.end;
        price = sd.price;
        sold = sd.sold;
        soldNum = sd.soldNum;
    }

    public PlayData getPlay() {
        return AccessHelper.sPlays.get(play);
    }

    public StudioData getStudio() {
        return AccessHelper.sStudios.get(studio);
    }
}

package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/5/29.
 */

public class PlayData extends Data {
    public String name;
    public String director;
    public String actors;
    public float score;
    public String intro;
    public String imgPath;
    public int length;

    public PlayData() {
        type = TYPE_PLAY;
    }

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_NAME, name);
        cv.put(StoreBusiness.ROW_DIRECTOR, director);
        cv.put(StoreBusiness.ROW_ACTORS, actors);
        cv.put(StoreBusiness.ROW_SCORE, score);
        cv.put(StoreBusiness.ROW_INTRO, intro);
        cv.put(StoreBusiness.ROW_IMGPATH, imgPath);
        cv.put(StoreBusiness.ROW_LENGTH, length);
        return cv;
    }
}

package com.example.a38938.ttms1.store;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.data.StudioData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

public abstract class AccessHelper<T> {
    private static HashMap<Class, AccessHelper> mHelpers = new HashMap<>();
    static {
        mHelpers.put(ScheduleData.class, new AccessHelper<ScheduleData>() {
            private int PLAY_INDEX = -1;
            private int START_INDEX = -1;
            private int END_INDEX = -1;
            private int SEAT_INDEX = -1;
            private int STUDIO_INDEX = -1;
            private int PRICE_INDEX = -1;
            private int ID_INDEX = -1;

            @Override
            public long insertInner(ScheduleData data, SQLiteDatabase db) {
                return db.insert(StoreBusiness.TABLE_SCHEDULE, null, data.contentValues());
            }

            @Override
            public List<ScheduleData> queryInner(String where, SQLiteDatabase db) {
                Cursor c = db.query(StoreBusiness.TABLE_SCHEDULE, null, where, null, null, null, null);
                try {
                    if (PLAY_INDEX == -1) {
                        PLAY_INDEX = c.getColumnIndex(StoreBusiness.ROW_PLAY);
                        START_INDEX = c.getColumnIndex(StoreBusiness.ROW_START);
                        END_INDEX = c.getColumnIndex(StoreBusiness.ROW_END);
                        SEAT_INDEX = c.getColumnIndex(StoreBusiness.ROW_SEAT);
                        STUDIO_INDEX = c.getColumnIndex(StoreBusiness.ROW_STUDIO);
                        PRICE_INDEX = c.getColumnIndex(StoreBusiness.ROW_PRICE);
                        ID_INDEX = c.getColumnIndex(StoreBusiness.ROW_ID);
                    }

                    List<ScheduleData> datas = new ArrayList<>(c.getCount());
                    while (c.moveToNext()) {
                        ScheduleData data = new ScheduleData();
                        data.play = c.getInt(PLAY_INDEX);
                        data.price = c.getInt(PRICE_INDEX);
                        data.start = c.getString(START_INDEX);
                        data.end = c.getString(END_INDEX);
                        data.studio = c.getInt(STUDIO_INDEX);
                        data.seat = c.getInt(SEAT_INDEX);
                        data.id = c.getLong(ID_INDEX);

                        datas.add(data);
                    }
                    return datas;
                } finally {
                    c.close();
                }
            }

            @Override
            public void deleteInner(ScheduleData data, SQLiteDatabase db) {
                db.delete(StoreBusiness.TABLE_SCHEDULE, StoreBusiness.ROW_ID + " = " + data.id, null);
            }

            @Override
            public void updateInner(ScheduleData data, SQLiteDatabase db) {
                db.update(StoreBusiness.TABLE_SCHEDULE, data.contentValues(), StoreBusiness.ROW_ID + " = " + data.id, null);
            }
        });
        mHelpers.put(PlayData.class, new AccessHelper<PlayData>() {
            private int ROW_NAME = -1;
            private int ROW_ACTORS = -1;
            private int ROW_DIRECTOR = -1;
            private int ROW_INTRO = -1;
            private int ROW_SCORE = -1;
            private int ROW_IMGPATH = -1;
            private int ROW_ID = -1;
            private int ROW_LENGTH = -1;

            @Override
            public long insertInner(PlayData data, SQLiteDatabase db) {
                return db.insert(StoreBusiness.TABLE_PLAY, null, data.contentValues());
            }

            @Override
            public List<PlayData> queryInner(String where, SQLiteDatabase db) {
                Cursor c = db.query(StoreBusiness.TABLE_PLAY, null, where, null, null, null, null);
                try {
                    if (ROW_NAME == -1) {
                        ROW_NAME = c.getColumnIndex(StoreBusiness.ROW_NAME);
                        ROW_ACTORS = c.getColumnIndex(StoreBusiness.ROW_ACTORS);
                        ROW_DIRECTOR = c.getColumnIndex(StoreBusiness.ROW_DIRECTOR);
                        ROW_INTRO = c.getColumnIndex(StoreBusiness.ROW_INTRO);
                        ROW_SCORE = c.getColumnIndex(StoreBusiness.ROW_SCORE);
                        ROW_IMGPATH = c.getColumnIndex(StoreBusiness.ROW_IMGPATH);
                        ROW_ID = c.getColumnIndex(StoreBusiness.ROW_ID);
                        ROW_LENGTH = c.getColumnIndex(StoreBusiness.ROW_LENGTH);
                    }

                    List<PlayData> datas = new ArrayList<>(c.getCount());
                    while (c.moveToNext()) {
                        PlayData data = new PlayData();
                        data.name = c.getString(ROW_NAME);
                        data.director = c.getString(ROW_DIRECTOR);
                        data.actors = c.getString(ROW_ACTORS);
                        data.intro = c.getString(ROW_INTRO);
                        data.score = c.getFloat(ROW_SCORE);
                        data.imgPath = c.getString(ROW_IMGPATH);
                        data.id = c.getLong(ROW_ID);
                        data.length = c.getInt(ROW_LENGTH);

                        datas.add(data);
                    }
                    return datas;
                } finally {
                    c.close();
                }
            }

            @Override
            public void deleteInner(PlayData data, SQLiteDatabase db) {
                db.delete(StoreBusiness.TABLE_PLAY, StoreBusiness.ROW_ID + " = " + data.id, null);
            }

            @Override
            public void updateInner(PlayData data, SQLiteDatabase db) {
                db.update(StoreBusiness.TABLE_PLAY, data.contentValues(), StoreBusiness.ROW_ID + " = " + data.id, null);
            }
        });
        mHelpers.put(StudioData.class, new AccessHelper<StudioData>() {
            private int ROW_NAME = -1;
            private int ROW_ROW = -1;
            private int ROW_COLUMN = -1;
            private int ROW_INVALID = -1;
            private int ROW_ID = -1;

            @Override
            public long insertInner(StudioData data, SQLiteDatabase db) {
                return db.insert(StoreBusiness.TABLE_STUDIO, null, data.contentValues());
            }

            @Override
            public List<StudioData> queryInner(String where, SQLiteDatabase db) {
                Cursor c = db.query(StoreBusiness.TABLE_STUDIO, null, where, null, null, null, null);
                try {
                    if (ROW_NAME == -1) {
                        ROW_NAME = c.getColumnIndex(StoreBusiness.ROW_NAME);
                        ROW_ROW = c.getColumnIndex(StoreBusiness.ROW_ROW);
                        ROW_COLUMN = c.getColumnIndex(StoreBusiness.ROW_COLUMN);
                        ROW_INVALID = c.getColumnIndex(StoreBusiness.ROW_INVALID);
                        ROW_ID = c.getColumnIndex(StoreBusiness.ROW_ID);
                    }

                    List<StudioData> datas = new ArrayList<>(c.getCount());
                    while (c.moveToNext()) {
                        StudioData data = new StudioData();
                        data.name = c.getString(ROW_NAME);
                        data.setRowCol(c.getInt(ROW_ROW), c.getInt(ROW_COLUMN));
                        data.id = c.getLong(ROW_ID);
                        String[] invalid = c.getString(ROW_INVALID).split(",");
                        if (invalid.length >= 2) {
                            for (int i = 0; i < invalid.length; i++) {
                                data.addInvalid(Integer.decode(invalid[i]), Integer.decode(invalid[++i]));
                            }
                        }

                        datas.add(data);
                    }
                    return datas;
                } finally {
                    c.close();
                }
            }

            @Override
            public void deleteInner(StudioData data, SQLiteDatabase db) {
                db.delete(StoreBusiness.TABLE_STUDIO, StoreBusiness.ROW_ID + " = " + data.id, null);
            }

            @Override
            public void updateInner(StudioData data, SQLiteDatabase db) {
                db.update(StoreBusiness.TABLE_STUDIO, data.contentValues(), StoreBusiness.ROW_ID + " = " + data.id, null);
            }
        });
    }

    public static <T extends Data> long insert(T data, SQLiteDatabase db) {
        AccessHelper<T> helper = mHelpers.get(data.getClass());
        return helper.insertInner(data, db);
    }

    public static <T extends Data> void delete(T data, SQLiteDatabase db) {
        AccessHelper<T> helper = mHelpers.get(data.getClass());
        helper.deleteInner(data, db);
    }

    public static <T extends Data> List<T> query(Class type, String where, SQLiteDatabase db) {
        AccessHelper<T> helper = mHelpers.get(type);
        return helper.queryInner(where, db);
    }

    public static <T extends Data> void update(T data, SQLiteDatabase db) {
        AccessHelper<T> helper = mHelpers.get(data.getClass());
        helper.updateInner(data, db);
    }

    public abstract long insertInner(T data, SQLiteDatabase db);

    public abstract List<T> queryInner(String where, SQLiteDatabase db);

    public abstract void deleteInner(T data, SQLiteDatabase db);

    public abstract void updateInner(T data, SQLiteDatabase db);
}

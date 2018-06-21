package com.example.a38938.ttms1.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.data.UserData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

/**
 * 目前主要进行线程切换
 */
public class StoreManager {
    private static StoreManager sInstance = null;

    private static final int INSERT_DATA = 0;
    private static final int GET_ALL = 1;
    public static final int DELETE_DATA = 2;
    public static final int UPDATE_DATA = 3;
    private static final int GET_IN_TIME = 4;
    private static final int LOGIN = 5;

    Context mAppContext = null;
    private StoreBusiness mBusiness;
    private Handler mHandler;
    private UserData mCurrentUser = null;

    private HandlerThread mThread = new HandlerThread("storemanager") {
        @SuppressLint("HandlerLeak")   //全局单例
        @Override
        protected void onLooperPrepared() {
            super.onLooperPrepared();
            mBusiness = new StoreBusiness();
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case INSERT_DATA: {
                            Data d = (Data) msg.obj;
                            d.id = mBusiness.insert(d);
                            break;
                        }
                        case GET_ALL: {
                            GetData d = (GetData) msg.obj;
                            List<Data> datas = mBusiness.query(d.type, null);
                            if (d.l.get() != null) {
                                d.l.get().onReceive(datas);
                            }
                            break;
                        }
                        case DELETE_DATA: {
                            Data d = (Data) msg.obj;
                            mBusiness.delete(d);
                            break;
                        }
                        case UPDATE_DATA: {
                            Data d = (Data) msg.obj;
                            mBusiness.update(d);
                            break;
                        }
                        case GET_IN_TIME: {
                            GetData d = (GetData) msg.obj;
                            List<Data> datas = mBusiness.query(d.type,
                                    StoreBusiness.ROW_START  + " between " + d.start + " and " + d.end +
                                            (d.play == -1 ? "" : (" and " + StoreBusiness.ROW_PLAY + " = " + d.play)));
                            if (d.l.get() != null) {
                                d.l.get().onReceive(datas);
                            }
                            break;
                        }
                        case LOGIN: {
                            GetData d = (GetData) msg.obj;
                            List<Data> datas = mBusiness.query(d.type,
                                    StoreBusiness.ROW_USR + " = \'" + d.usr + "\' and " +
                            StoreBusiness.ROW_PWD + " = \'" + d.pwd + "\'");
                            if (datas.size() != 0) {
                                mCurrentUser = (UserData) datas.get(0);
                            }

                            if (d.l.get() != null) {
                                d.l.get().onReceive(datas);
                            }
                            break;
                        }
                    }
                }
            };
        }
    };

    public static void init(Context c) {
        sInstance = new StoreManager(c);
    }

    private StoreManager(Context c) {
        mAppContext = c.getApplicationContext();
        mThread.start();
    }

    public static StoreManager get() {
        return sInstance;
    }

    public void insert(Data data) {
        mHandler.sendMessage(mHandler.obtainMessage(INSERT_DATA, data));
    }

    public long insertDirectly(Data data) {
        return mBusiness.insert(data);
    }

    public List<ScheduleData> searchScheduleByPlay(int playId) {
        return mBusiness.query(ScheduleData.class, StoreBusiness.ROW_PLAY + " = " + playId);
    }

    public List<PlayData> getPlays() {
        return mBusiness.query(PlayData.class, null);
    }

    public <T extends Data> List<T> getAllDirectly(Class type) {
        return mBusiness.query(type, null);
    }

    public <T extends Data> void getAllDatas(Class type, OnDataGetListener<T> l) {
        mHandler.sendMessage(mHandler.obtainMessage(GET_ALL, new GetData(type, l)));
    }

    public void getSchedules(long play, long start, long end, OnDataGetListener<ScheduleData> l) {
        GetData gd = new GetData(ScheduleData.class, l);
        gd.start = start;
        gd.end = end;
        gd.play = play;
        mHandler.sendMessage(mHandler.obtainMessage(GET_IN_TIME, gd));
    }

    public void getSchedules(long start, long end, OnDataGetListener<ScheduleData> l) {
        GetData gd = new GetData(ScheduleData.class, l);
        gd.start = start;
        gd.end = end;
        gd.play = -1;
        mHandler.sendMessage(mHandler.obtainMessage(GET_IN_TIME, gd));
    }

    public List<ScheduleData> getSchedulesDirectly(long start, long end) {
        return mBusiness.query(ScheduleData.class,
                StoreBusiness.ROW_START  + " between " + start + " and " + end);
    }

    public List<ScheduleData> getSchedulesDirectly(long start, long end, long play) {
        return mBusiness.query(ScheduleData.class,
                StoreBusiness.ROW_START  + " between " + start + " and " + end +
                        (" and " + StoreBusiness.ROW_PLAY + " = " + play));
    }

    public void delete(Data data) {
        mHandler.sendMessage(mHandler.obtainMessage(DELETE_DATA, data));
    }

    public void update(Data data) {
        mHandler.sendMessage(mHandler.obtainMessage(UPDATE_DATA, data));
    }

    public boolean isLoged() {
        return mCurrentUser != null;
    }

    public boolean hasPermission(int action) {
        if (mCurrentUser == null) {
            return false;
        }

        return true;
    }

    public void login(String usr, String pwd, OnDataGetListener<UserData> l) {
        GetData gd = new GetData(UserData.class, l);
        gd.usr = usr;
        gd.pwd = pwd;
        mHandler.sendMessage(mHandler.obtainMessage(LOGIN, gd));
    }

    public String loginDirectly(String usr, String pwd) {
        List<Data> datas = mBusiness.query(UserData.class,
                StoreBusiness.ROW_USR + " = \'" + usr + "\' and " +
                        StoreBusiness.ROW_PWD + " = \'" + pwd + "\'");
        if (datas.size() != 0) {
            return "user";
        }

        return "no user";
    }

    void cachePlay(long id) {
        mBusiness.query(PlayData.class, StoreBusiness.ROW_ID + " = " + id);
    }

    void cacheStudio(long id) {
        mBusiness.query(StudioData.class, StoreBusiness.ROW_ID + " = " + id);
    }

    static class GetData {
        Class type;
        long start;
        long end;
        long play;
        String usr, pwd;
        WeakReference<OnDataGetListener> l;
        public GetData(Class t, OnDataGetListener l) {
            type = t;
            this.l = new WeakReference<OnDataGetListener>(l);
        }
    }
}

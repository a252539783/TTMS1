package com.example.a38938.ttms1.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;

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

    Context mAppContext = null;
    private StoreBusiness mBusiness;
    private Handler mHandler;
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

    public List<ScheduleData> searchScheduleByPlay(int playId) {
        return mBusiness.query(ScheduleData.class, StoreBusiness.ROW_PLAY + " = " + playId);
    }

    public List<PlayData> getPlays() {
        return mBusiness.query(PlayData.class, null);
    }

    public <T extends Data> void getAllDatas(Class type, OnDataGetListener<T> l) {
        mHandler.sendMessage(mHandler.obtainMessage(GET_ALL, new GetData(type, l)));
    }

    public void delete(Data data) {
        mHandler.sendMessage(mHandler.obtainMessage(DELETE_DATA, data));
    }

    public void update(Data data) {
        mHandler.sendMessage(mHandler.obtainMessage(UPDATE_DATA, data));
    }

    static class GetData {
        Class type;
        WeakReference<OnDataGetListener> l;
        public GetData(Class t, OnDataGetListener l) {
            type = t;
            this.l = new WeakReference<OnDataGetListener>(l);
        }
    }
}

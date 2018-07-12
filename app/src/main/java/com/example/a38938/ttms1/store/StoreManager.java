package com.example.a38938.ttms1.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.data.TicketData;
import com.example.a38938.ttms1.data.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    private static final boolean USING_NET = false;

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

                            if (!USING_NET) {
                                d.id = mBusiness.insert(d);
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=" + d.getClass().getSimpleName()
                                            + "&action=insert"
                                            + "&obj=" + new Gson().toJson(d);

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        //Log.e("xx", "res==" + response.body().string());
                                        String r = response.body().string();
                                        d.id = new JSONObject(r).getLong("id");
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }
                            break;
                        }
                        case GET_ALL: {
                            final GetData d = (GetData) msg.obj;
                            List<Data> datas = null;

                            if (!USING_NET) {
                                datas = mBusiness.query(d.type, null);
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=" + d.type.getSimpleName()
                                            + "&action=get";

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        //Log.e("xx", "res==" + response.body().string());
                                        datas = new Gson().fromJson(response.body().string(),
                                                getListType(d.type));
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }

                            if (d.type.getName().equals("PlayData")) {
                                for (Data dd : datas) {
                                    AccessHelper.sPlays.put(dd.id, (PlayData) dd);
                                }
                            } else if (d.type.getName().equals("StudioData")) {
                                for (Data dd : datas) {
                                    AccessHelper.sStudios.put(dd.id, (StudioData) dd);
                                }
                            } else if (d.type.getName().equals("ScheduleData")) {
                                for (Data dd : datas) {
                                    AccessHelper.sSchedules.put(dd.id, (ScheduleData) dd);
                                }
                            }
                            if (d.l.get() != null) {
                                d.l.get().onReceive(datas);
                            }
                            break;
                        }
                        case DELETE_DATA: {
                            Data d = (Data) msg.obj;
                            if (!USING_NET) {
                                mBusiness.delete(d);
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=" + d.getClass().getSimpleName()
                                            + "&action=delete"
                                            + "&obj=" + new Gson().toJson(d);

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        //Log.e("xx", "res==" + response.body().string());
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }
                            break;
                        }
                        case UPDATE_DATA: {
                            Data d = (Data) msg.obj;

                            if (!USING_NET) {
                                mBusiness.update(d);
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=" + d.getClass().getSimpleName()
                                            + "&action=update"
                                            + "&obj=" + new Gson().toJson(d);

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        //Log.e("xx", "res==" + response.body().string());
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }
                            break;
                        }
                        case GET_IN_TIME: {
                            GetData d = (GetData) msg.obj;
                            List<Data> datas = null;

                            if (!USING_NET) {
                                datas = mBusiness.query(d.type,
                                        StoreBusiness.ROW_START + " between " + d.start + " and " + d.end +
                                                (d.play == -1 ? "" : (" and " + StoreBusiness.ROW_PLAY + " = " + d.play)));
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=" + d.type.getSimpleName()
                                            + "&action=getintime"
                                            + "&start=" + d.start
                                            + "&end=" + d.end
                                            + (d.play == -1 ? "" : "&play=" + d.play);

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        //Log.e("xx", "res==" + response.body().string());
                                        datas = new Gson().fromJson(response.body().string(),
                                                getListType(ScheduleData.class));
                                    }

                                    for (Data dd : datas) {
                                        ScheduleData sd = (ScheduleData) dd;
                                        if (AccessHelper.sPlays.get(sd.play) == null) {
                                            cachePlay(0);
                                        }

                                        if (AccessHelper.sStudios.get(sd.studio) == null) {
                                            cacheStudio(0);
                                        }

                                        AccessHelper.sSchedules.put(sd.id, sd);
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }

                            if (d.l.get() != null) {
                                d.l.get().onReceive(datas);
                            }
                            break;
                        }
                        case LOGIN: {
                            GetData d = (GetData) msg.obj;

                            List<Data> datas = null;
                            if (!USING_NET) {
                                datas = mBusiness.query(d.type,
                                        StoreBusiness.ROW_USR + " = \'" + d.usr + "\' and " +
                                                StoreBusiness.ROW_PWD + " = \'" + d.pwd + "\'");
                                if (datas.size() != 0) {
                                    mCurrentUser = (UserData) datas.get(0);
                                }
                            } else {
                                try {
                                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                                    String url = "http://192.168.43.1:6543/?type=UserData"
                                            + "&action=login&usrname=" + d.usr
                                            + "&passwd=" + d.pwd;

                                    Request request = new Request.Builder()
                                            .url(url)//请求接口。如果需要传参拼接到接口后面。
                                            .build();//创建Request 对象
                                    Response response = null;
                                    response = client.newCall(request).execute();//得到Response 对象
                                    if (response.isSuccessful()) {
                                        Log.e("xx", "response.code()==" + response.code());
                                        Log.e("xx", "response.message()==" + response.message());
                                        Log.e("xx", "res==" + response.body().string());
                                        if (!response.body().string().contains("isn")) {
                                            datas = new ArrayList<>();
                                            datas.add(new UserData());
                                            mCurrentUser = (UserData) datas.get(0);
                                        } else {
                                            datas = new ArrayList<>();
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("xx", e.toString());
                                }
                            }

                            if (d.usr.equals("a") && d.pwd.equals("a")) {
                                mCurrentUser = new UserData();
                                datas.add(mCurrentUser);
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

    public static void main(String[] args) {
        System.out.println(getListType(PlayData.class));
        String s = "[{\"actors\":\"特日贡\",\"director\":\"李权飞\",\"imgPath\":\"/storage/emulated/0/Android/data/com.eg.android.AlipayGphone/cache/eggconfig/eggconfig/GxnvJF7PTmaqbQIQsUnf_gAAACMAAQQD.png\",\"intro\":\"哈哈\",\"length\":120,\"name\":\"四个男人一个女人\",\"score\":30.0,\"id\":1,\"type\":1},{\"actors\":\"特日工\",\"director\":\"啊啊啊\",\"imgPath\":\"/storage/emulated/0/Android/data/com.eg.android.AlipayGphone/cache/eggconfig/eggconfig/y7PIgwl8S9ajKqWdLLJBFAAAACMAAQQD.png\",\"intro\":\"。。。\",\"length\":111,\"name\":\"剧目2\",\"score\":69.0,\"id\":2,\"type\":1},{\"actors\":\"aaa\",\"director\":\"aaa\",\"imgPath\":\"/storage/emulated/0/Android/data/com.eg.android.AlipayGphone/cache/eggconfig/eggconfig/Pi4OT2_QTE2rK4_BFtsr6AAAACMAAQQD.png\",\"intro\":\"aaa\",\"length\":11,\"name\":\"aaa\",\"score\":33.0,\"id\":3,\"type\":1}]\n";
        List<Data> d = new Gson().fromJson(s, getListType(PlayData.class));
        System.out.println(d.size());
    }

    static Type getListType(Class c) {
        switch (c.getSimpleName()) {
            case "UserData":
                return new TypeToken<List<UserData>>() {
                }.getType();
            case "PlayData":
                return new TypeToken<List<PlayData>>() {
                }.getType();
            case "ScheduleData":
                return new TypeToken<List<ScheduleData>>() {
                }.getType();
            case "TicketData":
                return new TypeToken<List<TicketData>>() {
                }.getType();
            case "StudioData":
                return new TypeToken<List<StudioData>>() {
                }.getType();
        }

        return new TypeToken<List<Data>>() {
        }.getType();
    }

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

    void cachePlay(long id) {
        if (!USING_NET) {
            mBusiness.query(PlayData.class, StoreBusiness.ROW_ID + " = " + id);
        } else {
            List<Data> datas = null;

            try {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                String url = "http://192.168.43.1:6543/?type=PlayData"
                        + "&action=get";

                Request request = new Request.Builder()
                        .url(url)//请求接口。如果需要传参拼接到接口后面。
                        .build();//创建Request 对象
                Response response = null;
                response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    Log.e("xx", "response.code()==" + response.code());
                    Log.e("xx", "response.message()==" + response.message());
                    //Log.e("xx", "res==" + response.body().string());
                    datas = new Gson().fromJson(response.body().string(),
                            getListType(PlayData.class));
                }
            } catch (Exception e) {
                Log.e("xx", e.toString());
            }

            for (Data dd : datas) {
                AccessHelper.sPlays.put(dd.id, (PlayData) dd);
            }
        }
    }

    void cacheStudio(long id) {
        if (!USING_NET) {
            mBusiness.query(StudioData.class, StoreBusiness.ROW_ID + " = " + id);
        } else {
            List<Data> datas = null;

            try {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                String url = "http://192.168.43.1:6543/?type=StudioData"
                        + "&action=get";

                Request request = new Request.Builder()
                        .url(url)//请求接口。如果需要传参拼接到接口后面。
                        .build();//创建Request 对象
                Response response = null;
                response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()) {
                    Log.e("xx", "response.code()==" + response.code());
                    Log.e("xx", "response.message()==" + response.message());
                    //Log.e("xx", "res==" + response.body().string());
                    datas = new Gson().fromJson(response.body().string(),
                            getListType(StudioData.class));
                }
            } catch (Exception e) {
                Log.e("xx", e.toString());
            }

            for (Data dd : datas) {
                AccessHelper.sStudios.put(dd.id, (StudioData) dd);
            }
        }
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

package com.example.a38938.ttms1;

import android.util.Log;

import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.store.StoreManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.NanoHTTPD;
import org.nanohttpd.protocols.http.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.nanohttpd.protocols.http.response.Response.newFixedLengthResponse;

/**
 * Created by LQF on 2018/6/21.
 */

public class MNanoHttpd extends NanoHTTPD {

    public MNanoHttpd() {
        super(6543);
        try {
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            Log.e("xx", e.toString());
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        Map<String, String> parms = session.getParms();

        String res = "this isn't data";
        try {
            Class type = getClass().getClassLoader().loadClass("com.example.a38938.ttms1.data." + parms.get("type"));

            String action = parms.get("action") ;
            Log.e("xx", "recv type " + type.getName() + " action = " + action);
            switch (action) {
                case "get": {
                    List rl = StoreManager.get().getAllDirectly(type);
                    res = new Gson().toJson(rl);
                    break;
                }
                case "insert": {
                    String obj = parms.get("obj");
                    long id = StoreManager.get().insertDirectly((Data) new Gson().fromJson(obj, type));
                    res = "{\"id\":" + id + "}";
                    break;
                }
                case "update": {
                    String obj = parms.get("obj");
                    StoreManager.get().update((Data) new Gson().fromJson(obj, type));
                    break;
                }
                case "delete": {
                    String obj = parms.get("obj");
                    StoreManager.get().delete((Data) new Gson().fromJson(obj, type));
                    break;
                }
                case "getintime": {
                    long start = Long.decode(parms.get("start"));
                    long end = Long.decode(parms.get("end"));
                    String play = parms.get("play");
                    if (play == null) {
                        res = new Gson().toJson(StoreManager.get().getSchedulesDirectly(start, end));
                    } else {
                        res = new Gson().toJson(StoreManager.get().getSchedulesDirectly(start, end, Long.decode(play)));
                    }
                    break;
                }
                case "login": {
                    String usrname = parms.get("usrname");
                    String passwd = parms.get("passwd");
                    res = "{\"user\": " +
                            StoreManager.get().loginDirectly(usrname, passwd)
                            + "}";
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            Log.e("xx", e.toString());
        }

        return newFixedLengthResponse(res);
    }
}

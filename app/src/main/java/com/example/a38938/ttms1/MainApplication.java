package com.example.a38938.ttms1;

import android.app.Application;

import com.example.a38938.ttms1.store.StoreManager;

/**
 * Created by LQF on 2018/5/28.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StoreManager.init(this);
    }
}

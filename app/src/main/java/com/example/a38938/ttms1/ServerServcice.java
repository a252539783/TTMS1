package com.example.a38938.ttms1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by LQF on 2018/6/21.
 */

public class ServerServcice extends Service {

    private MNanoHttpd mHttpd = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mHttpd == null) {
            mHttpd = new MNanoHttpd();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHttpd.stop();
    }
}

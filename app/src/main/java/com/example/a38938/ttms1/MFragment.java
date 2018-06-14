package com.example.a38938.ttms1;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by LQF on 2018/5/29.
 */

public class MFragment extends Fragment {

    private MFragment mHost = null;

    public boolean onBackPressed() {
        return false;
    }

    public void setHost(MFragment m) {
        mHost = m;
    }

    @Override
    public Context getContext() {
        if (mHost == null) {
            return super.getContext();
        }

        return mHost.getContext();
    }
}

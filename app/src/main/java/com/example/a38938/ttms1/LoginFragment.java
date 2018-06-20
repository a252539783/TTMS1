package com.example.a38938.ttms1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.a38938.ttms1.data.UserData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreManager;

import java.util.List;

/**
 * Created by LQF on 2018/6/14.
 */

public class LoginFragment extends MFragment {

    private ViewGroup mView = null;

    private EditText mUsrname, mPasswd;
    private Button mLogin;

    private OnDataGetListener<UserData> mListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.login_activity, container, false);
        }

        init();
        return mView;
    }

    void setListener (OnDataGetListener l) {
        mListener = l;
    }

    private void init() {
        if (mLogin == null) {
            mLogin = mView.findViewById(R.id.login);
            mUsrname = mView.findViewById(R.id.loginAccount_id);
            mPasswd = mView.findViewById(R.id.password_id);
            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StoreManager.get().login(mUsrname.getText().toString(), mPasswd.getText().toString(),
                            mListener);
                }
            });
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(null);
    }
}

package com.example.a38938.ttms1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a38938.ttms1.Adapter.UsrAdapter;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.UserData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreManager;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

/**
 * Created by LQF on 2018/6/14.
 */

public class UserFragment extends MFragment implements View.OnClickListener {

    private ViewGroup mView = null;
    private ViewGroup mActionBar = null;

    private RecyclerView mList = null;
    private UsrAdapter mAdapter = new UsrAdapter(this);
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private OnDataGetListener<UserData> mGetListener = new OnDataGetListener<UserData>() {
        @Override
        public void onReceive(final List<UserData> datas) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(datas);
                }
            });
        }
    };

    public void showDelete() {
        mAddButton.setVisibility(View.GONE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mSelectButton.setVisibility(View.VISIBLE);
    }

    public void hideDelete() {
        mAddButton.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.GONE);
        mSelectButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onBackPressed() {
        if (mAdapter.selecting()) {
            mAdapter.cancelSelect();
            return true;
        }

        return false;
    }

    private AlertDialog mDeleteDialog;
    private void showDeleteDialog() {
        if (mDeleteDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            mDeleteDialog = builder.setTitle("确认是否删除?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteDialog.dismiss();
                            mAdapter.delete();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDeleteDialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .show();
        } else {
            mDeleteDialog.show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.list, container, false);
            mActionBar = (ViewGroup)inflater.inflate(R.layout.play_manage_actionbar, container, false);
        }

        initView();
        return mView;
    }

    private UserData mCurrentEditData = null;

    private ImageView mDeleteButton, mSelectButton, mAddButton;
    private void initView() {
        if (mList == null) {
            mList = mView.findViewById(R.id.list_list);
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
            mList.setAdapter(mAdapter);
            mDeleteButton = mActionBar.findViewById(R.id.play_action_delete);
            mSelectButton = mActionBar.findViewById(R.id.play_action_select);
            mAddButton = mActionBar.findViewById(R.id.play_action_add);
            mAddButton.setOnClickListener(this);
            mSelectButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setCustomView(mActionBar);
        StoreManager.get().getAllDatas(UserData.class, mGetListener);
    }

    private AppCompatDialog mDialog;

    private EditText mEditName, mEditUsrname, mEditPasswd;
    private TextView mIdText;
    private Button mSave;
    public void edit(UserData data) {
        if (mDialog == null) {
            mDialog = new AppCompatDialog(getContext());
            mDialog.setContentView(R.layout.user_edit);

            mEditUsrname = mDialog.findViewById(R.id.user_edit_e_usrname);
            mEditName = mDialog.findViewById(R.id.user_edit_e_name);
            mEditPasswd = mDialog.findViewById(R.id.user_edit_e_passwd);
            mIdText = mDialog.findViewById(R.id.user_edit_id);
            mSave = mDialog.findViewById(R.id.user_edit_save);
            mSave.setOnClickListener(this);
        }

        mCurrentEditData = data;
        if (data.id != -1) {
            mEditName.setText(data.name);
            mEditPasswd.setText(data.passwd);
            mEditUsrname.setText(data.usrname);
            mIdText.setText("" + data.id);
        } else {
            mIdText.setText("");
        }

        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            edit(new UserData());
        } else if (v == mDeleteButton) {
            showDeleteDialog();
        } else if (v == mSelectButton) {
            mAdapter.selectAll();
        } else if (v == mSave) {
            mDialog.dismiss();
            mCurrentEditData.name = mEditName.getText().toString();
            mCurrentEditData.usrname = mEditUsrname.getText().toString();
            mCurrentEditData.passwd = mEditPasswd.getText().toString();

            if (mCurrentEditData.id == -1) {
                StoreManager.get().insert(mCurrentEditData);
                mAdapter.addData(mCurrentEditData);
                mAdapter.notifyDataSetChanged();
            } else {
                StoreManager.get().update(mCurrentEditData);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}

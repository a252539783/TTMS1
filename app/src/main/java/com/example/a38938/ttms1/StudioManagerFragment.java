package com.example.a38938.ttms1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a38938.ttms1.Adapter.StudioManageAdapter;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreManager;
import com.example.a38938.ttms1.view.SeatView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public class StudioManagerFragment extends MFragment implements View.OnClickListener {

    private ViewGroup mView = null;

    private RecyclerView mList;
    private StudioManageAdapter mAdapter;
    private ViewGroup mActionBar = null;

    private ImageView mDeleteButton, mSelectButton, mAddButton;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<StudioData> datas = (List<StudioData>) msg.obj;
            mAdapter.setData(datas);
        }
    };
    private OnDataGetListener<StudioData> mGetListener = new OnDataGetListener<StudioData>() {
        @Override
        public void onReceive(List<StudioData> datas) {
            mHandler.sendMessage(mHandler.obtainMessage(0, datas));
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.studio_manage, container, false);
            mActionBar = (ViewGroup) inflater.inflate(R.layout.play_manage_actionbar, container, false);
        }

        initView();
        return mView;
    }

    private void initView() {
        if (mList == null) {
            mList = mView.findViewById(R.id.studio_manage_list);
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
            mList.setAdapter(mAdapter = new StudioManageAdapter(this));
            mDeleteButton = mActionBar.findViewById(R.id.play_action_delete);
            mSelectButton = mActionBar.findViewById(R.id.play_action_select);
            mAddButton = mActionBar.findViewById(R.id.play_action_add);
            mAddButton.setOnClickListener(this);
            mSelectButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(mActionBar);
        StoreManager.get().getAllDatas(StudioData.class, mGetListener);
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            showSeatManageDialog(v, new StudioData());
        } else if (v == mDeleteButton) {
        } else if (v == mSelectButton) {
        } else if (v == mAddRowButton) {
            mCurrentStudio.setRowCol(mCurrentStudio.rows() + 1, mCurrentStudio.columns());
            mSeatView.setData(mCurrentStudio.rows(), mCurrentStudio.columns());
        } else if (v == mAddColButton) {
            mCurrentStudio.setRowCol(mCurrentStudio.rows(), mCurrentStudio.columns() + 1);
            mSeatView.setData(mCurrentStudio.rows(), mCurrentStudio.columns());
        } else if (v == mMinRowButton) {
            mCurrentStudio.setRowCol(mCurrentStudio.rows() - 1, mCurrentStudio.columns());
            mSeatView.setData(mCurrentStudio.rows(), mCurrentStudio.columns());
        } else if (v == mMinColButton) {
            mCurrentStudio.setRowCol(mCurrentStudio.rows(), mCurrentStudio.columns() - 1);
            mSeatView.setData(mCurrentStudio.rows(), mCurrentStudio.columns());
        } else if (v == mSaveButton) {
            if (mCurrentStudio.id == -1) {
                StoreManager.get().insert(mCurrentStudio);
                mAdapter.addData(mCurrentStudio);
            } else {
                StoreManager.get().update(mCurrentStudio);
                mAdapter.setData(mCurrentStudio);
                mAdapter.notifyDataSetChanged();
            }
            mSeatDialog.dismiss();
        }
    }

    private Button mAddRowButton, mAddColButton, mMinRowButton, mMinColButton, mSaveButton;
    private AppCompatDialog mSeatDialog;
    private SeatView mSeatView;
    private StudioData mCurrentStudio;

    public void showSeatManageDialog(View v, StudioData data) {
        mCurrentStudio = data;
        if (mSeatDialog == null) {
            mSeatDialog = new AppCompatDialog(v.getContext());
            mSeatDialog.setContentView(R.layout.seat_manage);
            mSeatDialog.setCancelable(true);
            mSeatDialog.setCanceledOnTouchOutside(true);

            mAddRowButton = mSeatDialog.findViewById(R.id.seat_manage_b_addrow);
            mAddColButton = mSeatDialog.findViewById(R.id.seat_manage_b_addcol);
            mMinColButton = mSeatDialog.findViewById(R.id.seat_manage_b_mincol);
            mMinRowButton = mSeatDialog.findViewById(R.id.seat_manage_b_minrow);
            mSaveButton = mSeatDialog.findViewById(R.id.seat_manage_b_save);
            mAddRowButton.setOnClickListener(this);
            mMinColButton.setOnClickListener(this);
            mMinRowButton.setOnClickListener(this);
            mAddColButton.setOnClickListener(this);
            mSaveButton.setOnClickListener(this);
            mSeatView = mSeatDialog.findViewById(R.id.seat_manage_view);
            mSeatView.setSeatChecker(new SeatView.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return true;
                }

                @Override
                public boolean isSold(int row, int column) {
                    return !mCurrentStudio.isValid(row, column);
                }

                @Override
                public void checked(int row, int column) {
                    mCurrentStudio.addInvalid(row, column);
                    mSeatView.invalidate();
                }

                @Override
                public void unCheck(int row, int column) {
                    mCurrentStudio.addInvalid(row, column);
                    mSeatView.invalidate();
                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return new String[] {""};
                }
            });
        }

        mSeatDialog.show();
        mSeatView.setManage(true);
        mSeatView.setData(data.rows(), data.columns());
    }
}

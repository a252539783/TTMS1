package com.example.a38938.ttms1;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.a38938.ttms1.Adapter.PlayManageAdapter;
import com.example.a38938.ttms1.Adapter.RecentImgAdapter;
import com.example.a38938.ttms1.Adapter.ScheduleListAdapter;
import com.example.a38938.ttms1.Adapter.Selector;
import com.example.a38938.ttms1.Adapter.StudioManageAdapter;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.store.OnDataGetListener;
import com.example.a38938.ttms1.store.StoreManager;
import com.example.a38938.ttms1.tools.TimeUtil;
import com.example.a38938.ttms1.view.ScheduleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

/**
 * Created by LQF on 2018/5/31.
 */

public class ScheduleManageFragment extends MFragment implements View.OnClickListener {

    private ViewGroup mView;
    private ViewGroup mActionBar = null;

    private Calendar mCalendar = Calendar.getInstance();
    private int year, month, day;
    private Selector<ScheduleData> mSelector = null;

    @SuppressLint("ValidFragment")
    ScheduleManageFragment(){
        super();
    }

    @SuppressLint("ValidFragment")
    ScheduleManageFragment(Selector<ScheduleData> s) {
        setSelect(s);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (ViewGroup) inflater.inflate(R.layout.schedule_manage, container, false);
            mActionBar = (ViewGroup) inflater.inflate(R.layout.play_manage_actionbar, container, false);
            if (mSelector == null) {
                mAdapter = new ScheduleListAdapter(this);
            } else {
                mAdapter = new ScheduleListAdapter(mSelector);
            }
        }

        init();
        return mView;
    }

    public void setSelect(Selector<ScheduleData> s) {
        mSelector = s;
    }

    //    private ScheduleView mScheduleView;
    private RecyclerView mScheduleList;
    private ScheduleListAdapter mAdapter = null;
    private ViewPager mPager = null;
    private RecentImgAdapter mPlayImgAdapter = new RecentImgAdapter();
    private PagerContainer mPagerContainer = null;

    private CoverFlow mCover = null;

    private ImageView mDeleteButton, mSelectButton, mAddButton;
    private TextView mPreviousText, mNextText, mDayText;

    private OnDataGetListener<ScheduleData> mGetScheduleListener = new OnDataGetListener<ScheduleData>() {
        @Override
        public void onReceive(final List<ScheduleData> datas) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(datas);
                }
            });
        }
    };

    private OnDataGetListener<ScheduleData> mPreGetScheduleListener = new OnDataGetListener<ScheduleData>() {
        @Override
        public void onReceive(List<ScheduleData> datas) {
            final ArrayList<PlayData> plays = new ArrayList<>();
            HashSet<Long> playSet = new HashSet<>();
            for (ScheduleData d : datas) {
                if (!playSet.contains(d.play)) {
                    plays.add(d.getPlay());
                }
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPlayImgAdapter.setData(plays);
                }
            });
            if (plays.size() != 0) {
                StoreManager.get().getSchedules(plays.get(0).id,
                        TimeUtil.getTime(year, month, day, 0, 0),
                        TimeUtil.getTime(year, month, day, 23, 59), mGetScheduleListener);
            } else {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(new ArrayList<ScheduleData>());
                    }
                });
            }
        }
    };

    private OnDataGetListener<PlayData> mGetPlayImgListener = new OnDataGetListener<PlayData>() {
        @Override
        public void onReceive(final List<PlayData> datas) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPlayImgAdapter.setData(datas);
                }
            });
            if (datas.size() != 0) {
                StoreManager.get().getSchedules(datas.get(0).id,
                        TimeUtil.getTime(year, month, day, 0, 0),
                        TimeUtil.getTime(year, month, day, 23, 59), mGetScheduleListener);
            }
        }
    };

    private void init() {
        if (mScheduleList == null) {
            mPreviousText = mView.findViewById(R.id.schedule_t_previous);
            mNextText = mView.findViewById(R.id.schedule_t_next);
            mPreviousText.setOnClickListener(this);
            mNextText.setOnClickListener(this);
            mDayText = mView.findViewById(R.id.schedule_t_day);
            mDayText.setOnClickListener(this);
            mDeleteButton = mActionBar.findViewById(R.id.play_action_delete);
            mSelectButton = mActionBar.findViewById(R.id.play_action_select);
            mAddButton = mActionBar.findViewById(R.id.play_action_add);
            mAddButton.setOnClickListener(this);
            mSelectButton.setOnClickListener(this);
            mDeleteButton.setOnClickListener(this);
            mScheduleList = mView.findViewById(R.id.schedule_list);
            mScheduleList.setLayoutManager(new LinearLayoutManager(getContext()));
            mScheduleList.setAdapter(mAdapter);
            mPager = mView.findViewById(R.id.schedule_pager);
            mPagerContainer = mView.findViewById(R.id.schedule_pager_container);
            mCover = new CoverFlow.Builder()
                    .with(mPager)
                    .pagerMargin(0f)
                    .scale(0.3f)
                    .spaceSize(0f)
                    .rotationY(0f)
                    .build();
            mPager.setOffscreenPageLimit(5);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mAdapter.cancelSelect();
                    StoreManager.get().getSchedules(mPlayImgAdapter.getData().get(position).id,
                            TimeUtil.getTime(year, month, day, 0, 0),
                            TimeUtil.getTime(year, month, day, 23, 59), mGetScheduleListener);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mPlayImgAdapter = new RecentImgAdapter();
            mPager.setAdapter(mPlayImgAdapter);
        }
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(mActionBar);
        } catch (Exception e){}
        updateTime();
        //StoreManager.get().getAllDatas(PlayData.class, mGetPlayImgListener);
    }

    private ScheduleData mCurrentData = null;
    private AppCompatDialog mDialog;

    private TextView mSelectPlay, mSelectStudio;
    private EditText mPriceEdit;
    private Button mSaveButton;
    private Spinner mStartHour, mStartMin, mEndHour, mEndMin;

    public void showEditDialog(ScheduleData data) {
        if (mDialog == null) {
            mDialog = new AppCompatDialog(getContext());
            mDialog.setContentView(R.layout.schedule_modify);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(true);

            mPriceEdit = mDialog.findViewById(R.id.schedule_edit_e_price);
            mSelectPlay = mDialog.findViewById(R.id.schedule_edit_t_play);
            mSelectStudio = mDialog.findViewById(R.id.schedule_edit_t_studio);
            mSaveButton = mDialog.findViewById(R.id.schedule_edit_b_save);
            mStartHour = mDialog.findViewById(R.id.schedule_edit_select_start_h);
            mStartMin = mDialog.findViewById(R.id.schedule_edit_select_start_m);
            mEndHour = mDialog.findViewById(R.id.schedule_edit_select_end_h);
            mEndMin = mDialog.findViewById(R.id.schedule_edit_select_end_m);
            mSelectStudio.setOnClickListener(this);
            mSelectPlay.setOnClickListener(this);
            mSaveButton.setOnClickListener(this);
        }
        mCurrentData = data;

        if (mCurrentData.id != -1) {
            mSelectPlay.setText(mCurrentData.getPlay().name);
            mSelectStudio.setText(mCurrentData.getStudio().id + "号厅");
            mPriceEdit.setText(mCurrentData.price + "");
            String time = TimeUtil.getHour(mCurrentData.start);
            int hour = Integer.decode(time.split(":")[0]);
            int min = Integer.decode(time.split(":")[1]);
            mStartHour.setSelection(hour);
            mStartMin.setSelection(min);
            time = TimeUtil.getHour(mCurrentData.end);
            hour = Integer.decode(time.split(":")[0]);
            min = Integer.decode(time.split(":")[1]);
            mEndHour.setSelection(hour);
            mEndMin.setSelection(min);
        }
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == mSelectPlay) {
            selectPlay();
        } else if (v == mSelectStudio) {
            selectStudio();
        } else if (v == mAddButton) {
            ScheduleData sd = mCurrentData == null ? new ScheduleData() : mCurrentData.copy();
            sd.id = -1;
            showEditDialog(sd);
        } else if (v == mDeleteButton) {
            showDeleteDialog();
        } else if (v == mSelectButton) {
            mAdapter.selectAll();
        } else if (v == mSaveButton) {
            mDialog.dismiss();
            try {
                mCurrentData.price = Float.valueOf(mPriceEdit.getText().toString());
            }catch (Exception e) {
                mCurrentData.price = 0;
            }
            mCurrentData.start = TimeUtil.getTime(year, month, day, Integer.decode((String)mStartHour.getSelectedItem()), Integer.decode((String)mStartMin.getSelectedItem()));
            mCurrentData.end = TimeUtil.getTime(year, month, day, Integer.decode((String)mEndHour.getSelectedItem()), Integer.decode((String)mEndMin.getSelectedItem()));
            if (mCurrentData.id == -1) {
                StoreManager.get().insert(mCurrentData);
                updateTime();
            } else {
                StoreManager.get().update(mCurrentData);
                mAdapter.setData(mCurrentData);
            }
        } else if (v == mDayText) {
            new DatePickerDialog(getContext(), onDateSetListener, year, month, day).show();
        } else if (v == mNextText) {
            mCalendar.roll(Calendar.DAY_OF_MONTH, true);
            updateTime();
        } else if (v == mPreviousText) {
            mCalendar.roll(Calendar.DATE, false);
            updateTime();
        }
    }

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

    @Override
    public boolean onBackPressed() {
        if (mAdapter.selecting()) {
            mAdapter.cancelSelect();
            return true;
        }

        return false;
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mCalendar.set(year, monthOfYear, dayOfMonth);
            updateTime();
        }
    };

    public void updateTime() {
        year = mCalendar.get(Calendar.YEAR);
        month = mCalendar.get(Calendar.MONTH) + 1;
        day = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDayText.setText(String.format("%04d-%02d-%02d", year, month, day));
        mAdapter.cancelSelect();
        StoreManager.get().getSchedules(
                TimeUtil.getTime(year, month, day, 0, 0),
                TimeUtil.getTime(year, month, day, 23, 59), mPreGetScheduleListener);
    }

    private AppCompatDialog mListDialog;
    private RecyclerView mList;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                List<PlayData> datas = (List<PlayData>) msg.obj;
                mPlayAdapter.setData(datas);
            } else {
                List<StudioData> datas = (List<StudioData>) msg.obj;
                mStudioAdapter.setData(datas);
            }
        }
    };
    private OnDataGetListener<PlayData> mGetPlayListener = new OnDataGetListener<PlayData>() {
        @Override
        public void onReceive(List<PlayData> datas) {
            mHandler.sendMessage(mHandler.obtainMessage(0, datas));
        }
    };
    private OnDataGetListener<StudioData> mGetStudioListener = new OnDataGetListener<StudioData>() {
        @Override
        public void onReceive(List<StudioData> datas) {
            mHandler.sendMessage(mHandler.obtainMessage(1, datas));
        }
    };

    private PlayManageAdapter mPlayAdapter;
    private StudioManageAdapter mStudioAdapter;

    private void selectPlay() {
        initListDialog();

        if (mPlayAdapter == null) {
            mPlayAdapter = new PlayManageAdapter(new Selector<PlayData>() {
                @Override
                public void select(PlayData data) {
                    mCurrentData.play = data.id;
                    mSelectPlay.setText(data.name);
                    mListDialog.dismiss();
                }
            });
        }

        mList.setAdapter(mPlayAdapter);
        StoreManager.get().getAllDatas(PlayData.class, mGetPlayListener);
        mListDialog.show();
    }

    private void selectStudio() {
        initListDialog();

        if (mStudioAdapter == null) {
            mStudioAdapter = new StudioManageAdapter(new Selector<StudioData>() {
                @Override
                public void select(StudioData data) {
                    mCurrentData.studio = data.id;
                    mSelectStudio.setText(data.id + "号厅");
                    mListDialog.dismiss();
                }
            });
        }

        mList.setAdapter(mStudioAdapter);
        StoreManager.get().getAllDatas(StudioData.class, mGetStudioListener);
        mListDialog.show();
    }

    private void initListDialog() {
        if (mListDialog == null) {
            mListDialog = new AppCompatDialog(getContext());
            mListDialog.setContentView(R.layout.list);
            mListDialog.setCancelable(true);
            mListDialog.setCanceledOnTouchOutside(true);

            mList = mListDialog.findViewById(R.id.list_list);
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }
}

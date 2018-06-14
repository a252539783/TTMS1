package com.example.a38938.ttms1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.a38938.ttms1.Adapter.Selector;
import com.example.a38938.ttms1.Adapter.TicketAdapter;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.data.TicketData;
import com.example.a38938.ttms1.store.AccessHelper;
import com.example.a38938.ttms1.store.StoreManager;
import com.example.a38938.ttms1.view.SeatView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by LQF on 2018/6/13.
 */

public class SaleFragment extends MFragment implements View.OnClickListener {

    private ViewGroup mView = null;
    private ScheduleManageFragment mScheduleFragment = null;

    private ScheduleData mCurrentSchedule = null;
    private HashSet<String> mChecked = new HashSet<>();

    private Selector<ScheduleData> mSelector = new Selector<ScheduleData>() {
        @Override
        public void select(ScheduleData data) {
            mCurrentSchedule = data;
            selectSeat();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mScheduleFragment == null) {
            //mView = new FrameLayout(getContext());
            mScheduleFragment = new ScheduleManageFragment(mSelector);
        }

        mScheduleFragment.setHost(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(null);
        return mScheduleFragment.onCreateView(
                LayoutInflater.from(getContext()), mView, null);
    }

    AppCompatDialog mSelectSeatDialog = null;
    private Button mAddRowButton, mAddColButton, mMinRowButton, mMinColButton, mSaveButton;
    private SeatView mSeatView;

    private void selectSeat() {
        if (mSelectSeatDialog == null) {
            mSelectSeatDialog = new AppCompatDialog(getContext());
            mSelectSeatDialog.setContentView(R.layout.seat_manage);
            mSelectSeatDialog.setCancelable(true);
            mSelectSeatDialog.setCanceledOnTouchOutside(true);

            mAddRowButton = mSelectSeatDialog.findViewById(R.id.seat_manage_b_addrow);
            mAddColButton = mSelectSeatDialog.findViewById(R.id.seat_manage_b_addcol);
            mMinColButton = mSelectSeatDialog.findViewById(R.id.seat_manage_b_mincol);
            mMinRowButton = mSelectSeatDialog.findViewById(R.id.seat_manage_b_minrow);
            mSaveButton = mSelectSeatDialog.findViewById(R.id.seat_manage_b_save);
            mAddRowButton.setVisibility(View.GONE);
            mMinColButton.setVisibility(View.GONE);
            mMinRowButton.setVisibility(View.GONE);
            mAddColButton.setVisibility(View.GONE);
            mSaveButton.setOnClickListener(this);
            mSeatView = mSelectSeatDialog.findViewById(R.id.seat_manage_view);
            mSeatView.setSeatChecker(new SeatView.SeatChecker() {
                @Override
                public boolean isValidSeat(int row, int column) {
                    return mCurrentSchedule.getStudio().isValid(row, column);
                }

                @Override
                public boolean isSold(int row, int column) {
                    return mCurrentSchedule.isSold(row, column);
                }

                @Override
                public void checked(int row, int column) {
                    mCurrentSchedule.addSold(row, column);
                    mChecked.add(row + "," + column);
                    mSeatView.invalidate();
                }

                @Override
                public void unCheck(int row, int column) {
                    mCurrentSchedule.addSold(row, column);
                    mChecked.remove(row + "," + column);
                    mSeatView.invalidate();
                }

                @Override
                public String[] checkedSeatTxt(int row, int column) {
                    return new String[]{""};
                }
            });

        }

        mSelectSeatDialog.show();
        mSeatView.setData(mCurrentSchedule.getStudio().rows(), mCurrentSchedule.getStudio().columns());
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveButton) {
            mSelectSeatDialog.dismiss();
            StoreManager.get().update(mCurrentSchedule);

            ArrayList<TicketData> tickets = new ArrayList<>();
            for (Object x : mChecked.toArray()) {
                String[] s = ((String) x).split(",");
                TicketData t = new TicketData();
                t.schedule = mCurrentSchedule.id;
                t.seat_x = Integer.decode(s[0]);
                t.seat_y = Integer.decode(s[1]);
                tickets.add(t);
                StoreManager.get().insert(t);
            }
            if (tickets.size() > 0) {
                showTickets(tickets);
            }
            mChecked.clear();
            AccessHelper.sSchedules.get(mCurrentSchedule.id).set(mCurrentSchedule);
            mScheduleFragment.updateTime();
        }
    }

    private AppCompatDialog mTicketsDialog = null;
    private RecyclerView mList = null;
    private TicketAdapter mTicketAdapter = null;

    private void showTickets(List<TicketData> datas) {
        if (mTicketsDialog == null) {
            mTicketsDialog = new AppCompatDialog(getContext());
            mTicketsDialog.setContentView(R.layout.list);

            mList = mTicketsDialog.findViewById(R.id.list_list);
            mTicketAdapter = new TicketAdapter();
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
            mList.setAdapter(mTicketAdapter);
        }

        mTicketAdapter.setData(datas);
        mTicketsDialog.show();
    }
}

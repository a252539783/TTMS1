package com.example.a38938.ttms1.Adapter;

import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.StudioManagerFragment;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.view.SeatView;

import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public class StudioManageAdapter extends RecyclerView.Adapter<H> implements View.OnClickListener, View.OnLongClickListener {

    private List<StudioData> mDatas;

    private StudioManagerFragment mFragment;

    public StudioManageAdapter(StudioManagerFragment f) {
        mFragment = f;
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        H h = new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.studio_item, parent, false));
        h.itemView.setOnClickListener(this);
        h.itemView.setOnLongClickListener(this);
        return h;
    }

    @Override
    public void onBindViewHolder(H h, final int position) {
        h.num.setText(mDatas.get(position).id + "号厅");
        h.seat.setText(mDatas.get(position).seat + "座位");

        if (mDatas.get(position).id == -1) {    //插入数据后才能得到id，等待id更新后再更新界面
            h.itemView.postDelayed(new ChangeDelayRunnable(position), 300);
        }
    }

    public void setData(List<StudioData> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public void setData(StudioData data) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).id == data.id) {
                mDatas.set(i, data);
            }
        }
    }

    public void addData(StudioData data) {
        mDatas.add(data);
        notifyItemInserted(mDatas.size() - 1);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void onClick(View v) {
        mFragment.showSeatManageDialog(v, mDatas.get(((H)v.getTag()).getAdapterPosition()).copy());
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    private class ChangeDelayRunnable implements Runnable {

        private int position;
        ChangeDelayRunnable(int index) {
            position = index;
        }

        @Override
        public void run() {
            if (mDatas.get(position).id != -1) {
                notifyItemChanged(position);
            } else {
                mFragment.getView().postDelayed(this, 300);
            }
        }
    }
}

class H extends RecyclerView.ViewHolder {

    TextView seat, num;

    public H(View itemView) {
        super(itemView);

        itemView.setTag(this);
        num = itemView.findViewById(R.id.studio_item_num);
        seat = itemView.findViewById(R.id.studio_item_seat);
    }
}

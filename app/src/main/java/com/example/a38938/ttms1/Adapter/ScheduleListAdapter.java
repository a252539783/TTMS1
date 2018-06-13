package com.example.a38938.ttms1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.ScheduleManageFragment;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.store.StoreManager;
import com.example.a38938.ttms1.tools.TimeUtil;

import java.util.HashSet;
import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    private List<ScheduleData> mData = null;
    private ScheduleManageFragment mFragment;
    private HashSet<Integer> mSelected = new HashSet<>();

    private boolean select = false;
    private boolean selectAll = false;
    private Selector<ScheduleData> mSelector;

    public ScheduleListAdapter(ScheduleManageFragment fra) {
        mFragment = fra;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        H h = new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false));
        h.itemView.setOnClickListener(this);
        h.itemView.setOnLongClickListener(this);
        return h;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScheduleData data = mData.get(position);
        H h = (H) holder;
        h.start.setText(TimeUtil.getHour(data.start));
        h.end.setText(TimeUtil.getHour(data.end) + "结束");
        h.studio.setText(String.valueOf(data.studio));
        h.price.setText("¥" + data.price);

        if (select) {
            h.check.setVisibility(View.VISIBLE);
            h.check.setChecked(mSelected.contains(position));
        } else {
            h.check.setVisibility(View.INVISIBLE);
        }
    }

    public void setData(List<ScheduleData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void selectAll() {
        if (selectAll) {
            mSelected.clear();
        } else {
            for (int i = 0; i < mData.size(); i++) {
                mSelected.add(i);
            }
        }
        selectAll = !selectAll;
        notifyDataSetChanged();
    }

    public void delete() {
        for (int i = mData.size() - 1; i >= 0; i--) {  //从后往前，删除不会打乱原本顺序
            if (mSelected.contains(i)) {
                StoreManager.get().delete(mData.get(i));
                mData.remove(i);
            }
        }
        cancelSelect();
        notifyDataSetChanged();
    }

    public void cancelSelect() {
        select = false;
        mSelected.clear();
        selectAll = false;
        if (mFragment != null) {
            mFragment.hideDelete();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onClick(View v) {
        H h = (H) v.getTag();
        if (mFragment != null)
        mFragment.showEditDialog(mData.get(h.getAdapterPosition()).copy());
    }

    public void setData(ScheduleData data) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).id == data.id) {
                mData.get(i).set(data);
            }
        }
        notifyDataSetChanged();
    }

    public void addData(ScheduleData data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public boolean selecting() {
        return select;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mData == null || mData.size() == 0) {
            return false;
        }

        if (!select) {
            select = true;
            H h = (H) v.getTag();
            mSelected.add(h.getAdapterPosition());
            if (mFragment != null) {
                mFragment.showDelete();
            }
            notifyDataSetChanged();
        }

        return true;
    }

    static class H extends RecyclerView.ViewHolder {

        private TextView start, end, studio, seat, price;
        private CheckBox check;

        public H(View itemView) {
            super(itemView);

            itemView.setTag(this);
            start = itemView.findViewById(R.id.schedule_item_start);
            end = itemView.findViewById(R.id.schedule_item_end);
            studio = itemView.findViewById(R.id.schedule_item_studio);
            seat = itemView.findViewById(R.id.schedule_item_seat);
            price = itemView.findViewById(R.id.schedule_item_price);
            check = itemView.findViewById(R.id.schedule_item_check);
        }
    }
}

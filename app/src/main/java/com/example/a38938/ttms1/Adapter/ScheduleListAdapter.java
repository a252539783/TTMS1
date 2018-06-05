package com.example.a38938.ttms1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.data.ScheduleData;

import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter {

    private List<ScheduleData> mData = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ScheduleData data = mData.get(position);
        H h = (H) holder;
        h.seat.setText(String.valueOf(data.seat));
        h.start.setText(data.start);
        h.end.setText(data.end + "结束");
        h.studio.setText(String.valueOf(data.studio));
        h.price.setText("¥" + data.price);
    }

    public void setData(List<ScheduleData> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class H extends RecyclerView.ViewHolder {

        private TextView start, end, studio, seat, price;

        public H(View itemView) {
            super(itemView);

            start = itemView.findViewById(R.id.schedule_item_start);
            end = itemView.findViewById(R.id.schedule_item_end);
            studio = itemView.findViewById(R.id.schedule_item_studio);
            seat = itemView.findViewById(R.id.schedule_item_seat);
            price = itemView.findViewById(R.id.schedule_item_price);
        }
    }
}

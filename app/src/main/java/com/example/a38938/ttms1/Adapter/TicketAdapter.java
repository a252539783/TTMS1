package com.example.a38938.ttms1.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.data.TicketData;
import com.example.a38938.ttms1.tools.TimeUtil;

import java.io.File;
import java.util.List;

/**
 * Created by LQF on 2018/6/14.
 */

public class TicketAdapter extends RecyclerView.Adapter {

    private List<TicketData> mData = null;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ticket_item, parent, false
        ));
    }

    public void setData(List<TicketData> d) {
        mData = d;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        H h = (H) holder;
        TicketData d = mData.get(position);
        h.img.setImageURI(Uri.fromFile(new File(d.getSchedule().getPlay().imgPath)));
        h.name.setText(d.getSchedule().getPlay().name);
        h.price.setText("¥" + d.getSchedule().price);
        h.seat.setText("第" + d.seat_x + "排" + d.seat_y + "列");
        h.studio.setText(d.getSchedule().studio + "号厅");
        h.time.setText(TimeUtil.getFull(d.getSchedule().start) + "-" + TimeUtil.getHour(d.getSchedule().end));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class H extends RecyclerView.ViewHolder {

        TextView time, price, name, seat, studio;
        ImageView img;

        public H(View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.ticket_time);
            price = itemView.findViewById(R.id.ticket_price);
            name = itemView.findViewById(R.id.ticket_play);
            seat = itemView.findViewById(R.id.ticket_seat);
            studio = itemView.findViewById(R.id.ticket_studio);
            img = itemView.findViewById(R.id.ticket_img);
        }
    }

}

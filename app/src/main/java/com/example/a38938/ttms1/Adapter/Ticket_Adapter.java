package com.example.a38938.ttms1.Adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a38938.ttms1.R;

import java.util.List;

/**
 * Created by 38938 on 2018/5/19.
 */

public class Ticket_Adapter extends BaseAdapter {
    private List<Ticket_Adapter_List> list;
    private LayoutInflater inflter;
    private ImageView ticket_img;
    private TextView ticket_play_name;
    private TextView ticket_play_time;
    private TextView ticket_play_score;
    private TextView ticket_play_type;
    private TextView ticket_play_director;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.ticket_listview, null);

        // 初始化UI
        ticket_img = (ImageView)view.findViewById(R.id.ticket_img);
        ticket_play_name = (TextView)view.findViewById(R.id.ticket_play_name);
        ticket_play_time = (TextView)view.findViewById(R.id.ticket_play_time);
        ticket_play_score = (TextView)view.findViewById(R.id.ticket_play_score);
        ticket_play_type = (TextView)view.findViewById(R.id.ticket_play_type);
        ticket_play_director = (TextView)view.findViewById(R.id.ticket_play_director);
        // Set date into tv
        ticket_img.setImageResource(R.drawable.aaa);
        ticket_play_name.setText("aaa");
        ticket_play_time.setText("bbbb");
        ticket_play_type.setText("ccc");
        ticket_play_score.setText("ddd");
        ticket_play_director.setText("fff");

        return view;
    }
}

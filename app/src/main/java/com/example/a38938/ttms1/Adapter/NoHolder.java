package com.example.a38938.ttms1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.a38938.ttms1.R;

/**
 * Created by LQF on 2018/6/8.
 */

public class NoHolder extends RecyclerView.ViewHolder {
    TextView t;

    public NoHolder(View itemView) {
        super(itemView);

        t = itemView.findViewById(R.id.no_item_t);
    }
}

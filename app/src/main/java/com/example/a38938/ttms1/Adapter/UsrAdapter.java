package com.example.a38938.ttms1.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.UserFragment;
import com.example.a38938.ttms1.data.Data;
import com.example.a38938.ttms1.data.UserData;
import com.example.a38938.ttms1.store.StoreManager;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by LQF on 2018/6/14.
 */

public class UsrAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    private List<UserData> mData = null;
    private HashSet<Integer> mSelected = new HashSet<>();

    private boolean select = false;
    private boolean selectAll = false;

    private UserFragment mFragment = null;

    public UsrAdapter(UserFragment f) {
        mFragment = f;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.user_item, parent, false
        ));
    }

    public void setData(List<UserData> d) {
        mData = d;
        cancelSelect();
        notifyDataSetChanged();
    }

    public boolean selecting() {
        return select;
    }

    public void addData(UserData d) {
        mData.add(d);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        H h = (H) holder;
        UserData d = mData.get(position);
        h.username.setText(d.usrname);
        h.name.setText(d.name);
        h.id.setText("" + d.id);

        if (d.id == -1) {    //插入数据后才能得到id，等待id更新后再更新界面
            h.itemView.postDelayed(new ChangeDelayRunnable(position), 300);
        }
        if (select) {
            h.check.setVisibility(View.VISIBLE);
            h.check.setChecked(mSelected.contains(position));
        } else {
            h.check.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onClick(View v) {
        H h = (H) v.getTag();
        if (select) {
            if (mSelected.contains(h.getAdapterPosition())) {
                mSelected.remove(h.getAdapterPosition());
            } else {
                mSelected.add(h.getAdapterPosition());
            }
            notifyItemChanged(h.getAdapterPosition());
        } else {
            if (mFragment != null) {
                mFragment.edit(mData.get(h.getAdapterPosition()));
            }
        }
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

    class H extends RecyclerView.ViewHolder {

        TextView username, name, id;
        CheckBox check;

        public H(View itemView) {
            super(itemView);

            itemView.setOnClickListener(UsrAdapter.this);
            itemView.setOnLongClickListener(UsrAdapter.this);
            itemView.setTag(this);
            username = itemView.findViewById(R.id.user_usrname);
            name = itemView.findViewById(R.id.user_name);
            id = itemView.findViewById(R.id.user_id);
            check = itemView.findViewById(R.id.user_check);
        }
    }

    private class ChangeDelayRunnable implements Runnable {

        private int position;
        ChangeDelayRunnable(int index) {
            position = index;
        }

        @Override
        public void run() {
            if (mData.get(position).id != -1) {
                notifyItemChanged(position);
            } else {
                if (mFragment != null) {
                    mFragment.getView().postDelayed(this, 300);
                }
            }
        }
    }
}

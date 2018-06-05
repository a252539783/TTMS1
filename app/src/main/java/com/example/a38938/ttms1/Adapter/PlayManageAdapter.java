package com.example.a38938.ttms1.Adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a38938.ttms1.PlayManageFragment;
import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.data.PlayData;
import com.example.a38938.ttms1.store.StoreManager;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public class PlayManageAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {
    private List<PlayData> mDatas = null;
    private HashSet<Integer> mSelected = new HashSet<>();

    private boolean select = false;
    private boolean selectAll = false;

    private PlayManageFragment mFragment;

    public PlayManageAdapter(PlayManageFragment f) {
        mFragment = f;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.play_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PlayData data = mDatas.get(position);
        H h = (H) holder;
        h.director.setText("导演:" + data.director);
        h.name.setText("名称:" + data.name);
        h.actor.setText("主演:" + data.actors);
        h.score.setText("评分" + String.valueOf(data.score));

        h.img.setImageURI(data.imgPath == null ? null : Uri.fromFile(new File(data.imgPath)));
        if (select) {
            h.check.setVisibility(View.VISIBLE);
            h.check.setChecked(mSelected.contains(position));
        } else {
            h.check.setVisibility(View.INVISIBLE);
        }

        h.itemView.setOnClickListener(this);
        h.itemView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setData(List<PlayData> data) {
        mDatas = data;
        mSelected.clear();
        notifyDataSetChanged();
    }

    public void cancelSelect() {
        select = false;
        mSelected.clear();
        selectAll = false;
        mFragment.hideDelete();
        notifyDataSetChanged();
    }

    public List<PlayData> getDatas() {
        return mDatas;
    }

    public boolean selecting() {
        return select;
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
            mFragment.edit(mDatas.get(h.getAdapterPosition()));
        }
    }

    public void selectAll() {
        if (selectAll) {
            mSelected.clear();
        } else {
            for (int i = 0; i < mDatas.size(); i++) {
                mSelected.add(i);
            }
        }
        selectAll = !selectAll;
        notifyDataSetChanged();
    }

    public void delete() {
        for (int i = mDatas.size() - 1; i >= 0; i--) {  //从后往前，删除不会打乱原本顺序
            if (mSelected.contains(i)) {
                StoreManager.get().delete(mDatas.get(i));
                mDatas.remove(i);
            }
        }
        cancelSelect();
        notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View v) {
        if (!select) {
            select = true;
            H h = (H) v.getTag();
            mSelected.add(h.getAdapterPosition());
            mFragment.showDelete();
            notifyDataSetChanged();
        }

        return true;
    }

    static class H extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, director, actor, score;
        CheckBox check;

        public H(View itemView) {
            super(itemView);

            itemView.setTag(this);
            img = itemView.findViewById(R.id.play_item_image);
            name = itemView.findViewById(R.id.play_item_t_name);
            director = itemView.findViewById(R.id.play_item_t_director);
            actor = itemView.findViewById(R.id.play_item_t_actor);
            score = itemView.findViewById(R.id.play_item_t_score);
            check = itemView.findViewById(R.id.play_item_check);
        }
    }
}

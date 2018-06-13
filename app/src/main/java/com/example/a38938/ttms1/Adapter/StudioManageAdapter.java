package com.example.a38938.ttms1.Adapter;

import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.StudioManagerFragment;
import com.example.a38938.ttms1.data.StudioData;
import com.example.a38938.ttms1.store.StoreManager;
import com.example.a38938.ttms1.view.SeatView;

import java.util.HashSet;
import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public class StudioManageAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

    private List<StudioData> mDatas;
    private HashSet<Integer> mSelected = new HashSet<>();

    private boolean select = false;
    private boolean selectAll = false;

    private StudioManagerFragment mFragment;
    private Selector<StudioData> mSelector;

    public StudioManageAdapter(StudioManagerFragment f) {
        mFragment = f;
    }

    public StudioManageAdapter(Selector<StudioData> s) {
        mSelector = s;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder h = viewType == 0 ?
                new H(LayoutInflater.from(parent.getContext()).inflate(R.layout.studio_item, parent, false)) :
                new NoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.no_item, parent, false));
        h.itemView.setOnClickListener(this);
        h.itemView.setOnLongClickListener(this);
        return h;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof H) {
            H h = (H) holder;
            h.num.setText(mDatas.get(position).id + "号厅");
            h.seat.setText(mDatas.get(position).seat + "座位");

            if (mDatas.get(position).id == -1) {    //插入数据后才能得到id，等待id更新后再更新界面
                h.itemView.postDelayed(new ChangeDelayRunnable(position), 300);
            }
            if (select) {
                h.check.setVisibility(View.VISIBLE);
                h.check.setChecked(mSelected.contains(position));
            } else {
                h.check.setVisibility(View.INVISIBLE);
            }
        } else {
            NoHolder h = (NoHolder) holder;
            if (mDatas == null) {
                h.t.setText("加载中...");
            } else {
                h.t.setText("没有数据...");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return 666;
        }

        return 0;
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

    public void setData(List<StudioData> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    public void setData(StudioData data) {
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).id == data.id) {
                mDatas.get(i).set(data);
            }
        }
    }

    public void addData(StudioData data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }

    public boolean selecting() {
        return select;
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
    public int getItemCount() {
        return mDatas == null ? 1 : mDatas.size() == 0 ? 1 : mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }

        if (mFragment != null) {
            mFragment.showSeatManageDialog(v, mDatas.get(((H) v.getTag()).getAdapterPosition()).copy());
        }

        if (mSelector != null) {
            mSelector.select(mDatas.get(((H) v.getTag()).getAdapterPosition()));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mDatas == null || mDatas.size() == 0) {
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
                if (mFragment != null) {
                    mFragment.getView().postDelayed(this, 300);
                }
            }
        }
    }
}

class H extends RecyclerView.ViewHolder {

    TextView seat, num;
    CheckBox check;

    public H(View itemView) {
        super(itemView);

        itemView.setTag(this);
        num = itemView.findViewById(R.id.studio_item_num);
        seat = itemView.findViewById(R.id.studio_item_seat);
        check = itemView.findViewById(R.id.studio_item_check);
    }
}

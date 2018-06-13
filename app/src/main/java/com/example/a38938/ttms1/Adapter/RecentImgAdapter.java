package com.example.a38938.ttms1.Adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a38938.ttms1.R;
import com.example.a38938.ttms1.data.PlayData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQF on 2018/5/28.
 */

public class RecentImgAdapter extends PagerAdapter {

    private ArrayList<ImageView> mImgs = new ArrayList<>();
    private List<PlayData> mDatas;

    @Override
    public int getCount() {
        return mDatas == null ?
                0 : (mDatas.size() < 5 ?
                mDatas.size() : 5);
    }

    public List<PlayData> getData() {
        return mDatas;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<PlayData> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImgs.size() <= position || mImgs.get(position) == null) {
            ImageView img = (ImageView) LayoutInflater.from(container.getContext()).inflate(R.layout.img_item, container, false);
//            img.setImageResource(R.drawable.aaa);
            mImgs.add(position, img);
        }

        if (mImgs.get(position).getParent() == null)
            container.addView(mImgs.get(position));

        mImgs.get(position).setImageURI(Uri.fromFile(new File(mDatas.get(position).imgPath)));
        return mImgs.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImgs.get(position));
    }
}

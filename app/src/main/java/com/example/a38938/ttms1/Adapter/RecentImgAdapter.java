package com.example.a38938.ttms1.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a38938.ttms1.R;

/**
 * Created by LQF on 2018/5/28.
 */

public class RecentImgAdapter extends PagerAdapter {

    private View[] mImgs = new View[5];

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mImgs[position] == null) {
            View img = LayoutInflater.from(container.getContext()).inflate(R.layout.img_item, container, false);
            img.getLayoutParams().height = 300;
            img.setLayoutParams(img.getLayoutParams());
//            img.setImageResource(R.drawable.aaa);
            mImgs[position] = img;
        }

        if (mImgs[position].getParent() == null)
            container.addView(mImgs[position]);
        return mImgs[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mImgs[position]);
    }
}

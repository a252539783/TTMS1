package com.example.a38938.ttms1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.LinkagePager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.a38938.ttms1.Adapter.RecentImgAdapter;
import com.example.a38938.ttms1.Adapter.ScheduleListAdapter;
import com.example.a38938.ttms1.Adapter.Selector;
import com.example.a38938.ttms1.data.ScheduleData;

import java.util.Arrays;

import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.LinkagePagerContainer;
import me.crosswall.lib.coverflow.core.PagerContainer;

/**
 * Created by LQF on 2018/5/28.
 */

public class MainFragment extends MFragment {

    private ViewGroup mView = null;

    private RecyclerView mList = null;
    private ViewPager mPager = null;
    private PagerContainer mPagerContainer = null;

    private CoverFlow mCover = null;
    private ScheduleListAdapter mAdapter = new ScheduleListAdapter(new Selector<ScheduleData>() {
        @Override
        public void select(ScheduleData data) {

        }
    });

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = (LinearLayout) inflater.inflate(R.layout.content_main, container, false);
        }

        initView();
        return mView;
    }

    private void initView() {
        if (mList == null) {
            mList = mView.findViewById(R.id.main_list);
            mList.setLayoutManager(new LinearLayoutManager(getContext()));
            mList.setAdapter(mAdapter);
            mPager = mView.findViewById(R.id.main_pager);
            mPagerContainer = mView.findViewById(R.id.main_pager_container);
            //mPagerContainer.setOverlapEnabled(true);
            mCover = new CoverFlow.Builder()
                    .with(mPager)
                    .pagerMargin(0f)
                    .scale(0.3f)
                    .spaceSize(0f)
                    .rotationY(0f)
                    .build();
            //mPager.setClipChildren(false);
            mPager.setOffscreenPageLimit(5);
            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mAdapter.setData(Arrays.asList(new ScheduleData(),new ScheduleData(),new ScheduleData(),new ScheduleData()));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mPager.setAdapter(new RecentImgAdapter());
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(null);

        //TODO 初始页，即还未滚动的页面展示
    }
}

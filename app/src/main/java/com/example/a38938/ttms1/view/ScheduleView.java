package com.example.a38938.ttms1.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.a38938.ttms1.data.ScheduleData;
import com.example.a38938.ttms1.tools.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LQF on 2018/6/7.
 */

public class ScheduleView extends View {

    private float whRatio = 3 / 4f;
    private int unitWidth, unitHeight;
    private float offsetX = 0, offsetY = 0;
    private float realWidth, realHeight;

    private OnEditListener mListener = null;

    private int dayStart = 0, days = 30;
    private int year, month;

    private List<List<ScheduleData>> mDatas = null;

    private static Paint sRectPaint, sDayPaint, sWeekPaint, sTextPaint;
    private static int sTextHeight = 20;
    private static int sTextNum = 0;

    static {
        sRectPaint = new Paint();
        sRectPaint.setStyle(Paint.Style.STROKE);
        sRectPaint.setColor(Color.GRAY);
        sDayPaint = new Paint();
        sDayPaint.setStyle(Paint.Style.STROKE);
        sDayPaint.setColor(Color.BLACK);
        sTextPaint = new Paint();
        sTextPaint.setStyle(Paint.Style.STROKE);
        sTextPaint.setColor(Color.WHITE);
        sWeekPaint = sRectPaint;
    }

    public ScheduleView(Context context) {
        super(context);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScheduleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnEditListener l) {
        mListener = l;
    }

    public void setTime(int year, int month) {
        this.year = year;
        this.month = month;

        dayStart = (TimeUtil.dateToWeek(year + "-" + month) + 1) % 7;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        sWeekPaint.setTextSize(dm.density * 20);
        sTextHeight = (int) (dm.density * 30);
        whRatio = (float) getMeasuredWidth() / getMeasuredHeight();
        if (getMeasuredWidth() < getMeasuredHeight()) {
            unitWidth = (int) (140 * dm.density);
            unitHeight = (int) (unitWidth / whRatio);
        } else {
            unitHeight = (int) (140 * dm.density);
            unitWidth = (int) (unitHeight * whRatio);
        }
        sTextNum = unitHeight / sTextHeight;
        realWidth = unitWidth * 7;
        realHeight = unitHeight * 5;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        int x = (int) offsetX;
        int y = (int) offsetY;
        int index = -1;
        for (int i = 0; i < 5; i++) {
            x = (int) offsetX;
            for (int j = 0; j < 7; j++) {
                c.drawRect(x, y, x + unitWidth, y + unitHeight, sRectPaint);
                drawContent(x, y, index++, c);
                x += unitWidth;
            }
            y += unitHeight;
        }
    }

    private void drawContent(int x, int y, int index, Canvas c) {
        if (mDatas != null) {
            List<ScheduleData> datas = mDatas.get(index);
            if (datas != null) {
                for (int i = 0; i < datas.size(); i++) {
                    if (i == sTextNum - 1) {
                        c.drawText(".....", x, y + sTextHeight * i, sTextPaint);
                        break;
                    } else {
                        c.drawText(datas.get(i).play + "", x, y + sTextHeight * i, sTextPaint);
                    }
                }
            }
        }
    }

    private float mLastX = -1, mLastY = -1;
    private float mDownX = -1, mDownY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean eat = false;
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                eat = true;
                mDownX = e.getX();
                mDownY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = e.getX() - mLastX;
                float dy = e.getY() - mLastY;
                if (checkScroll(dx, dy)) {
                    eat = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if ((e.getX() - mDownX) * (e.getX() - mDownX) + (e.getY() - mDownY) * (e.getY() - mDownY) <= 100) {
                    click(e.getX() - offsetX, e.getY() - offsetY);
                }
                break;
        }
        mLastX = e.getX();
        mLastY = e.getY();

        return eat;
    }

    private void click(float x, float y) {
        int xi = (int) (x / unitWidth);
        int yi = (int) (y / unitHeight);
        int index = yi * 7 + xi - dayStart;
        int indexInner = (int)y % unitHeight / sTextHeight;

        if (mListener != null) {
            if (mDatas == null || mDatas.get(index) == null || mDatas.get(index).size() <= indexInner) {
                //添加
                mListener.onAdd(year, month, index);
            } else {
                //修改
                mListener.onEdit(index, indexInner, mDatas.get(index).get(indexInner));
            }
        }
    }

    private boolean checkScroll(float dx, float dy) {
        float oldX = offsetX;
        float oldY = offsetY;
        offsetX += dx;
        offsetY += dy;
        if (offsetX > 0) {
            offsetX = 0;
        } else if (offsetX < getWidth() - realWidth) {
            offsetX = getWidth() - realWidth;
        }

        if (offsetY > 0) {
            offsetY = 0;
        } else if (offsetY < getHeight() - realHeight) {
            offsetY = getHeight() - realHeight;
        }

        return (oldX != offsetX) || (oldY != offsetY);
    }

    public interface OnEditListener {
        void onEdit(int dayIndex, int innerIndex, ScheduleData data);

        void onAdd(int year, int month, int day);
    }
}

package com.example.a38938.ttms1.store;

import com.example.a38938.ttms1.data.Data;

import java.util.List;

/**
 * Created by LQF on 2018/5/29.
 */

public interface OnDataGetListener<T extends Data> {
    void onReceive(List<T> datas);
}

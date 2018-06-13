package com.example.a38938.ttms1.Adapter;

import com.example.a38938.ttms1.data.Data;

/**
 * Created by LQF on 2018/6/8.
 */

public interface Selector<T extends Data> {
    void select(T data);
}

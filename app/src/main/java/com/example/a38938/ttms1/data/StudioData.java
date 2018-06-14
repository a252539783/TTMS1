package com.example.a38938.ttms1.data;

import android.content.ContentValues;

import com.example.a38938.ttms1.store.StoreBusiness;

/**
 * Created by LQF on 2018/5/29.
 */

public class StudioData extends Data {
    public String name;
    private int rows = 10, columns = 10;
    public int seat = rows * columns;
    private boolean[][] inval = null;
    private int invalidNum = 0;

    public StudioData() {
        type = TYPE_STUDIO;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public void setRowCol(int row, int col) {
        rows = row;
        columns = col;
        seat = rows * columns - invalidNum;

        if (inval != null && inval.length != 0 && inval[0].length != 0) {
            boolean[][] old = inval;
            inval = new boolean[rows][columns];
            for (int i = 0; i < old.length && i < row; i++) {
                for (int j = 0; j < old[0].length && j < col; j++) {
                    inval[i][j] = old[i][j];
                }
            }
        }
    }

    public void addInvalid(int row, int column) {
        if (inval == null) {
            inval = new boolean[rows][columns];
        }

        inval[row][column] = !inval[row][column];

        if (inval[row][column]) {
            invalidNum++;
            seat--;
        } else {
            invalidNum--;
            seat++;
        }
    }

    public boolean isValid(int r, int c) {
        if (inval == null) {
            return true;
        }

        if (inval.length <= r) {
            return false;
        }

        if (inval[0].length <= c) {
            return false;
        }

        return !inval[r][c];
    }

    @Override
    public ContentValues contentValues() {
        ContentValues cv = new ContentValues();
        cv.put(StoreBusiness.ROW_NAME, name);
        cv.put(StoreBusiness.ROW_SEAT, seat);
        cv.put(StoreBusiness.ROW_ROW, rows);
        cv.put(StoreBusiness.ROW_COLUMN, columns);

        StringBuilder res = new StringBuilder("");
        if (inval != null) {
            for (int i = 0; i < inval.length; i++) {
                for (int j = 0;j < columns; j++) {
                    if (inval[i][j]) {
                        res.append(i).append(',').append(j).append(',');
                    }
                }
            }
        }
        cv.put(StoreBusiness.ROW_INVALID, res.toString());
        return cv;
    }

    public StudioData copy() {
        StudioData data = new StudioData();
        data.id = id;
        data.setRowCol(rows, columns);
        if (inval != null) {
            for (int i = 0; i < inval.length; i++) {
                for (int j = 0;j < columns; j++) {
                    if (inval[i][j]) {
                        data.addInvalid(i, j);
                    }
                }
            }
        }

        return data;
    }

    /**
     * 有这个方法是为了让所有的更改都统一在同一个对象上，方便缓存的更新
     * @param data
     */
    public void set(StudioData data) {
        id = data.id;
        seat = data.seat;
        rows = data.rows;
        columns = data.columns;
        inval = data.inval;
        invalidNum = data.invalidNum;
        name = data.name;
    }
}

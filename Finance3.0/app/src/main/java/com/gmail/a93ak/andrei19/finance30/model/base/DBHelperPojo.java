package com.gmail.a93ak.andrei19.finance30.model.base;

import android.database.Cursor;

public interface DBHelperPojo<T> {
    long add(T t);
    T get(long id);
    Cursor getAll();
    int update(T t);
    int delete(long id);
    int deleteAll();
}

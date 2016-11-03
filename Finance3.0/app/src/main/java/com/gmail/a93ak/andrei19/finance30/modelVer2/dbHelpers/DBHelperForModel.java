package com.gmail.a93ak.andrei19.finance30.modelVer2.dbHelpers;

import android.database.Cursor;

import java.util.List;

//
public interface DBHelperForModel<T> {
    long add(T t);
    T get(long id);
    Cursor getAll();
    List<T> getAllToList();
    int update(T t);
    int delete(long id);
    int deleteAll();
}

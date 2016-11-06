package com.gmail.a93ak.andrei19.finance30.model.dbHelpers;

import android.database.Cursor;

import java.util.List;

//
public interface DBHelperForModel<Model> {

    long add(Model model);

    Model get(long id);

    Cursor getAll();

    List<Model> getAllToList();

    int update(Model model);

    int delete(long id);

    int deleteAll();
}

package com.github.andrei1993ak.finances.model.dbHelpers;

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

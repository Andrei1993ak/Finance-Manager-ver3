package com.github.andrei1993ak.finances.model.newModel;

import android.database.Cursor;

import com.github.andrei1993ak.finances.model.models.TableClass;

import java.util.List;

public interface IUniversalDBHelper<Model extends TableClass> {

    long add(Model model);

    Model get(long id);

    Cursor getAll();

    List<Model> getAllToList();

    int update(Model model,long id);

    int delete(long id);

    int deleteAll();
}

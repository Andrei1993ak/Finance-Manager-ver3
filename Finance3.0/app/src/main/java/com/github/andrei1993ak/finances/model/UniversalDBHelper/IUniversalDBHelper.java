package com.github.andrei1993ak.finances.model.UniversalDBHelper;

import android.database.Cursor;

import com.github.andrei1993ak.finances.model.models.TableClass;

import java.util.List;

public interface IUniversalDBHelper<Model extends TableClass> {

    long add(Model model);

    Model get(long id, Class<Model> clazz);

    Cursor getAll(Class<Model> clazz);

    List<Model> getAllToList(Class<Model> clazz);

    int update(Model model,long id);

    int delete(long id,Class<Model> clazz);

    int deleteAll(Class<Model> clazz);
}

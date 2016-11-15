package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryCost;

public class CostCategoryCursorLoader extends CursorLoader {

    private DBHelperCategoryCost helper;
    private long id;

    public CostCategoryCursorLoader(Context context, long id) {
        super(context);
        helper = DBHelperCategoryCost.getInstance();
        this.id = id;
    }

    @Override
    public Cursor loadInBackground() {
        return helper.getAllByParentId(id);
    }
}



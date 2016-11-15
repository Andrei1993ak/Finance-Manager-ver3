package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCategoryIncome;

public class IncomeCategoryCursorLoader extends CursorLoader {

    private DBHelperCategoryIncome helper;
    private long id;

    public IncomeCategoryCursorLoader(Context context, long id) {
        super(context);
        helper = DBHelperCategoryIncome.getInstance();
        this.id = id;
    }

    @Override
    public Cursor loadInBackground() {
        return helper.getAllByParentId(id);
    }
}



package com.gmail.a93ak.andrei19.finance30.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryIncome;

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



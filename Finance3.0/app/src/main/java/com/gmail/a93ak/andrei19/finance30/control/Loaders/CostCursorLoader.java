package com.gmail.a93ak.andrei19.finance30.control.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCost;

public class CostCursorLoader extends CursorLoader {

    private DBHelperCost dbHelperCost;

    public CostCursorLoader(Context context) {
        super(context);
        this.dbHelperCost = DBHelperCost.getInstance(DBHelper.getInstance(context));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCost.getAll();
    }
}

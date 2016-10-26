package com.gmail.a93ak.andrei19.finance30.control.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperIncome;

public class IncomeCursorLoader extends CursorLoader {

    private DBHelperIncome dbHelperIncome;

    public IncomeCursorLoader(Context context) {
        super(context);
        this.dbHelperIncome = DBHelperIncome.getInstance(DBHelper.getInstance(context));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperIncome.getAll();
    }
}

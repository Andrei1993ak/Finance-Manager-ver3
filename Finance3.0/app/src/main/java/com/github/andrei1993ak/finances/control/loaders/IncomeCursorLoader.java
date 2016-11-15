package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;

public class IncomeCursorLoader extends CursorLoader {

    private DBHelperIncome dbHelperIncome;

    public IncomeCursorLoader(Context context) {
        super(context);
        this.dbHelperIncome = DBHelperIncome.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperIncome.getAll();
    }
}

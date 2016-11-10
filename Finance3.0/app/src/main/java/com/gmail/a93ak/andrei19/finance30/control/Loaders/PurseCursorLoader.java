package com.gmail.a93ak.andrei19.finance30.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperPurse;

public class PurseCursorLoader extends CursorLoader{
//
    private DBHelperPurse dbHelperPurse;

    public PurseCursorLoader(Context context) {
        super(context);
        this.dbHelperPurse = DBHelperPurse.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperPurse.getAll();
    }
}

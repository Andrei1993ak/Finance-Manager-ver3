package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperPurse;

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

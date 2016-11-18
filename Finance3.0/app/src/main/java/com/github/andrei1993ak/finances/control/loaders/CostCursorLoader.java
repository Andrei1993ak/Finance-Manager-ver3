package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;

public class CostCursorLoader extends CursorLoader {

    private final DBHelperCost dbHelperCost;

    public CostCursorLoader(final Context context) {
        super(context);
        this.dbHelperCost = DBHelperCost.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCost.getAll();
    }
}

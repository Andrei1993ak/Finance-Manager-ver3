package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCost;
import com.github.andrei1993ak.finances.model.models.Cost;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class CostCursorLoader extends CursorLoader {

    private final DBHelperCost dbHelperCost;

    public CostCursorLoader(final Context context) {
        super(context);
        this.dbHelperCost = ((DBHelperCost) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Cost.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCost.getAll();
    }
}

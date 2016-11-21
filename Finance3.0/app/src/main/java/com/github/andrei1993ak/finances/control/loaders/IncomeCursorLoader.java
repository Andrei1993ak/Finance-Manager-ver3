package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class IncomeCursorLoader extends CursorLoader {

    private final DBHelperIncome dbHelperIncome;

    public IncomeCursorLoader(final Context context) {
        super(context);
        this.dbHelperIncome = ((DBHelperIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Income.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperIncome.getAll();
    }
}

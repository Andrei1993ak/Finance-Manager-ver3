package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;


public class CurrencyCursorLoader extends CursorLoader {

    private final DBHelperCurrency dbHelperCurrency;

    public CurrencyCursorLoader(final Context context) {
        super(context);
        dbHelperCurrency = DBHelperCurrency.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCurrency.getAll();
    }
}

package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;

public class CurrencyAllCursorLoader extends CursorLoader {

    private final DBHelperCurrencyOfficial dbHelperCurrency;

    public CurrencyAllCursorLoader(final Context context) {
        super(context);
        dbHelperCurrency = DBHelperCurrencyOfficial.getInstance();
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCurrency.getAll();
    }
}

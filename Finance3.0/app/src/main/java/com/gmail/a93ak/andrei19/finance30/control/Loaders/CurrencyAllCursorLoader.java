package com.gmail.a93ak.andrei19.finance30.control.Loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.dbhelpers.DBHelperCurrencyOfficial;

public class CurrencyAllCursorLoader extends CursorLoader {

    private DBHelperCurrencyOfficial dbHelperCurrency;

    public CurrencyAllCursorLoader(Context context) {
        super(context);
        dbHelperCurrency = DBHelperCurrencyOfficial.getInstance(DBHelper.getInstance(context));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCurrency.getAll();
    }
}

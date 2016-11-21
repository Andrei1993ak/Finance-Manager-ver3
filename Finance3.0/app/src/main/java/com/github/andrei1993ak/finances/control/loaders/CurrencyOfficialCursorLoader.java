package com.github.andrei1993ak.finances.control.loaders;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.ContextHolder;

public class CurrencyOfficialCursorLoader extends CursorLoader {

    private final DBHelperCurrencyOfficial dbHelperCurrency;

    public CurrencyOfficialCursorLoader(final Context context) {
        super(context);
        dbHelperCurrency = ((DBHelperCurrencyOfficial) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CurrencyOfficial.class));
    }

    @Override
    public Cursor loadInBackground() {
        return dbHelperCurrency.getAll();
    }
}

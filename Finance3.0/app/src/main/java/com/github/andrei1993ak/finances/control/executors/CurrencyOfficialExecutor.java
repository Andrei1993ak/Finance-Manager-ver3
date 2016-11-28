package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.IOnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.List;


public class CurrencyOfficialExecutor extends PojoExecutor<CurrencyOfficial> {

    public static final int KEY_RESULT_GET = 204;

    private final DBHelperCurrencyOfficial dbHelperCurrencyOfficial = ((DBHelperCurrencyOfficial) ((App) ContextHolder.getInstance().getContext()).getDbHelper(CurrencyOfficial.class));
    public CurrencyOfficialExecutor(final IOnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<CurrencyOfficial> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperCurrencyOfficial.get(id));
    }

    @Override
    public Result<Long> addPojo(final CurrencyOfficial currencyOfficial) {
        return null;
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return null;
    }

    @Override
    public Result<Integer> updatePojo(final CurrencyOfficial currencyOfficial) {
        return null;
    }

    @Override
    public Result<Cursor> getAll() {
        return null;
    }

    @Override
    public Result<Integer> deleteAll() {
        return null;
    }

    @Override
    public Result<List<CurrencyOfficial>> getAllToList(final int selection) {
        return null;
    }
}

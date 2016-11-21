package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrencyOfficial;
import com.github.andrei1993ak.finances.model.models.Currency;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperCurrency;
import com.github.andrei1993ak.finances.model.models.CurrencyOfficial;
import com.github.andrei1993ak.finances.util.ContextHolder;


import java.util.List;

public class CurrencyExecutor extends PojoExecutor<Currency> {

    private final DBHelperCurrency dbHelperCurrency = ((DBHelperCurrency) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Currency.class));
    public static final int KEY_RESULT_ADD = 101;
    public static final int KEY_RESULT_EDIT = 102;
    public static final int KEY_RESULT_DELETE = 103;
    public static final int KEY_RESULT_GET = 104;
    public static final int KEY_RESULT_GET_ALL = 105;
    public static final int KEY_RESULT_DELETE_ALL = 106;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 107;

    public CurrencyExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Currency> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperCurrency.get(id));
    }

    @Override
    public Result<Long> addPojo(final Currency currency) {
        return new Result<>(KEY_RESULT_ADD, dbHelperCurrency.add(currency));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelperCurrency.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Currency currency) {
        return new Result<>(KEY_RESULT_EDIT, dbHelperCurrency.update(currency));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelperCurrency.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperCurrency.deleteAll());
    }

    @Override
    public Result<List<Currency>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelperCurrency.getAllToList());
    }
}

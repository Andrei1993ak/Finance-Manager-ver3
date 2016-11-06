package com.gmail.a93ak.andrei19.finance30.control.Executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCurrency;
import com.gmail.a93ak.andrei19.finance30.model.models.Currency;


import java.util.List;

public class CurrencyExecutor extends PojoExecutor<Currency> {

    public static final int KEY_RESULT_ADD = 101;
    public static final int KEY_RESULT_EDIT = 102;
    public static final int KEY_RESULT_DELETE = 103;
    public static final int KEY_RESULT_GET = 104;
    public static final int KEY_RESULT_GET_ALL = 105;
    public static final int KEY_RESULT_DELETE_ALL = 106;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 107;

    public CurrencyExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Currency> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperCurrency.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(Currency currency) {
        return new Result<>(KEY_RESULT_ADD, DBHelperCurrency.getInstance().add(currency));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperCurrency.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Currency currency) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperCurrency.getInstance().update(currency));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperCurrency.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperCurrency.getInstance().deleteAll());
    }

    @Override
    public Result<List<Currency>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, DBHelperCurrency.getInstance().getAllToList());
    }
}

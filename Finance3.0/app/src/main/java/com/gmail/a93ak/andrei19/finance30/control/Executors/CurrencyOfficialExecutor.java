package com.gmail.a93ak.andrei19.finance30.control.executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCurrencyOfficial;
import com.gmail.a93ak.andrei19.finance30.model.models.CurrencyOfficial;

import java.util.List;


public class CurrencyOfficialExecutor extends PojoExecutor<CurrencyOfficial> {

    public static final int KEY_RESULT_GET = 204;

    public CurrencyOfficialExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<CurrencyOfficial> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperCurrencyOfficial.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(CurrencyOfficial currencyOfficial) {
        return null;
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return null;
    }

    @Override
    public Result<Integer> updatePojo(CurrencyOfficial currencyOfficial) {
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
    public Result<List<CurrencyOfficial>> getAllToList(int selection) {
        return null;
    }
}

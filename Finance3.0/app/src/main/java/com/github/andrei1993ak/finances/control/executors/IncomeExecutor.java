package com.github.andrei1993ak.finances.control.executors;

import android.database.Cursor;

import com.github.andrei1993ak.finances.App;
import com.github.andrei1993ak.finances.control.base.OnTaskCompleted;
import com.github.andrei1993ak.finances.control.base.PojoExecutor;
import com.github.andrei1993ak.finances.control.base.Result;
import com.github.andrei1993ak.finances.model.dbHelpers.DBHelperIncome;
import com.github.andrei1993ak.finances.model.models.Income;
import com.github.andrei1993ak.finances.util.ContextHolder;

import java.util.ArrayList;
import java.util.List;

public class IncomeExecutor extends PojoExecutor<Income> {

    private final DBHelperIncome dbHelperIncome = ((DBHelperIncome) ((App) ContextHolder.getInstance().getContext()).getDbHelper(Income.class));

    public static final int KEY_RESULT_ADD = 601;
    public static final int KEY_RESULT_EDIT = 602;
    public static final int KEY_RESULT_DELETE = 603;
    public static final int KEY_RESULT_GET = 604;
    public static final int KEY_RESULT_GET_ALL = 605;
    public static final int KEY_RESULT_DELETE_ALL = 606;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 607;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 608;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID = 609;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 610;

    public IncomeExecutor(final OnTaskCompleted listener) {
        super(listener);

    }

    @Override
    public Result<Income> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelperIncome.get(id));
    }

    @Override
    public Result<Long> addPojo(final Income income) {
        return new Result<>(KEY_RESULT_ADD, dbHelperIncome.add(income));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelperIncome.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final Income income) {
        return new Result<>(KEY_RESULT_EDIT, dbHelperIncome.update(income));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelperIncome.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperIncome.deleteAll());
    }

    @Override
    public Result<List<Income>> getAllToList(final int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelperIncome.getAllToList());
    }

    @Override
    protected Result<List<Income>> getAllToListByDates(final ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES, dbHelperIncome.getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Income>> getAllToListByWalletId(final Long walletId) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_WALLET_ID, dbHelperIncome.getAllToListByWalletId(walletId));
    }

    @Override
    protected Result<List<Income>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID, dbHelperIncome.getAllToListByCategoryId(id));
    }
}

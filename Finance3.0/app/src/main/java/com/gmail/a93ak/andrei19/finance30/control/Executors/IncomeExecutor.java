package com.gmail.a93ak.andrei19.finance30.control.executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperIncome;
import com.gmail.a93ak.andrei19.finance30.model.models.Income;

import java.util.ArrayList;
import java.util.List;

public class IncomeExecutor extends PojoExecutor<Income> {

    public static final int KEY_RESULT_ADD = 601;
    public static final int KEY_RESULT_EDIT = 602;
    public static final int KEY_RESULT_DELETE = 603;
    public static final int KEY_RESULT_GET = 604;
    public static final int KEY_RESULT_GET_ALL = 605;
    public static final int KEY_RESULT_DELETE_ALL = 606;
    public static final int KEY_RESULT_GET_ALL_TO_LIST= 607;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID = 608;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID = 609;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_DATES = 610;

    public IncomeExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<Income> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, DBHelperIncome.getInstance().get(id));
    }

    @Override
    public Result<Long> addPojo(Income income) {
        return new Result<>(KEY_RESULT_ADD, DBHelperIncome.getInstance().add(income));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, DBHelperIncome.getInstance().delete(id));
    }

    @Override
    public Result<Integer> updatePojo(Income income) {
        return new Result<>(KEY_RESULT_EDIT, DBHelperIncome.getInstance().update(income));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, DBHelperIncome.getInstance().getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return  new Result<>(KEY_RESULT_DELETE_ALL, DBHelperIncome.getInstance().deleteAll());
    }

    @Override
    public Result<List<Income>> getAllToList(int selection) {
        return new Result<>(KEY_RESULT_DELETE_ALL, DBHelperIncome.getInstance().getAllToList());
    }

    @Override
    protected Result<List<Income>> getAllToListByDates(ArrayList<Long> dates) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_DATES,DBHelperIncome.getInstance().getAllToListByDates(dates.get(0), dates.get(1)));
    }

    @Override
    protected Result<List<Income>> getAllToListByPurseId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PURSE_ID,DBHelperIncome.getInstance().getAllToListByPurseId(id));
    }

    @Override
    protected Result<List<Income>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_CATEGORY_ID,DBHelperIncome.getInstance().getAllToListByCategoryId(id));
    }
}

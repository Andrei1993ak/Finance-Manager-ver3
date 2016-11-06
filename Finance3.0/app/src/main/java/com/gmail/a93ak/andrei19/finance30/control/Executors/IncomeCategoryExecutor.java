package com.gmail.a93ak.andrei19.finance30.control.Executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryIncome;
import com.gmail.a93ak.andrei19.finance30.model.models.IncomeCategory;


import java.util.List;

public class IncomeCategoryExecutor extends PojoExecutor<IncomeCategory> {

    public static final int KEY_RESULT_ADD = 401;
    public static final int KEY_RESULT_EDIT = 402;
    public static final int KEY_RESULT_DELETE = 403;
    public static final int KEY_RESULT_GET = 404;
    public static final int KEY_RESULT_GET_ALL = 405;
    public static final int KEY_RESULT_DELETE_ALL = 406;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 407;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID = 408;

    private DBHelperCategoryIncome dbHelper = DBHelperCategoryIncome.getInstance();

    public IncomeCategoryExecutor(OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<IncomeCategory> getPojo(long id) {
        return new Result<>(KEY_RESULT_GET, dbHelper.get(id));
    }

    @Override
    public Result<Long> addPojo(IncomeCategory incomeCategory) {
        return new Result<>(KEY_RESULT_ADD, dbHelper.add(incomeCategory));
    }

    @Override
    public Result<Integer> deletePojo(long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelper.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(IncomeCategory incomeCategory) {
        return new Result<>(KEY_RESULT_EDIT, dbHelper.update(incomeCategory));
    }

    @Override
    public Result<Cursor> getAll() {
        return new Result<>(KEY_RESULT_GET_ALL, dbHelper.getAll());
    }

    @Override
    public Result<Integer> deleteAll() {
        return new Result<>(KEY_RESULT_DELETE_ALL, dbHelper.deleteAll());
    }

    @Override
    public Result<List<IncomeCategory>> getAllToList(int selection) {
        if (selection == 0) {
//            all categories
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelper.getAllToList());
        } else if ( selection == 1 ) {
//            parents only
            return new Result<>(KEY_RESULT_GET_ALL_TO_LIST, dbHelper.getAllToListByParentId(-1));
        }
            else return null;
    }

    @Override
    protected Result<List<IncomeCategory>> getAllToListByCategoryId(Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID,dbHelper.getAllToListByParentId(id));
    }
}

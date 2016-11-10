package com.gmail.a93ak.andrei19.finance30.control.executors;

import android.database.Cursor;

import com.gmail.a93ak.andrei19.finance30.control.base.OnTaskCompleted;
import com.gmail.a93ak.andrei19.finance30.control.base.PojoExecutor;
import com.gmail.a93ak.andrei19.finance30.control.base.Result;
import com.gmail.a93ak.andrei19.finance30.model.dbHelpers.DBHelperCategoryCost;
import com.gmail.a93ak.andrei19.finance30.model.models.CostCategory;


import java.util.List;

public class CostCategoryExecutor extends PojoExecutor<CostCategory> {

    public static final int KEY_RESULT_ADD = 501;
    public static final int KEY_RESULT_EDIT = 502;
    public static final int KEY_RESULT_DELETE = 503;
    public static final int KEY_RESULT_GET = 504;
    public static final int KEY_RESULT_GET_ALL = 505;
    public static final int KEY_RESULT_DELETE_ALL = 506;
    public static final int KEY_RESULT_GET_ALL_TO_LIST = 507;
    public static final int KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID = 508;

    private final DBHelperCategoryCost dbHelper = DBHelperCategoryCost.getInstance();

    public CostCategoryExecutor(final OnTaskCompleted listener) {
        super(listener);
    }

    @Override
    public Result<CostCategory> getPojo(final long id) {
        return new Result<>(KEY_RESULT_GET, dbHelper.get(id));
    }

    @Override
    public Result<Long> addPojo(final CostCategory costCategory) {
        return new Result<>(KEY_RESULT_ADD, dbHelper.add(costCategory));
    }

    @Override
    public Result<Integer> deletePojo(final long id) {
        return new Result<>(KEY_RESULT_DELETE, dbHelper.delete(id));
    }

    @Override
    public Result<Integer> updatePojo(final CostCategory costCategory) {
        return new Result<>(KEY_RESULT_EDIT, dbHelper.update(costCategory));
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
    public Result<List<CostCategory>> getAllToList(final int selection) {
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
    protected Result<List<CostCategory>> getAllToListByCategoryId(final Long id) {
        return new Result<>(KEY_RESULT_GET_ALL_TO_LIST_BY_PARENT_ID,dbHelper.getAllToListByParentId(id));
    }
}
